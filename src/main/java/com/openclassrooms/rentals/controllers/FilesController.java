package com.openclassrooms.rentals.controllers;
import java.nio.file.Files;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.openclassrooms.rentals.services.StorageService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.extern.slf4j.Slf4j;

/**
 * REST controller for handling file-related operations.
 *
 * This controller exposes endpoints for retrieving rental-related images stored on the server.
 * It extends {@link AbstractController} to leverage utility methods for building HTTP responses.
 *
 * Key responsibilities:
 * <ul>
 *   <li>Serve rental pictures via HTTP</li>
 *   <li>Determine and set appropriate content types for file responses</li>
 *   <li>Log errors during file access operations</li>
 * </ul>
 *
 * The controller is mapped to <code>/api/files</code> and is designed to be consumed
 * by the frontend application for displaying rental images.
 *
 * @author Pagès
 */
@RestController
@RequestMapping("/api/files")
@Slf4j
public class FilesController extends AbstractController {

    private final StorageService storageSrvc;

    /**
     * Constructor for injecting the storage service dependency.
     *
     * @param storageSrvc the service responsible for loading files from disk
     */
    public FilesController(StorageService storageSrvc) {
        this.storageSrvc = storageSrvc;
    }

    /**
     * Retrieves a specific rental picture by rental ID and filename.
     *
     * @param id the ID of the rental associated with the image
     * @param filename the exact name of the image file to retrieve
     * @return a {@link ResponseEntity} containing the image file as a {@link FileSystemResource},
     *         or an appropriate error response if the file is not found or unreadable
     */
    @Operation(summary = "Get the picture linked to a rental.")
    @GetMapping("/rentalpicture/{id}/{filename:.+}")
    public ResponseEntity<?> getRentalPicture(@PathVariable Long id, @PathVariable String filename) {
        return responseFromOptional(
            storageSrvc.load(id, filename),
            file -> {
                try {
                    String contentType = Files.probeContentType(file.toPath());
                    if (contentType == null) {
                        contentType = MediaType.APPLICATION_OCTET_STREAM_VALUE;
                    }
                    return ResponseEntity.ok()
                            .contentType(MediaType.parseMediaType(contentType))
                            .body(new FileSystemResource(file));
                } catch (Exception e) {
                    log.error("Erreur lecture fichier {}: {}", file.getName(), e.getMessage());
                    return ResponseEntity.internalServerError().build();
                }
            }
        );
    }
}
