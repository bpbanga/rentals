package com.openclassrooms.rentals.dto;

import java.util.List;

import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * DTO for a getAll rental response which is a {@link List} of
 * {@link RentalResponse}
 * <p>
 * Represents the response body returned after getAllRentals request
 * </p>
 */
@Schema(description = "Response body returned after getAllRentals request")
@Data
@AllArgsConstructor
public class RentalListDto {
    @ArraySchema(schema = @Schema(implementation = RentalDto.class))
    private List<RentalDto> rentals;
}
