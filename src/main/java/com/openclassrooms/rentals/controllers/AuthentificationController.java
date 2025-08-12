package com.openclassrooms.rentals.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.openclassrooms.rentals.dto.LoginRequestDto;     
import com.openclassrooms.rentals.dto.RegisterRequestDto;
import com.openclassrooms.rentals.dto.TokenResponseDto;
import com.openclassrooms.rentals.dto.UserProfileResponse;
import com.openclassrooms.rentals.services.AuthenticationService;
import com.openclassrooms.rentals.services.UserService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
/**
 * Controller class used for authentication purpose.
 * <p>
 * This class implements the authentication endpoints of the application.
 * </p>
 * <p>
 * - {@link AuthenticationService} service that process the user authentication
 * - {@link UserService} service used for business operations on users
 * </p>
 * 
 */


@RestController
@RequestMapping("/auth")
@Slf4j
@Tag(name = "AuthController", description = "Process authentication related operations")
public class AuthentificationController {

    private final UserService userService;
    private final AuthenticationService authenticationService;

    public AuthentificationController(AuthenticationService authenticationService, UserService userService) {
        this.authenticationService = authenticationService;
        this.userService = userService;
        log.debug("AuthentificationController initialized.");
    }

     /**
     * Login to the API.
     * 
     * <p>
     * This method authenticates using POST parameters and return back
     * a Json Web Token.
     * 
     * @param {@link LoginRequestDto} the request DTO.
     * @return {@link TokenResponseDto} the response DTO.
     */

    @Operation(summary = "Login to the API", description = "This endpoint allows a user to authenticate by providing credentials. It returns a JWT token.", responses = {
            @ApiResponse(responseCode = "200", description = "Successful authentication, returns a token"),
            @ApiResponse(responseCode = "401", description = "Unauthorized")
    })
    @PostMapping("/login")
    public ResponseEntity<TokenResponseDto> login( @RequestBody LoginRequestDto request) {
        log.debug("@PostMapping(\"/login\")");
        try {
            TokenResponseDto tokenResponse = authenticationService.authenticate(request);
            return ResponseEntity.ok(tokenResponse);
        } catch (Exception e) {
            log.error("Authentication failed: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new TokenResponseDto("error"));
        }
    }

    /**
     * Create a new user.
     * 
     * <p>
     * Use the {@link UserService} to create the new User.
     * Use the {@link AuthenticationService} to retrieve the authenticated user.
     * </p>
     * 
     * @param {@link RegisterRequestDto} the request DTO.
     * @return {@link TokenResponseDto} the response DTO.
     * 
     */
    @Operation(summary = "Register a new user", description = "This endpoint allows a user to register in application. It also authenticate and returns the JWT token.", responses = {
            @ApiResponse(responseCode = "200", description = "Successful authentication, returns a token"),
            @ApiResponse(responseCode = "409", description = "Conflict, the user already exist"),
            @ApiResponse(responseCode = "500", description = "Internal server error"),

    })
    @PostMapping("/register")
    public ResponseEntity<TokenResponseDto> register(@RequestBody RegisterRequestDto request) throws Exception {
        log.debug("@PostMapping(\"/register\") - RegisterRequest: {}", request);

        userService.createUser(
                request.getEmail(),
                request.getPassword(),
                request.getName());

        LoginRequestDto loginRequest = new LoginRequestDto(request.getEmail(), request.getPassword());
        TokenResponseDto tokenResponse = authenticationService.authenticate(loginRequest);

        log.debug("User created successfully: {}", request.getEmail());

        return ResponseEntity.ok(tokenResponse);

    }

    /**
     * Get user informations.
     * 
     * <p>
     * Use the {@link AuthenticationService} to retrieve the authenticated user
     * informations.
     * 
     * </p>
     * 
     * @return {@link UserProfileResponse} the response DTO.
     * 
     */
    @SecurityRequirement(name = "Bearer_Authentication")
    @Operation(summary = "Get user informations", description = "This endpoint allows to retrieve user details by providing JWT token.", responses = {
            @ApiResponse(responseCode = "200", description = "Successfully get the user details"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "500", description = "Error fetching user details for email.")
    })
    @GetMapping("/me")
    public ResponseEntity<UserProfileResponse> getUserInformations() {
        log.debug("@GetMapping(\"/me\")");

        String email = authenticationService.getAuthenticatedUserEmail();

        if (email == "") {
            log.error("No authenticated user found.");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new UserProfileResponse(null, "", "", "", ""));
        }

        try {
            UserProfileResponse meResponse = userService.getUserProfilebyEmail(email);

            if (meResponse.getEmail().isEmpty()) {
                log.error("Error fetching user details for email: {}", email);
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .body(new UserProfileResponse(null, "", "", "", ""));
            }

            log.debug("Return response: {}", meResponse.toString());
            return ResponseEntity.ok(meResponse);

        } catch (Exception e) {
            log.error("Unexpected error: ", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new UserProfileResponse(null, "", "", "", ""));
        }
    }
}
