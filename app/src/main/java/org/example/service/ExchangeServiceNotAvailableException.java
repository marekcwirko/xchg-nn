package org.example.service;

public class ExchangeServiceNotAvailableException extends RuntimeException {
    public ExchangeServiceNotAvailableException(String message) {
        super(message);
    }
    public ExchangeServiceNotAvailableException() {
        super("Exchange Service From NBP Not Available");
    }
}
