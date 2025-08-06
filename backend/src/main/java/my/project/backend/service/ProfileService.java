package my.project.backend.service;

import my.project.backend.io.*;
import org.springframework.context.annotation.Profile;
import org.springframework.http.ResponseEntity;

public interface ProfileService {
    CommonResponse createProfile(ProfileRequest profileRequest);
}
