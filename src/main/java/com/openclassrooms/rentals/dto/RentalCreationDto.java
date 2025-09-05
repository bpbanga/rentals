package com.openclassrooms.rentals.dto;

import java.math.BigDecimal;

import org.springframework.web.multipart.MultipartFile;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


/**
 * DTO for a rental creation response.
 * <p>
 * Represents the response body returned after rental creation.
 * </p>
 */
@Schema(description = "Response body returned after rental creation")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class RentalCreationDto {
    @Schema(description = "Information message returned", example = "Rental created !")
    private String name;

	private BigDecimal surface;

	private BigDecimal price;

	private String description;

	private MultipartFile picture;

	}
