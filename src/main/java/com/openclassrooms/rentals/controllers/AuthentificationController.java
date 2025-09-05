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
 * <b>AuthentificationController</b>
 * <p>
 * This controller exposes all endpoints related to user authentication
 * and user lifecycle management.
 * </p>
 *
 * <ul>
 *   <li>/api/auth/login → authenticate a user and generate a JWT</li>
 *   <li>/api/auth/register → register a new user</li>
 *   <li>/api/auth/me → fetch authenticated user details</li>
 * </ul>
 *
 * <p>
 * It relies on two main services:
 * <ul>
 *   <li>{@link AuthenticationService} for authentication and JWT management</li>
 *   <li>{@link UserService} for user creation and profile retrieval</li>
 * </ul>
 * </p>
 *
 * <p><b>Logging</b>: This class uses Lombok {@code @Slf4j} to log actions at DEBUG and ERROR levels.</p>
 */
@RestController
@RequestMapping("/api/auth")
@Slf4j
@Tag(name = "AuthController", description = "Authentication related operations")
public class AuthentificationController {

    private final UserService userService;
    private final AuthenticationService authenticationService;

    /**
     * Constructor for the Authentication controller.
     *
     * @param authenticationService the service handling login and JWT management
     * @param userService the service handling user creation and profile retrieval
     */
    public AuthentificationController(AuthenticationService authenticationService, UserService userService) {
        this.authenticationService = authenticationService;
        this.userService = userService;
        log.debug("AuthentificationController initialized.");
    }

    /**
     * Authenticates an existing user and generates a JWT token.
     *
     * @param request {@link LoginRequestDto} containing user email and password
     * @return {@link TokenResponseDto} containing the JWT if authentication succeeds.
     *         Returns {@code 401 Unauthorized} if authentication fails.
     */
    @Operation(summary = "Login to the API", description = "Authenticate with email/password and receive a JWT token.", 
        responses = {
            @ApiResponse(responseCode = "200", description = "Authentication successful"),
            @ApiResponse(responseCode = "401", description = "Invalid credentials or unauthorized")
    })
    @PostMapping("/login")
    public ResponseEntity<TokenResponseDto> login(@RequestBody LoginRequestDto request) {
        log.debug("POST /api/auth/login - Attempting authentication for {}", request.getEmail());
        try {
            TokenResponseDto tokenResponse = authenticationService.authenticate(request);
            return ResponseEntity.ok(tokenResponse);
        } catch (Exception e) {
            log.error("Authentication failed for {}: {}", request.getEmail(), e.getMessage());
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new TokenResponseDto("error"));
        }
    }

    /**
     * Registers a new user in the application.
     * <p>
     * After successful registration, the user is automatically authenticated
     * and a JWT token is returned.
     * </p>
     *
     * @param request {@link RegisterRequestDto} containing user email, password, and name
     * @return {@link TokenResponseDto} containing the JWT token for the newly created user
     * @throws Exception if user creation fails
     */
    @Operation(summary = "Register a new user", description = "Creates a new user and returns a JWT token.", 
        responses = {
            @ApiResponse(responseCode = "200", description = "User successfully registered and authenticated"),
            @ApiResponse(responseCode = "409", description = "User already exists"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PostMapping("/register")
    public ResponseEntity<TokenResponseDto> register(@RequestBody RegisterRequestDto request) throws Exception {
        log.debug("POST /api/auth/register - Registering new user {}", request.getEmail());

        userService.createUser(request.getEmail(), request.getPassword(), request.getName());

        LoginRequestDto loginRequest = new LoginRequestDto(request.getEmail(), request.getPassword());
        TokenResponseDto tokenResponse = authenticationService.authenticate(loginRequest);

        log.debug("User {} successfully created", request.getEmail());

        return ResponseEntity.ok(tokenResponse);
    }

    /**
     * Retrieves information of the currently authenticated user.
     *
     * @return {@link UserProfileResponse} containing authenticated user details,
     *         or {@code 401 Unauthorized} if no user is authenticated.
     */
    @SecurityRequirement(name = "Bearer_Authentication")
    @Operation(summary = "Get user information", description = "Fetch details of the authenticated user using JWT token.", 
        responses = {
            @ApiResponse(responseCode = "200", description = "User profile retrieved successfully"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "500", description = "Error while fetching user details")
    })
    @GetMapping("/me")
    public ResponseEntity<UserProfileResponse> getUserInformations() {
        log.debug("GET /api/auth/me - Fetching authenticated user profile");

        String email = authenticationService.getAuthenticatedUserEmail();

        if (email.isEmpty()) {
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

            log.debug("User profile retrieved successfully: {}", meResponse);
            return ResponseEntity.ok(meResponse);

        } catch (Exception e) {
            log.error("Unexpected error fetching profile for {}: {}", email, e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new UserProfileResponse(null, "", "", "", ""));
        }
    }
}