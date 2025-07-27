package my.project.backend.service;

import lombok.RequiredArgsConstructor;
import my.project.backend.entity.UserEntity;
import my.project.backend.repository.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;


@Service
@RequiredArgsConstructor
public class AppUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
            UserEntity user =  userRepository.findByEmail(email)
                        .orElseThrow(() -> new UsernameNotFoundException("User not found"));

            return new org.springframework.security.core.userdetails
                    .User(user.getEmail(),user.getPassword(),new ArrayList<>());


    }
}
