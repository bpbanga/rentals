package com.openclassrooms.rentals.services;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.openclassrooms.rentals.dto.LoginRequestDto;
import com.openclassrooms.rentals.dto.TokenResponseDto;
import com.openclassrooms.rentals.exceptions.AuthenticatedUserNotFound;
import com.openclassrooms.rentals.security.JwtService;

@Service
public class AuthenticationService {
    
        private final AuthenticationManager authenticationManager;
        private final JwtService jwtService;

    public AuthenticationService(AuthenticationManager authenticationManager, JwtService jwtService) {
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
    }

    public TokenResponseDto authenticate(LoginRequestDto request) throws Exception {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword()));

            String token = jwtService.generateToken(authentication);
            return new TokenResponseDto(token);

        } catch (AuthenticationException e) {
            throw new AuthenticationServiceException("Authentication failed", e);
        }
    }

     public String getAuthenticatedUserEmail() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication != null && authentication.isAuthenticated()) {
            return authentication.getName();
        } else {
            throw new AuthenticatedUserNotFound("No authenticated user found in Security Context",
                    "AuhtenticationService.getAuthenticatedUserEmail");
        }
    }
}
