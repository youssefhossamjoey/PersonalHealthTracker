package com.example.personalhealthtracker.exceptions;

public class ExpiredRefreshTokenException extends RuntimeException {
    public ExpiredRefreshTokenException() {
        super("Refresh token expired");
    }

    public ExpiredRefreshTokenException(Throwable cause) {
        super("Refresh token expired", cause);
    }
}
