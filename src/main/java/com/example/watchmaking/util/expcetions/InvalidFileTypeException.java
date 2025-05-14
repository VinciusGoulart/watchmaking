package com.example.watchmaking.util.expcetions;

public class InvalidFileTypeException extends RuntimeException {
    public InvalidFileTypeException(String message, Exception e) {
        super(message, e);
    }
    public InvalidFileTypeException(String message) {
        super(message);
    }

}