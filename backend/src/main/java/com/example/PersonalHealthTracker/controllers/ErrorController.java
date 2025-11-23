package com.example.personalhealthtracker.controllers;

import com.example.personalhealthtracker.domain.dto.ApiErrorResponse;
import com.example.personalhealthtracker.exceptions.ExpiredRefreshTokenException;
import com.example.personalhealthtracker.exceptions.InvalidRefreshTokenException;
import com.example.personalhealthtracker.exceptions.RevokedRefreshTokenException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingRequestCookieException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;

@RestController
@ControllerAdvice
@Slf4j
public class ErrorController {
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiErrorResponse> handleExcepton(Exception ex){
        log.error("Caught exception",ex);
        ApiErrorResponse error = ApiErrorResponse.builder()
                .status(HttpStatus.INTERNAL_SERVER_ERROR.value())
                .message("An unexpected error occurred")
                .build();
        return  new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
    }
    @ExceptionHandler(IllegalArgumentException.class)
    public  ResponseEntity<ApiErrorResponse> handleIllegalArgumentException(IllegalArgumentException ex){
        ApiErrorResponse error= ApiErrorResponse.builder()
                .status(HttpStatus.BAD_REQUEST.value())
                .message(ex.getMessage())
                .build();
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<ApiErrorResponse> handleBadCredentialsExcepton(BadCredentialsException ex) {
        ApiErrorResponse error = ApiErrorResponse.builder()
                .status(HttpStatus.UNAUTHORIZED.value())
                .message("Incorrect username or password")
                .build();
        return new ResponseEntity<>(error, HttpStatus.UNAUTHORIZED);
    }
    @ExceptionHandler(MissingRequestCookieException.class)
    public ResponseEntity<ApiErrorResponse> handleMissingCookie(MissingRequestCookieException ex) {
        ApiErrorResponse error = ApiErrorResponse.builder()
                .status(HttpStatus.UNAUTHORIZED.value())
                .message("Authorization token is required")
                .build();
        return new ResponseEntity<>(error, HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiErrorResponse> handleInvalidRequest(MethodArgumentNotValidException ex) {
        ApiErrorResponse error = ApiErrorResponse.builder()
                .status(HttpStatus.BAD_REQUEST.value())
                .message(ex.getMessage())
                .build();
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);

    }
    @ExceptionHandler(ExpiredRefreshTokenException.class)
    public ResponseEntity<ApiErrorResponse> handleExpiredRefreshToken(ExpiredRefreshTokenException ex) {
        ApiErrorResponse error = ApiErrorResponse.builder()
                .status(HttpStatus.UNAUTHORIZED.value())
                .message(ex.getMessage())
                .build();
        return new ResponseEntity<>(error, HttpStatus.UNAUTHORIZED);

    }
    @ExceptionHandler(InvalidRefreshTokenException.class)
    public ResponseEntity<ApiErrorResponse> handleInvalidRefreshToken(InvalidRefreshTokenException ex) {
        ApiErrorResponse error = ApiErrorResponse.builder()
                .status(HttpStatus.UNAUTHORIZED.value())
                .message(ex.getMessage())
                .build();
        return new ResponseEntity<>(error, HttpStatus.UNAUTHORIZED);

    }
    @ExceptionHandler(RevokedRefreshTokenException.class)
    public ResponseEntity<ApiErrorResponse> handleRevokedRefreshToken(RevokedRefreshTokenException ex) {
        ApiErrorResponse error = ApiErrorResponse.builder()
                .status(HttpStatus.UNAUTHORIZED.value())
                .message(ex.getMessage())
                .build();
        return new ResponseEntity<>(error, HttpStatus.UNAUTHORIZED);

    }
}
