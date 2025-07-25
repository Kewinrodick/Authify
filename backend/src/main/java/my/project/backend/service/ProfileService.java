package my.project.backend.service;

import my.project.backend.io.ProfileRequest;
import my.project.backend.io.ProfileResponse;
import org.springframework.context.annotation.Profile;

public interface ProfileService {
    ProfileResponse createProfile(ProfileRequest profileRequest);
}
