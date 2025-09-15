package my.project.backend.controller;

import jakarta.validation.Valid;
import my.project.backend.io.*;
import my.project.backend.service.ProfileService;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.CurrentSecurityContext;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@RestController
public class ProfileController {

    @Autowired
    private ProfileService profileService;

    @PostMapping("/register")
    public ResponseEntity<CommonResponse> createUserProfile(@Valid @RequestBody ProfileRequest request) {
        CommonResponse response = profileService.createProfile(request);
        //TODO : Send a Welcome Email
        return ResponseEntity.status(response.getCode()).body(response);
    }

    @GetMapping("/profile")
    public ResponseEntity<CommonResponse> getProfile(@CurrentSecurityContext(expression = "authentication?.name") String email) {
        CommonResponse response = profileService.getProfile(email);
        return ResponseEntity.status(response.getCode()).body(response);
    }

    @PostMapping("/send-reset-otp")
    public ResponseEntity<?> sendResetOtp (@RequestParam String email) {
        try{
            System.out.println("/in send reset otp controller");
            profileService.sendResetOtp(email);
            return ResponseEntity.status(HttpStatus.OK).build();
        }catch (Exception e){
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    

}
