package com.expense_tracker.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class BankAccount {
    private Integer id;
    private String name;
    private String lastFourDigits;
    private String currency;
}
