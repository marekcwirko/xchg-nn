package org.example.handler;

import org.example.service.ExchangeRateUnavailableException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ExchangeRateUnavailableException.class)
    public ResponseEntity<String> handleExchangeServiceNotAvailable(ExchangeRateUnavailableException ex) {
        return ResponseEntity
                .status(HttpStatus.SERVICE_UNAVAILABLE)
                .body(ex.getMessage());
    }

    @ExceptionHandler(ExchangeRateUnavailableException.class)
    public ResponseEntity<String> handleNoAccountForIdentifier(ExchangeRateUnavailableException ex) {
        return ResponseEntity
                .status(HttpStatus.SERVICE_UNAVAILABLE)
                .body(ex.getMessage());
    }
}
