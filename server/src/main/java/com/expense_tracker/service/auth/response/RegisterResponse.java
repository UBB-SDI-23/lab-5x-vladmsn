package com.expense_tracker.service.auth.response;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class RegisterResponse {
    private String activationCode;
    private String message;
}
