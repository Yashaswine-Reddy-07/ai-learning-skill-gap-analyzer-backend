package com.example.skillgap.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Custom runtime exception thrown when a requested resource is not found in the database.
 */
@ResponseStatus(HttpStatus.NOT_FOUND)
public class ResourceNotFoundException extends RuntimeException {

    /**
     * Constructs a new ResourceNotFoundException with the specified detail message.
     *
     * @param message descriptive explanation of the missing entity
     */
    public ResourceNotFoundException(String message) {
        super(message);
    }
}
