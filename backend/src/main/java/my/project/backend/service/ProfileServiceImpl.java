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

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import my.project.backend.util.JwtUtils;

import java.time.Duration;
import java.util.HashMap;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ProfileServiceImpl implements ProfileService {


    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;


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
