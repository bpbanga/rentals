package com.openclassrooms.rentals.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * DTO for error responses.
 * <p>
 * Represents a generic response body for api errors.
 * </p>
 */
@Data
@AllArgsConstructor
public class ApiErrorResponseDto {


    @Schema(description = "A short description of the error", example = "User with id 8 not found")
    private String message;
  
    @Schema(description = "source that throws the error", example = "UserManagementService.getUserbyId")
    private String source;

    public ApiErrorResponseDto(String message) {
        this.message = message;
    }

}
