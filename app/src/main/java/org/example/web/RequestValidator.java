package org.example.web;

import org.example.web.dto.CreateAccountRequest;

public class RequestValidator {
    static void validateAccountCreateRequest(CreateAccountRequest request) {
        if (request == null) {
            throw new IllegalDataForAccountCreationException("Request body cannot be null.");
        }
        if (request.getName() == null || request.getName().isEmpty()) {
            throw new IllegalDataForAccountCreationException("Name is required.");
        }
        if (request.getSurname() == null || request.getSurname().isEmpty()) {
            throw new IllegalDataForAccountCreationException("Surname is required.");
        }
        if (request.getBalancePLN() == null || request.getBalancePLN() < 0) {
            throw new IllegalDataForAccountCreationException("Balance must be non-negative.");
        }
    }
}
