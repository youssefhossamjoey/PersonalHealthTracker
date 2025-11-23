package com.example.personalhealthtracker.exceptions;

public class InvalidRefreshTokenException extends RuntimeException {
    public InvalidRefreshTokenException() {
        super("Refresh token cannot be verified");
    }

    public InvalidRefreshTokenException(Throwable cause) {
        super("Refresh token cannot be verified", cause);
    }
}
