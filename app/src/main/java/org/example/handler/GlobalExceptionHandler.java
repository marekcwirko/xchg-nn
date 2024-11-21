package org.example.handler;

import org.example.service.ExchangeServiceNotAvailableException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ExchangeServiceNotAvailableException.class)
    public ResponseEntity<String> handleExchangeServiceNotAvailable(ExchangeServiceNotAvailableException ex) {
        return ResponseEntity
                .status(HttpStatus.SERVICE_UNAVAILABLE)
                .body(ex.getMessage());
    }

    @ExceptionHandler(ExchangeServiceNotAvailableException.class)
    public ResponseEntity<String> handleNoAccountForIdentifier(ExchangeServiceNotAvailableException ex) {
        return ResponseEntity
                .status(HttpStatus.SERVICE_UNAVAILABLE)
                .body(ex.getMessage());
    }
}
