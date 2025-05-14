package com.example.watchmaking.util.expcetions;

public class CreateDiretoryException  extends RuntimeException {
    public CreateDiretoryException(String message, Exception e) {
        super(message, e);
    }

    public CreateDiretoryException(String message) {
        super(message);
    }
}