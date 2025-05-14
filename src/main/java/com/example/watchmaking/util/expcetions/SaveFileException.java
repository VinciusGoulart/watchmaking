package com.example.watchmaking.util.expcetions;

public class SaveFileException extends RuntimeException {
    public SaveFileException(String message, Exception e) {
        super(message, e);
    }

    public SaveFileException(String message) {
        super(message);
    }
}
