package my.project.backend.service;

import lombok.RequiredArgsConstructor;
import my.project.backend.entity.UserEntity;
import my.project.backend.enumeration.ResponseStatus;
import my.project.backend.io.*;
import my.project.backend.repository.UserRepository;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;

import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import my.project.backend.util.JwtUtils;

import java.time.Duration;
import java.util.HashMap;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;

@Service
@RequiredArgsConstructor

public class ProfileServiceImpl implements ProfileService {


    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final EmailService emailService;


    @Override
    public CommonResponse createProfile(ProfileRequest profile)throws ResponseStatusException {
        CommonResponse response = new CommonResponse();
        try {
            UserEntity newUser = convertToUserEntity(profile);
            if (userRepository.existsByEmail(profile.getEmail())) {
                throw new ResponseStatusException(HttpStatus.CONFLICT, "Email already exists");
            }
            newUser.setPassword(passwordEncoder.encode(profile.getPassword()));
            newUser = userRepository.save(newUser);
            emailService.sendSimpleMail(newUser.getEmail(), newUser.getName());
            ProfileResponse newResponse = convertToProfileResponse(newUser);

            response.setCode(HttpStatus.CREATED.value());
            response.setStatus(ResponseStatus.SUCCESS);
            response.setSuccessMessage("Profile created successfully");
            response.setData(newResponse);
        }catch (Exception e) {
            response.setCode(HttpStatus.CONFLICT.value());
            response.setStatus(ResponseStatus.FAILURE);
            response.setSuccessMessage(e.getMessage());
        }
        return response;
    }

    @Override
    public CommonResponse getProfile(String email) {
        CommonResponse response = new CommonResponse();
        try{
            UserEntity user = userRepository.findByEmail(email)
                    .orElseThrow(()->new ResponseStatusException(HttpStatus.NOT_FOUND, "User Not Found"));
            response.setCode(HttpStatus.OK.value());
            response.setStatus(ResponseStatus.SUCCESS);
            response.setSuccessMessage("Profile found");
            response.setData(convertToProfileResponse(user));
        }catch(Exception e) {
            response.setCode(HttpStatus.NOT_FOUND.value());
            response.setStatus(ResponseStatus.FAILURE);
            response.setSuccessMessage(e.getMessage());
        }
        return response;
    }

    @Override
    public void sendResetOtp(String email) {
        UserEntity user = userRepository.findByEmail(email)
                .orElseThrow(()->new ResponseStatusException(HttpStatus.NOT_FOUND, "User Not Found "+email));
        String otp = String.valueOf(ThreadLocalRandom.current().nextInt(100000,1000000));
        System.out.println("/in send reset otp service");
        long expiryTime  = System.currentTimeMillis() + (15 *60 * 1000);

        user.setReset0tp(otp);
        user.setResetOtpExpireAt(expiryTime);
        userRepository.save(user);

        try{
            emailService.sentResetOtpMail(email,otp);
        }catch (Exception e) {
            throw new RuntimeException("Unable to send email...");
        }

    }

    @Override
    public void resetPassword(String email, String otp, String newPassword) {


            UserEntity currentUser = userRepository.findByEmail(email).
                    orElseThrow(() -> new RuntimeException("User Not Found"));
            if (currentUser.getReset0tp() == null || !currentUser.getReset0tp().equals(otp)) {
                throw new RuntimeException( "OTP does not match");
            }
            if(currentUser.getResetOtpExpireAt() < System.currentTimeMillis()) {
                throw new RuntimeException("OTP expired");
            }
            currentUser.setPassword(passwordEncoder.encode(newPassword));
            currentUser.setReset0tp(null);
            currentUser.setResetOtpExpireAt(0L);
            userRepository.save(currentUser);

    }

    @Override
    public void sendOtp(String email) {
        UserEntity user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User Not Found"));

        if(!user.getIsAccountVerified() || user.getVerifyOtp() == null) {
            String otp = String.valueOf(ThreadLocalRandom.current().nextInt(100000, 1000000));
            long expiryTime = System.currentTimeMillis() + (24 * 60 * 60 * 1000);
            user.setVerifyOtp(otp);
            user.setVerifyOtpExpireAt(expiryTime);
            userRepository.save(user);

            emailService.sentVerifyOtpMail(email, otp);
        }
    }

    @Override
    public void verifyOtp(String email, String otp) {
        UserEntity user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User Not Found"));
        if(!user.getVerifyOtp().equals(otp)) {
            throw new RuntimeException( "OTP does not match");
        }
        if(user.getVerifyOtpExpireAt() < System.currentTimeMillis()) {
            throw new RuntimeException("OTP expired");
        }

        user.setIsAccountVerified(true);
        user.setVerifyOtp(null);
        user.setVerifyOtpExpireAt(0L);
        userRepository.save(user);
    }

    public ProfileResponse convertToProfileResponse (UserEntity user) {
        return ProfileResponse.builder()
                .userId(user.getUserId())
                .name(user.getName())
                .email(user.getEmail())
                .isAccountVerified(user.getIsAccountVerified())
                .build();
    }
    public UserEntity convertToUserEntity(ProfileRequest profile) {
        return UserEntity.builder()
                .userId(UUID.randomUUID().toString())
                .email(profile.getEmail())
                .password(passwordEncoder.encode(profile.getPassword()))
                .name(profile.getName())
                .isAccountVerified(false)
                .resetOtpExpireAt(0L)
                .verifyOtp(null)
                .verifyOtpExpireAt(0L)
                .reset0tp(null)
                .build();
    }
}
