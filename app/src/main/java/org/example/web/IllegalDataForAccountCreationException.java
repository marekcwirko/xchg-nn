package org.example.web;


public class IllegalDataForAccountCreationException extends IllegalArgumentException {
    public IllegalDataForAccountCreationException(String message) {
        super(message);
    }
}
