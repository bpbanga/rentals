package com.openclassrooms.rentals.services;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.*;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import com.openclassrooms.rentals.models.RentalEntity;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class StorageService {

    private final Path rootLocation;

    public StorageService(@Value("${pages.uploadDir}") String uploadDir) {
        this.rootLocation = Path.of(uploadDir);
    }

    @PostConstruct
    public void init() {
        try {
            if (!Files.exists(rootLocation)) {
                Files.createDirectories(rootLocation);
            }
        } catch (IOException e) {
            throw new RuntimeException("Could not initialize storage", e);
        }
    }

    public Optional<RentalEntity> store(RentalEntity rental, MultipartFile file) {
        if (file == null || file.isEmpty()) {
            return Optional.empty();
        }

        try {
            Path rentalFolder = this.rootLocation.resolve(rental.getId().toString());
            if (!Files.exists(rentalFolder)) {
                Files.createDirectories(rentalFolder);
            }

            Path destinationFile = rentalFolder.resolve(file.getOriginalFilename());
            try (InputStream inputStream = file.getInputStream()) {
                Files.copy(inputStream, destinationFile, StandardCopyOption.REPLACE_EXISTING);
            }

            rental.setPicture("/api/files/rentalpicture/" + rental.getId() + "/" + file.getOriginalFilename());
            return Optional.of(rental);

        } catch (IOException e) {
            log.error("Erreur stockage fichier: {}", e.getMessage());
            return Optional.empty();
        }
    }

    /**
     * Charger un fichier précis d’un rental
     * @param rentalId l’ID du rental
     * @param filename le nom exact du fichier
     * @return le fichier si trouvé
     */
    public Optional<File> load(Long rentalId, String filename) {
        try {
            Path rentalFolder = this.rootLocation.resolve(rentalId.toString());
            Path filePath = rentalFolder.resolve(filename);

            if (Files.exists(filePath)) {
                return Optional.of(filePath.toFile());
            } else {
                log.warn("Fichier non trouvé: {}", filePath.toAbsolutePath());
                return Optional.empty();
            }

        } catch (Exception e) {
            log.error("Erreur lors du chargement du fichier pour rentalId {}: {}", rentalId, e.getMessage());
            return Optional.empty();
        }
    }
}
