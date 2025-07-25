package my.project.backend.controller;

import jakarta.validation.Valid;
import my.project.backend.io.ProfileRequest;
import my.project.backend.io.ProfileResponse;
import my.project.backend.service.ProfileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1.0")
public class ProfileController {

    @Autowired
    private ProfileService profileService;

    @PostMapping("/register")
    public ResponseEntity<ProfileResponse> createUserProfile(@Valid @RequestBody ProfileRequest request) {
        ProfileResponse response = profileService.createProfile(request);
        //TODO : Send a Welcome Email
        return ResponseEntity.status(201).body(response);
    }
}
