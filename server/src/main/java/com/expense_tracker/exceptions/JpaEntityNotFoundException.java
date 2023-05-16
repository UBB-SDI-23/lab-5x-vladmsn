package com.expense_tracker.exceptions;

public class JpaEntityNotFoundException extends RuntimeException {
    public JpaEntityNotFoundException(String message) {
        super(message);
    }
}
