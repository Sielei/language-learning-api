package com.hs.languagelearningapi.common.exception.handlers;

import com.hs.languagelearningapi.common.exception.ResourceNotFoundException;
import com.hs.languagelearningapi.common.exception.UserAuthenticationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ResourceNotFoundException.class)
    ProblemDetail handleResourceNotFoundException(ResourceNotFoundException e){
        var location = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path("/api/v1/errors/resource-not-found")
                .build().toUri();
        var problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.NOT_FOUND, e.getMessage());
        problemDetail.setTitle("Resource Not Found");
        problemDetail.setType(location);
        problemDetail.setProperty("timestamp", DateTimeFormatter
                .ofPattern("yyyy-MM-dd HH:mm:ss", Locale.ENGLISH)
                .format(LocalDateTime.now()));
        return problemDetail;
    }

    @ExceptionHandler(value = {BadCredentialsException.class, UserAuthenticationException.class})
    ProblemDetail handleBadCredentialsException(RuntimeException e){
        var location = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path("/api/v1/errors/authentication-error")
                .build().toUri();
        var problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.UNAUTHORIZED, e.getMessage());
        problemDetail.setTitle("Authentication Error");
        problemDetail.setType(location);
        problemDetail.setProperty("timestamp", DateTimeFormatter
                .ofPattern("yyyy-MM-dd HH:mm:ss", Locale.ENGLISH)
                .format(LocalDateTime.now()));
        return problemDetail;
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    ProblemDetail handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        var location = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path("/api/v1/errors/validation-error")
                .build().toUri();
        var problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, e.getMessage());
        problemDetail.setTitle("Validation Error");
        problemDetail.setType(location);
        problemDetail.setProperty("timestamp", DateTimeFormatter
                .ofPattern("yyyy-MM-dd HH:mm:ss", Locale.ENGLISH)
                .format(LocalDateTime.now()));
        var errors = new HashMap<String, List<String>>();
        e.getBindingResult().getAllErrors().forEach(error -> {
            String fieldName = ((FieldError) error).getField();
            if (!errors.containsKey(fieldName)) {
                errors.put(fieldName,
                        new ArrayList<>(Collections.singletonList(error.getDefaultMessage())));
            }
            else {
                errors.get(fieldName).add(error.getDefaultMessage());
            }
        });
        problemDetail.setProperty("errors", errors);
        return problemDetail;
    }
}
