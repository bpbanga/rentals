package com.openclassrooms.rentals.exceptions;

import lombok.Getter;

@Getter
public class EmailAlreadyUsedException extends RuntimeException {

    private String source;

    public EmailAlreadyUsedException(String message, String source) {
        super(message);
        this.source = source;
    }

    public EmailAlreadyUsedException(String message) {
        super(message);
    }
}