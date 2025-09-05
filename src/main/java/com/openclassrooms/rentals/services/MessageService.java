package com.openclassrooms.rentals.services;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.openclassrooms.rentals.dto.MessageRequestDto;
import com.openclassrooms.rentals.models.MessageEntity;
import com.openclassrooms.rentals.repositorys.MessageRepository;
import com.openclassrooms.rentals.services.map.MessageMapper;

@Service
public class MessageService {

	private final MessageRepository messageRepo;

	private final MessageMapper messageMapper;

	public MessageService(MessageRepository messageRepo, MessageMapper messageMapper) {
		this.messageRepo = messageRepo;
		this.messageMapper = messageMapper;
	}

	public Optional<MessageEntity> save(MessageRequestDto message) {
		return messageMapper.fromRequest(message).map(m -> messageRepo.save(m));
	}

}
