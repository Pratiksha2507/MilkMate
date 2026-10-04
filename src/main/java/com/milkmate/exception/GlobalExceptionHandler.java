package com.milkmate.exception;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Map<String, String>>
    handleIllegalArgumentException(
            IllegalArgumentException ex) {

        Map<String, String> error =
                new HashMap<>();

        error.put(
                "error",
                "Bad Request"
        );

        error.put(
                "message",
                ex.getMessage() != null
                        ? ex.getMessage()
                        : "Invalid request"
        );

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(error);
    }


    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<Map<String, String>>
    handleResourceNotFoundException(
            ResourceNotFoundException ex) {

        Map<String, String> error =
                new HashMap<>();

        error.put(
                "error",
                "Resource Not Found"
        );

        error.put(
                "message",
                ex.getMessage()
        );

        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(error);
    }


    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>>
    handleValidationException(
            MethodArgumentNotValidException ex) {

        Map<String, String> error =
                new HashMap<>();

        StringBuilder message =
                new StringBuilder();

        ex.getBindingResult()
                .getFieldErrors()
                .forEach(fieldError -> {

                    String field =
                            fieldError.getField();

                    String fieldMessage =
                            fieldError.getDefaultMessage();

                    error.put(
                            field,
                            fieldMessage
                    );

                    if (message.length() > 0) {
                        message.append(" ");
                    }

                    message.append(fieldMessage);
                });

        error.put(
                "error",
                "Validation Error"
        );

        error.put(
                "message",
                message.toString()
        );

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(error);
    }


    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, String>>
    handleGenericException(
            Exception ex) {

        ex.printStackTrace();

        Map<String, String> error =
                new HashMap<>();

        error.put(
                "error",
                "Internal Server Error"
        );

        error.put(
                "message",
                ex.getMessage() != null
                        ? ex.getMessage()
                        : "Something went wrong"
        );

        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(error);
    }
}