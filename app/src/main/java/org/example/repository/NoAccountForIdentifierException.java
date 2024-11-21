package org.example.repository;

import java.util.NoSuchElementException;

public class NoAccountForIdentifierException extends NoSuchElementException {
    public NoAccountForIdentifierException(String message) {
        super(message);
    }

    public NoAccountForIdentifierException() {
        super("Exchange Service From NBP Not Available");
    }
}
