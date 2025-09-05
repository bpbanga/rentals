package com.openclassrooms.rentals.services.map;


import java.util.Optional;

import org.springframework.stereotype.Service;

import com.openclassrooms.rentals.dto.MessageRequestDto;
import com.openclassrooms.rentals.models.MessageEntity;
import com.openclassrooms.rentals.repositorys.RentalRepository;
import com.openclassrooms.rentals.repositorys.UserRepository;

@Service
public class MessageMapper {

	private final UserRepository userRepo;

	private final RentalRepository rentalRepo;

	public MessageMapper(RentalRepository rentalRepo, UserRepository userRepo) {
		this.userRepo = userRepo;
		this.rentalRepo = rentalRepo;
	}

	public Optional<MessageEntity> fromRequest(MessageRequestDto messageReq) {
		return Optional.of(new MessageEntity()).flatMap(m -> userRepo.findById(messageReq.getUserId()).map(u -> {
			m.setUser(u);
			return m;
		})).flatMap(m -> rentalRepo.findById(messageReq.getRentalId()).map(r -> {
			m.setRental(r);
			return m;
		})).map(m -> {
			m.setMessage(messageReq.getMessage());
			return m;
		});
	}
}
