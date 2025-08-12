package com.openclassrooms.rentals.exceptions;

import lombok.Getter;

@Getter
public class AuthenticatedUserNotFound extends RuntimeException {
    private String source;

    public AuthenticatedUserNotFound(String message, String source) {
        super(message);
        this.source = source;
    }

    public AuthenticatedUserNotFound(String message) {
        super(message);
    }
}