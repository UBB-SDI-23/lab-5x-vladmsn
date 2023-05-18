package com.expense_tracker.service.auth.requests;

import com.expense_tracker.model.security.ERole;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RegisterRequest {
    private String username;
    private String password;
    private String email;
    private ERole role = ERole.USER_ROLE;
    private String lastname;
    private String firstname;

}