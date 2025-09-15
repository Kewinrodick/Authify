package my.project.backend.service;

import my.project.backend.io.*;
import org.springframework.context.annotation.Profile;
import org.springframework.http.ResponseEntity;

public interface ProfileService {
    CommonResponse createProfile(ProfileRequest profileRequest);

    CommonResponse getProfile(String email);

    void sendResetOtp(String email);

    void resetPassword(String email,String otp,String newPassword);

    void sendOtp(String email);

    void verifyOtp(String email,String otp);


}
