package com.expense_tracker.model.util;

import com.expense_tracker.model.BankAccount;
import com.expense_tracker.model.db.BankAccountEntity;
import lombok.experimental.UtilityClass;

@UtilityClass
public class BankAccountConverter {

    public static BankAccount convertToBankAccount(BankAccountEntity bankAccountEntity) {
        return BankAccount.builder()
                .id(bankAccountEntity.getId())
                .name(bankAccountEntity.getName())
                .lastFourDigits(bankAccountEntity.getCardNumber().substring(12))
                .currency(bankAccountEntity.getCurrency())
                .build();
    }
}
