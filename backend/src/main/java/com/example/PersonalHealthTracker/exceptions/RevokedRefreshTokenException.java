package com.example.personalhealthtracker.exceptions;

public class RevokedRefreshTokenException extends RuntimeException{
    public RevokedRefreshTokenException() {
        super("Refresh token Revoked");
    }

    public RevokedRefreshTokenException(Throwable cause) {
        super("Refresh token Revoked", cause);
    }
}
