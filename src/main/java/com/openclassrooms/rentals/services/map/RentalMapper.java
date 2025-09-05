package com.openclassrooms.rentals.services.map;


import com.openclassrooms.rentals.dto.RentalCreationDto;
import com.openclassrooms.rentals.dto.RentalDto;
import com.openclassrooms.rentals.dto.RentalUpdateDto;
import com.openclassrooms.rentals.models.RentalEntity;
import com.openclassrooms.rentals.services.StorageService;
import com.openclassrooms.rentals.services.UserService;

import java.util.Optional;

import org.springframework.stereotype.Service;


@Service
public class RentalMapper {

	private final UserService userSrvc;

	public RentalMapper(StorageService files, UserService userSrvc) {
		this.userSrvc = userSrvc;
	}

public Optional<RentalEntity> fromDto(RentalCreationDto rental) {
    return userSrvc.getCurrentUserEntity()
        .map(currentUser -> {
            RentalEntity r = new RentalEntity();
            r.setOwner(currentUser); 
            r.setName(rental.getName());
            r.setSurface(rental.getSurface());
            r.setPrice(rental.getPrice());
            r.setDescription(rental.getDescription());
            return r;
        });
}

	public RentalEntity updateFromDto(RentalEntity rental, RentalUpdateDto rentalDto) {
		rental.setName(rentalDto.getName());
		rental.setSurface(rentalDto.getSurface());
		rental.setPrice(rentalDto.getPrice());
		rental.setDescription(rentalDto.getDescription());
		return rental;
	}

	public RentalDto toDto(RentalEntity rental) {
		RentalDto rentalDto = new RentalDto();
		rentalDto.setId(rental.getId());
		rentalDto.setName(rental.getName());
		rentalDto.setSurface(rental.getSurface());
		rentalDto.setPrice(rental.getPrice());
		rentalDto.setPicture(rental.getPicture());
		rentalDto.setDescription(rental.getDescription());
		rentalDto.setOwner(rental.getOwner().getId());
		rentalDto.setCreatedAt(rental.getCreatedAt());
		rentalDto.setUpdatedAt(rental.getUpdatedAt());
		return rentalDto;
	}
}