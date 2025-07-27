package my.project.backend.controller;

import lombok.RequiredArgsConstructor;
import my.project.backend.io.AuthRequest;
import my.project.backend.io.AuthResponse;
import my.project.backend.io.CommonResponse;
import my.project.backend.repository.UserRepository;
import my.project.backend.service.AppUserDetailsService;
import my.project.backend.service.ProfileService;
import my.project.backend.util.JwtUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;


import java.time.Duration;
import java.util.HashMap;

@RestController
@RequiredArgsConstructor
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final AppUserDetailsService appUserDetailsService;
    private final JwtUtils jwtUtils;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody AuthRequest authRequest){
        try{

            Authentication authentication = authenticationManager
                    .authenticate(new UsernamePasswordAuthenticationToken(authRequest.getEmail(), authRequest.getPassword()));

            UserDetails userDetails = (UserDetails) authentication.getPrincipal();

            final String jwt = jwtUtils.generateToken(userDetails);
            System.out.println("/in login");
            ResponseCookie cookie = ResponseCookie.from("jwt",jwt)
                    .httpOnly(true)
                    .path("/")
                    .maxAge(Duration.ofDays(1))
                    .sameSite("Strict")
                    .build();
            return ResponseEntity.ok().header(HttpHeaders.SET_COOKIE, cookie.toString())
                    .body(new AuthResponse(authRequest.getEmail(), jwt));

        }catch (BadCredentialsException e){
            HashMap<String,Object> error = new HashMap<>();
            error.put("email",true);
            error.put("message","Email or password is incorrect");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(error);
        }catch (DisabledException e){
            HashMap<String,Object> error = new HashMap<>();
            error.put("email",true);
            error.put("message","Account is disabled");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(error);
        }catch (Exception e){
            HashMap<String,Object> error = new HashMap<>();
            error.put("email",true);
            error.put("message","Authentication Failed");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(error);
        }
    }

}
