package org.example.service;

public class ExchangeRateUnavailableException extends RuntimeException {
    public ExchangeRateUnavailableException(String message) {
        super(message);
    }
    public ExchangeRateUnavailableException() {
        super("Exchange Service From NBP Not Available");
    }
}
