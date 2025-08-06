package my.project.backend.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import my.project.backend.service.AppUserDetailsService;
import my.project.backend.util.JwtUtils;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

@Component
@RequiredArgsConstructor
public class JwtRequestFilter extends OncePerRequestFilter {

    private final JwtUtils jwtUtils;
    private final AppUserDetailsService appUserDetailsService;

    private final List<String> PUBLIC_URLS = List.of("/login","/register","send-reset-otp","/reset-password","/logout");

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String path = request.getContextPath();
        if(PUBLIC_URLS.contains(path)) {
            filterChain.doFilter(request, response);
        }

        String token = null;
        String email = null;

        // 1. check for token in header
        final String authHeader = request.getHeader("Authorization");
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            token = authHeader.substring(7);
        }

        // 2.check for token in cookies
        if(token == null){
            Cookie[] cookies = request.getCookies();
            if (cookies != null) {
                for(Cookie cookie : cookies) {
                    if (cookie.getName().equals("jwt")) {
                        token = cookie.getValue();
                        break;
                    }
                }
            }
        }

        // 3. validate the token
        if(token != null) {
            email =  jwtUtils.extractEmail(token);
            if(email != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                UserDetails userDetails = appUserDetailsService.loadUserByUsername(email);
                if(jwtUtils.validateToken(token, userDetails)) {
                    UsernamePasswordAuthenticationToken authentication =
                            new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());

                    authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                    SecurityContextHolder.getContext().setAuthentication(authentication);
                }
            }
        }


        filterChain.doFilter(request, response);
    }
}
