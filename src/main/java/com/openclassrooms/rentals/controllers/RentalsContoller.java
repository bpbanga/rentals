package com.openclassrooms.rentals.controllers;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.openclassrooms.rentals.dto.RentalCreationDto;
import com.openclassrooms.rentals.dto.RentalDto;
import com.openclassrooms.rentals.dto.RentalListDto;
import com.openclassrooms.rentals.dto.RentalResponseDto;
import com.openclassrooms.rentals.dto.RentalUpdateDto;
import com.openclassrooms.rentals.services.AuthenticationService;
import com.openclassrooms.rentals.services.RentalService;
import com.openclassrooms.rentals.services.StorageService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;

/**
 * <b>RentalsController</b>
 * <p>
 * This controller manages rental-related operations, including:
 * </p>
 * <ul>
 *   <li>Retrieving all rentals</li>
 *   <li>Fetching a rental by its ID</li>
 *   <li>Creating a new rental</li>
 *   <li>Updating an existing rental</li>
 * </ul>
 *
 * <p>
 * It relies on the following services:
 * </p>
 * <ul>
 *   <li>{@link RentalService} - to handle rental business logic</li>
 *   <li>{@link AuthenticationService} - to ensure authenticated access</li>
 *   <li>{@link StorageService} - to handle file storage (e.g., rental pictures)</li>
 * </ul>
 *
 * <p><b>Logging</b>: Uses Lombok {@code @Slf4j} for debug and error logging.</p>
 */
@Tag(name = "RentalsController", description = "Manage rental-related operations")
@RestController
@RequestMapping("/api/rentals")
@Slf4j
public class RentalsContoller extends AbstractController {

    private final RentalService rentalService;

    /**
     * Constructor for RentalsController.
     *
     * @param rentalService the service used to manage rental logic
     * @param storageSrvc the service used to manage file storage
     */
    public RentalsContoller(RentalService rentalService, StorageService storageSrvc) {
        this.rentalService = rentalService;
        log.debug("RentalsController initialized.");
    }

    /**
     * Retrieve all rentals available in the system.
     *
     * @return a {@link RentalListDto} containing the list of rentals
     */
    @Operation(summary = "Get all rentals", description = "Retrieve the complete list of rentals.",
        responses = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved the list of rentals")
    })
    @GetMapping("")
    public ResponseEntity<RentalListDto> getAllRentals() {
        log.debug("GET /api/rentals - Retrieving all rentals");
        RentalListDto rentals = new RentalListDto(rentalService.getAll());
        log.debug("Retrieved {} rentals.", rentals.getRentals().size());
        return ResponseEntity.ok(rentals);
    }

    /**
     * Retrieve a rental by its ID.
     *
     * @param id the rental ID
     * @return a {@link RentalDto} if found, otherwise {@code 404 Not Found}
     */
    @Operation(summary = "Get a rental by ID", description = "Retrieve rental details using its unique identifier.",
        responses = {
            @ApiResponse(responseCode = "200", description = "Rental found and returned successfully"),
            @ApiResponse(responseCode = "404", description = "Rental not found")
    })
    @GetMapping("/{id}")
    public ResponseEntity<RentalDto> getById(@PathVariable Long id) {
        log.debug("GET /api/rentals/{} - Retrieving rental by ID", id);
        ResponseEntity<RentalDto> response = responseFromOptional(rentalService.findById(id));

        if (response.getBody() == null) {
            log.error("Rental with ID {} not found.", id);
        } else {
            log.debug("Rental with ID {} retrieved successfully.", id);
        }

        return response;
    }

    /**
     * Create a new rental.
     *
     * @param rentalCreation the DTO containing rental creation details
     * @return a {@link RentalResponseDto} indicating the result of the creation
     */
    @Operation(summary = "Create a rental", description = "Create a new rental with the provided details.",
        responses = {
            @ApiResponse(responseCode = "200", description = "Rental created successfully"),
            @ApiResponse(responseCode = "406", description = "Invalid rental data provided")
    })
    @PostMapping("")
    public ResponseEntity<RentalResponseDto> createRental(@ModelAttribute RentalCreationDto rentalCreation) {
        log.debug("POST /api/rentals - Creating a new rental: {}", rentalCreation);

        ResponseEntity<RentalResponseDto> response = responseFromOptional(
            rentalService.create(rentalCreation, rentalCreation.getPicture())
                .map(r -> new RentalResponseDto("Rental created")),
            HttpStatus.NOT_ACCEPTABLE
        );

        if (response.getStatusCode() == HttpStatus.OK) {
            log.debug("Rental created successfully.");
        } else {
            log.error("Failed to create rental with data: {}", rentalCreation);
        }

        return response;
    }

    /**
     * Update an existing rental by its ID.
     *
     * @param id the rental ID
     * @param rental the DTO containing updated rental information
     * @return a {@link RentalResponseDto} indicating the update result
     */
    @Operation(summary = "Update a rental", description = "Update an existing rental with new details.",
        responses = {
            @ApiResponse(responseCode = "200", description = "Rental updated successfully"),
            @ApiResponse(responseCode = "404", description = "Rental not found")
    })
    @PutMapping("/{id}")
    public ResponseEntity<RentalResponseDto> update(@PathVariable Long id, @ModelAttribute RentalUpdateDto rental) {
        log.debug("PUT /api/rentals/{} - Updating rental with data: {}", id, rental);

        rentalService.update(id, rental, rental.getPictureUrl());

        log.debug("Rental with ID {} updated successfully.", id);
        return ResponseEntity.ok(new RentalResponseDto("Rental updated"));
    }
}
