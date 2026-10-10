package com.example.skillgap.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Custom runtime exception thrown when client payload violates business invariants.
 */
@ResponseStatus(HttpStatus.BAD_REQUEST)
public class BadRequestException extends RuntimeException {

    /**
     * Constructs a new BadRequestException with the specified detail message.
     *
     * @param message descriptive explanation of the validation failure
     */
    public BadRequestException(String message) {
        super(message);
    }
}
