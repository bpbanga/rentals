package com.openclassrooms.rentals.services.map;

import org.springframework.stereotype.Service;

import com.openclassrooms.rentals.dto.UserDto;
import com.openclassrooms.rentals.dto.UserProfileResponse;
import com.openclassrooms.rentals.models.UserEntity;

@Service
public class UserMapper {

	public UserMapper() {
	}

	public UserDto toDto(UserEntity user) {
		UserDto userDto = new UserDto();
		userDto.setId(user.getId());
		userDto.setEmail(user.getEmail());
		userDto.setName(user.getName());
		userDto.setCreatedAt(user.getCreatedAt());
		userDto.setUpdatedAt(user.getUpdatedAt());
		return userDto;
	}

	public UserEntity fromDTO(UserDto dto) {
    UserEntity user = new UserEntity();
    user.setId(dto.getId());
    user.setEmail(dto.getEmail());
    user.setName(dto.getName());
    user.setCreatedAt(dto.getCreatedAt());
    user.setUpdatedAt(dto.getUpdatedAt());
    return user;
}

public UserProfileResponse toProfileResponse(UserEntity user) {
    return new UserProfileResponse(
        user.getId(),
        user.getName(),
        user.getEmail(),
        user.getCreatedAt().toString(),
        user.getUpdatedAt().toString()
    );
}

}
