package com.example.skillgap.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Custom runtime exception thrown when authentication fails or access is forbidden.
 */
@ResponseStatus(HttpStatus.UNAUTHORIZED)
public class UnauthorizedException extends RuntimeException {

    /**
     * Constructs a new UnauthorizedException with the specified detail message.
     *
     * @param message descriptive explanation of the authorization failure
     */
    public UnauthorizedException(String message) {
        super(message);
    }
}
