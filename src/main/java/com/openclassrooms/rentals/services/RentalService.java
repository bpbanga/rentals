// package com.openclassrooms.rentals.services;

// import java.util.List;
// import java.util.Optional;

// import org.springframework.stereotype.Service;

// import com.openclassrooms.rentals.dto.RentalCreationDto;
// import com.openclassrooms.rentals.dto.RentalDto;
// import com.openclassrooms.rentals.dto.RentalUpdateDto;
// import com.openclassrooms.rentals.repositorys.RentalRepository;
// import com.openclassrooms.rentals.services.map.RentalMapper;

// import org.springframework.transaction.annotation.Transactional;

// /**
//  * Implementation of {@link RentalService} responsible for managing rental
//  * entities.
//  * <p>
//  * This service handles operations like retrieving, creating, and updating
//  * rental details.
//  * It communicates with the {@link RentalRepository} for persistence and
//  * {@link UserService} for user validation.
//  * 
//  * @see ModelMapper used to convert {@link RentalEntity} to
//  *      {@link RentalResponseDto}
//  *      with its specific configuration in {@link AppConfig} configuration file.
//  *      </p>
//  */
// @Service
// public class RentalService {

// 	private final RentalMapper mapper;

// 	private final RentalRepository rentalRepo;

// 	private final UserService userSrvc;

// 	private final StorageService files;

// 	public RentalService(RentalRepository rentalRepo, UserService userSrvc, RentalMapper mapper, StorageService files,
// 			StorageService files2) {
// 		this.mapper = mapper;
// 		this.rentalRepo = rentalRepo;
// 		this.userSrvc = userSrvc;
// 		this.files = files2;
// 	}

// 	/**
// 	 * Tries to retrieve a rental from its unique identifier.
// 	 * @param rentalId the rental unique identifier.
// 	 * @return The rental Data Transfer Object if found `Optional.empty()` otherwise.
// 	 */
// 	@Transactional(readOnly = true)
// 	public Optional<RentalDto> findById(Long rentalId) {
// 		return rentalRepo.findById(rentalId).map(u -> mapper.toDto(u));
// 	}

// 	/**
// 	 * @return The list of rental Data Transfer Objects.
// 	 */
// 	@Transactional(readOnly = true)
// 	public List<RentalDto> getAll() {
// 		return rentalRepo.findAll().stream().map(r -> mapper.toDto(r)).toList();
// 	}

// 	/**
// 	 * Store a newly created rental to the database.
// 	 * @param rentalCreation The rental creation information.
// 	 * @return The stored rental Data Transfer Object.
// 	 */
// 	@Transactional
// 	public Optional<RentalDto> create(RentalCreationDto rentalCreation) {
// 		return mapper.fromDto(rentalCreation)
// 			.map(r -> rentalRepo.save(r))
// 			.flatMap(r -> files.store(r, rentalCreation.getPicture()))
// 			.map(r -> mapper.toDto(rentalRepo.save(r)));
// 	}

// 	public Optional<RentalDto> update(Long id, RentalUpdateDto rental) {
// 		return rentalRepo.findById(id).filter(r -> {
// 			return userSrvc.getCurrentUserEntity().map(u -> u.getId() == r.getOwner().getId()).orElse(false);
// 		}).map(r -> mapper.toDto(rentalRepo.save(mapper.updateFromDto(r, rental))));
// 	}

// }


package com.openclassrooms.rentals.services;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.openclassrooms.rentals.dto.RentalCreationDto;
import com.openclassrooms.rentals.dto.RentalDto;
import com.openclassrooms.rentals.dto.RentalUpdateDto;
import com.openclassrooms.rentals.models.RentalEntity;
import com.openclassrooms.rentals.repositorys.RentalRepository;
import com.openclassrooms.rentals.services.map.RentalMapper;

@Service
public class RentalService {

    private final RentalMapper mapper;
    private final RentalRepository rentalRepo;
    private final UserService userSrvc;
    private final StorageService storageService;

    public RentalService(RentalRepository rentalRepo,
                         UserService userSrvc,
                         RentalMapper mapper,
                         StorageService storageService) {
        this.mapper = mapper;
        this.rentalRepo = rentalRepo;
        this.userSrvc = userSrvc;
        this.storageService = storageService;
    }

    /**
     * Retrieve a rental by id.
     */
    @Transactional(readOnly = true)
    public Optional<RentalDto> findById(Long rentalId) {
        return rentalRepo.findById(rentalId).map(mapper::toDto);
    }

    /**
     * Retrieve all rentals.
     */
    @Transactional(readOnly = true)
    public List<RentalDto> getAll() {
        return rentalRepo.findAll().stream().map(mapper::toDto).toList();
    }

    /**
     * Create a new rental with an optional picture.
     */
   @Transactional
public Optional<RentalDto> create(RentalCreationDto rentalCreation, MultipartFile picture) {
    return mapper.fromDto(rentalCreation)
            .map(r -> {
                userSrvc.getCurrentUserEntity().ifPresent(r::setOwner);

                RentalEntity saved = rentalRepo.save(r);

                if (picture != null && !picture.isEmpty()) {
                    storageService.store(saved, picture).ifPresent(updated -> {
                        saved.setPicture(updated.getPicture());
                        rentalRepo.save(saved);
                    });
                }
                return saved;
            })
            .map(mapper::toDto);
}

@Transactional
public Optional<RentalDto> update(Long id, RentalUpdateDto rentalUpdate, MultipartFile picture) {
    return rentalRepo.findById(id)
            .filter(r -> userSrvc.getCurrentUserEntity()
                    .map(u -> u.getId().equals(r.getOwner().getId()))
                    .orElse(false))
            .map(r -> {
                RentalEntity updated = mapper.updateFromDto(r, rentalUpdate);

                if (picture != null && !picture.isEmpty()) {
                    storageService.store(updated, picture).ifPresent(stored -> {
                        updated.setPicture(stored.getPicture());
                    });
                }

                return rentalRepo.save(updated);
            })
            .map(mapper::toDto);
}

}
