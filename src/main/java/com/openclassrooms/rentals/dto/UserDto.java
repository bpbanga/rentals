package com.openclassrooms.rentals.dto;


import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;
@Data
public class UserDto {

	private Long id;

	private String email;

	private String name;

	private LocalDateTime createdAt;

	private LocalDateTime updatedAt;

	@JsonProperty("created_at")
	public LocalDateTime getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(LocalDateTime createdAt) {
		this.createdAt = createdAt;
	}

	@JsonProperty("updated_at")
	public LocalDateTime getUpdatedAt() {
		return updatedAt;
	}

	public void setUpdatedAt(LocalDateTime updatedAt) {
		this.updatedAt = updatedAt;
	}

}