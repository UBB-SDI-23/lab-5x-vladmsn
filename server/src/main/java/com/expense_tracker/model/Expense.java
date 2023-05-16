package com.expense_tracker.model;

import lombok.Builder;
import lombok.Data;

import java.util.Date;

@Data
@Builder
public class Expense {
    private Integer id;
    private Integer groupId;
    private Double amount;
    private String currency;
    private String category;
    private String description;
    private Date transactionDate;
    private User payer;
}
