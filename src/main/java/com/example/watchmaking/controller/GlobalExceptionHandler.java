package com.example.watchmaking.controller;

import com.example.watchmaking.util.expcetions.NotFoundException;
import com.example.watchmaking.util.expcetions.ResourceExistsException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class GlobalExceptionHandler {

     @ExceptionHandler(NotFoundException.class)
     public ResponseEntity<String> handleNotFoundException(NotFoundException ex) {
         return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
     }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> handleValidationExceptions(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getFieldErrors().forEach(error ->
                errors.put(error.getField(), error.getDefaultMessage())
        );
        return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<String> handleIllegalArgumentException(IllegalArgumentException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
    }

    @ExceptionHandler(ResourceExistsException.class)
    public ResponseEntity<String> handleIllegalArgumentException(ResourceExistsException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
    }

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<String> handleIllegalArgumentException(HttpRequestMethodNotSupportedException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("URI não encontrada");
    }



}
