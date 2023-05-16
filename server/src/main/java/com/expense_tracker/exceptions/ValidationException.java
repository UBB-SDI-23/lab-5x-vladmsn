package com.expense_tracker.exceptions;

public class ValidationException  extends RuntimeException {
    public ValidationException(String message) {
        super(message);
    }
}
