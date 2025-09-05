package com.openclassrooms.rentals.dto;

import java.math.BigDecimal;

import org.springframework.web.multipart.MultipartFile;

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
@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "DTO used to update an existing rental")
public class RentalUpdateDto {

    @Schema(description = "Title or name of the rental")
    private String name;

    @Schema(description = "Surface of the rental")
    private BigDecimal surface;

    @Schema(description = "Price of the rental")
    private BigDecimal price;

    @Schema(description = "Detailed description of the rental")
    private String description;

    @Schema(description = "Optional picture URL or path")
    private MultipartFile pictureUrl;
}
