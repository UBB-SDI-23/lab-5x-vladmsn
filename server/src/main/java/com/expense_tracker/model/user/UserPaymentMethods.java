package com.expense_tracker.model.user;

import com.expense_tracker.model.BankAccount;
import lombok.Builder;
import lombok.Data;

import java.util.Set;

@Data
@Builder
public class UserPaymentMethods {
    private Integer id;
    private String username;
    private String preferredCurrency;
    private Set<BankAccount> bankAccounts;
}
