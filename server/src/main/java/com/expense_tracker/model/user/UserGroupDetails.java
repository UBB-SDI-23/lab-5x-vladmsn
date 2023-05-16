package com.expense_tracker.model.user;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;

@Data
@Builder
public class UserGroupDetails {
    private Integer userId;
    private String username;
    private String email;
    private String firstName;
    private String lastName;
    private String preferredCurrency;
    private LocalDate createdDate;
    private Double balance;
    private Double totalPayed;
    private Double totalShare;
}
