package com.openclassrooms.rentals.controllers;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.openclassrooms.rentals.dto.UserDto;
import com.openclassrooms.rentals.services.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;

/**
 * <b>UserController</b>
 * <p>
 * This controller manages operations related to application users.
 * </p>
 *
 * <ul>
 *   <li>/api/user/{id} → retrieve user details by user ID</li>
 * </ul>
 *
 * <p>
 * It relies on the {@link UserService} to interact with the data layer and
 * provide user information in the form of a {@link UserDto}.
 * </p>
 *
 * <p><b>Logging</b>: Uses Lombok {@code @Slf4j} to log requests and errors.</p>
 */
@RestController
@RequestMapping("/api/user")
@Slf4j
@Tag(name = "UserController", description = "User-related operations")
public class UserController extends AbstractController {

    private final UserService userSrvc;

    /**
     * Constructor for UserController.
     *
     * @param userSrvc the {@link UserService} used to retrieve user information
     */
    public UserController(UserService userSrvc) {
        this.userSrvc = userSrvc;
        log.debug("UserController initialized.");
    }

    /**
     * Retrieves user details by their ID.
     *
     * @param id the ID of the user to fetch
     * @return {@link UserDto} containing user details if found,
     *         otherwise {@code 404 Not Found}.
     */
    @Operation(summary = "Get user by ID", description = "Retrieve a specific user based on their unique identifier.", 
        responses = {
            @ApiResponse(responseCode = "200", description = "User found and returned successfully"),
            @ApiResponse(responseCode = "404", description = "User not found")
    })
    @GetMapping("{id}")
    public ResponseEntity<UserDto> getUserById(@PathVariable Long id) {
        log.debug("GET /api/user/{} - Fetching user by ID", id);

        ResponseEntity<UserDto> response = responseFromOptional(userSrvc.findById(id));

        if (response.getBody() == null) {
            log.error("User with ID {} not found.", id);
        } else {
            log.debug("User with ID {} retrieved successfully: {}", id, response.getBody());
        }

        return response;
    }
}
