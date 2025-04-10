package com.market.manager.handler;

import com.market.manager.exception.InvalidDateException;
import com.market.manager.exception.StockPriceNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(StockPriceNotFoundException.class)
    public ResponseEntity<Object> handleStockNotFound(StockPriceNotFoundException ex) {
        var body = new HashMap<String, Object>();
        body.put("error", "Not Found");
        body.put("message", ex.getMessage());
        return new ResponseEntity<>(body, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(InvalidDateException.class)
    public ResponseEntity<Object> handleSInvalidDate(InvalidDateException ex) {
        var body = new HashMap<String, Object>();
        body.put("message", ex.getMessage());
        return new ResponseEntity<>(body, HttpStatus.BAD_REQUEST);
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> handleValidationExceptions(MethodArgumentNotValidException ex) {
        var errors = new HashMap<String, String>();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });
        return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<Map<String, String>> handleInvalidDateFormat(HttpMessageNotReadableException ex) {
        var message = "";

        if (ex.getCause() instanceof com.fasterxml.jackson.databind.exc.InvalidFormatException) {
            var cause = (com.fasterxml.jackson.databind.exc.InvalidFormatException) ex.getCause();
            if (cause.getTargetType().equals(LocalDate.class)) {
                message = "Invalid date value. Expected format is 'YYYY-MM-DD'.";
            }
        }
        var error = new HashMap<String, String>();
        error.put("Error", message);
        return ResponseEntity.badRequest().body(error);
    }

}
