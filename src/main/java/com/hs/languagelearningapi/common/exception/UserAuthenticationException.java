package com.hs.languagelearningapi.common.exception;

public class UserAuthenticationException extends RuntimeException{
    public UserAuthenticationException(String message) {
        super(message);
    }
}
