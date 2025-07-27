package my.project.backend.controller;

import jakarta.validation.Valid;
import my.project.backend.io.CommonResponse;
import my.project.backend.io.ProfileRequest;
import my.project.backend.service.ProfileService;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

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
}
