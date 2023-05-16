package com.expense_tracker.model.user;

import com.expense_tracker.model.Expense;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class UserExpenses {
    private Integer id;
    private String username;
    private String preferredCurrency;
    private List<Expense> expenses;
}
