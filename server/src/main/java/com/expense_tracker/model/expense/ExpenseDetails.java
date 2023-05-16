package com.expense_tracker.model.expense;

import com.expense_tracker.model.User;
import lombok.Builder;
import lombok.Data;

import java.util.Date;
import java.util.HashMap;

@Data
@Builder
public class ExpenseDetails {
    private Integer id;
    private Integer groupId;
    private Double amount;
    private String currency;
    private String category;
    private String description;
    private Date transactionDate;
    private User payer;
    private HashMap<User, Double> participants_amounts;
}
