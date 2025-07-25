package my.project.backend.service;

import lombok.RequiredArgsConstructor;
import my.project.backend.entity.User;
import my.project.backend.io.ProfileRequest;
import my.project.backend.io.ProfileResponse;
import my.project.backend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ProfileServiceImpl implements ProfileService {

    @Autowired
    UserRepository userRepository;

    @Override
    public ProfileResponse createProfile(ProfileRequest profile)throws ResponseStatusException {
        User newUser = convertToUserEntity(profile);
            if(userRepository.existsByEmail(profile.getEmail())){
                throw new ResponseStatusException(HttpStatus.CONFLICT, "Email already exists");
            }
            newUser = userRepository.save(newUser);
        return convertToProfileResponse(newUser);
    }
    public ProfileResponse convertToProfileResponse (User user) {
        return ProfileResponse.builder()
                .userId(user.getUserId())
                .name(user.getName())
                .email(user.getEmail())
                .isAccountVerified(user.getIsAccountVerified())
                .build();
    }
    public User convertToUserEntity(ProfileRequest profile) {
        return User.builder()
                .userId(UUID.randomUUID().toString())
                .email(profile.getEmail())
                .password(profile.getPassword())
                .name(profile.getName())
                .isAccountVerified(false)
                .resetOtpExpireAt(0L)
                .verifyOtp(null)
                .verifyOtpExpireAt(0L)
                .reset0tp(null)
                .build();

    }
}
