package com.expense_tracker.validators;

import com.expense_tracker.exceptions.ValidationException;
import com.expense_tracker.model.db.ExpenseEntity;
import lombok.experimental.UtilityClass;

import java.util.Currency;
import java.util.HashSet;
import java.util.Locale;
import java.util.Set;

@UtilityClass
public class ExpenseValidator {

    public static final Set<String> currencySet = getCurrencySet();

    public static void validateExpense(ExpenseEntity expenseEntity) {
        boolean isValid = true;
        StringBuilder errorMessage = new StringBuilder();

        if (expenseEntity.getPayerId() == null) {
            isValid = false;
            errorMessage.append("Payer id is required. \n");
        }

        if (expenseEntity.getGroupId() == null) {
            isValid = false;
            errorMessage.append("Group id is required. \n");
        }

        if (expenseEntity.getAmount() == null) {
            isValid = false;
            errorMessage.append("Amount is required. \n");
        }
        else if (expenseEntity.getAmount() <= 0) {
            isValid = false;
            errorMessage.append("Amount must be greater than 0. \n");
        }

        if (expenseEntity.getCurrency() == null || expenseEntity.getCurrency().isEmpty()) {
            isValid = false;
            errorMessage.append("Currency is required. \n");
        }
        else if (expenseEntity.getCurrency().length() != 3 && !currencySet.contains(expenseEntity.getCurrency())) {
            isValid = false;
               errorMessage.append("Currency is not valid. \n");
        }

        if (expenseEntity.getCategory() == null || expenseEntity.getCategory().isEmpty()) {
            isValid = false;
            errorMessage.append("Category is required. \n");
        }

        if (expenseEntity.getDescription() == null || expenseEntity.getDescription().isEmpty()) {
            isValid = false;
            errorMessage.append("Description is required. \n");
        }

        if (expenseEntity.getTransactionDate() == null) {
            isValid = false;
            errorMessage.append("Transaction date is required. \n");
        }

        if (!isValid) {
            throw new ValidationException(errorMessage.toString());
        }
    }

    private static Set<String> getCurrencySet() {
        Set<String> currencySet = new HashSet<>();
        for (Locale locale : Locale.getAvailableLocales()) {
            try {
                Currency currency = Currency.getInstance(locale);
                currencySet.add(currency.getCurrencyCode());
            } catch (Exception e) {
                // Ignore locales that don't have a currency code
            }
        }
        return currencySet;
    }
}
