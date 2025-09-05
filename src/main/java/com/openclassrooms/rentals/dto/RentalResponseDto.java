package com.openclassrooms.rentals.dto;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO for a rental response.
 * <p>
 * Represents the response body returned after rental creation or retrieve.
 * </p>
 */

@Schema(description = "Response body returned after rental creation or retrieve giving the detailed informations about the rental.")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class RentalResponseDto {
private String message;
}
