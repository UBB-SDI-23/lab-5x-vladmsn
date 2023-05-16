package com.expense_tracker.model.util;

import com.expense_tracker.model.Expense;
import com.expense_tracker.model.User;
import com.expense_tracker.model.db.ExpenseEntity;
import com.expense_tracker.model.expense.ExpenseDetails;
import lombok.experimental.UtilityClass;

import java.util.HashMap;

@UtilityClass
public class ExpenseConverter {

    public static Expense convertToExpense(ExpenseEntity expenseEntity) {
        return Expense.builder()
                .id(expenseEntity.getId())
                .groupId(expenseEntity.getGroupId())
                .amount(expenseEntity.getAmount())
                .currency(expenseEntity.getCurrency())
                .category(expenseEntity.getCategory())
                .description(expenseEntity.getDescription())
                .transactionDate(expenseEntity.getTransactionDate())
                .build();
    }

    public static ExpenseDetails convertToExpenseDetails(ExpenseEntity expenseEntity, User payer, HashMap<User, Double> participants_amounts) {
        return ExpenseDetails.builder()
                .id(expenseEntity.getId())
                .groupId(expenseEntity.getGroupId())
                .amount(expenseEntity.getAmount())
                .currency(expenseEntity.getCurrency())
                .category(expenseEntity.getCategory())
                .description(expenseEntity.getDescription())
                .transactionDate(expenseEntity.getTransactionDate())
                .payer(payer)
                .participants_amounts(participants_amounts)
                .build();
    }

    public static ExpenseEntity convertToExpenseEntity(Expense expense) {
        ExpenseEntity expenseEntity = new ExpenseEntity();
        expenseEntity.setId(expense.getId());
        expenseEntity.setAmount(expense.getAmount());
        expenseEntity.setCurrency(expense.getCurrency());
        expenseEntity.setCategory(expense.getCategory());
        expenseEntity.setDescription(expense.getDescription());
        expenseEntity.setTransactionDate(expense.getTransactionDate());
        return expenseEntity;
    }

}
