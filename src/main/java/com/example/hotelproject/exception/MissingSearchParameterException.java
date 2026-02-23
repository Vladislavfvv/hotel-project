package com.example.hotelproject.exception;

public class MissingSearchParameterException extends RuntimeException {
    public MissingSearchParameterException(String message) {
        super(message);
    }
}
