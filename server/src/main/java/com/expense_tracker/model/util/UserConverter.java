package com.expense_tracker.model.util;

import com.expense_tracker.model.BankAccount;
import com.expense_tracker.model.Expense;
import com.expense_tracker.model.User;
import com.expense_tracker.model.db.UserEntity;
import com.expense_tracker.model.user.UserExpenses;
import com.expense_tracker.model.user.UserGroupDetails;
import com.expense_tracker.model.user.UserPaymentMethods;
import lombok.experimental.UtilityClass;

import java.util.List;
import java.util.Set;

@UtilityClass
public class UserConverter {

    public static User convertToUser(UserEntity userEntity) {
        return User.builder()
                .id(userEntity.getId())
                .username(userEntity.getUsername())
                .preferredCurrency(userEntity.getPreferredCurrency())
                .email(userEntity.getEmail())
                .firstName(userEntity.getFirstName())
                .lastName(userEntity.getLastName())
                .build();
    }

    public static UserGroupDetails convertToUserGroupDetails(UserEntity userEntity) {
        return UserGroupDetails.builder()
                .userId(userEntity.getId())
                .username(userEntity.getUsername())
                .email(userEntity.getEmail())
                .firstName(userEntity.getFirstName())
                .lastName(userEntity.getLastName())
                .preferredCurrency(userEntity.getPreferredCurrency())
                .createdDate(userEntity.getCreatedAt())
                .build();
    }

    public static UserExpenses convertToUserExpenses(UserEntity userEntity, List<Expense> expenses) {
        return UserExpenses.builder()
                .id(userEntity.getId())
                .username(userEntity.getUsername())
                .preferredCurrency(userEntity.getPreferredCurrency())
                .expenses(expenses)
                .build();
    }

    public static UserPaymentMethods convertToUserPaymentMethods(UserEntity userEntity, Set<BankAccount> bankAccounts) {
        return UserPaymentMethods.builder()
                .id(userEntity.getId())
                .username(userEntity.getUsername())
                .bankAccounts(bankAccounts)
                .preferredCurrency(userEntity.getPreferredCurrency())
                .build();
    }

    public static UserEntity convertToUserEntity(User user) {
        UserEntity userEntity = new UserEntity();
        userEntity.setId(user.getId());
        userEntity.setUsername(user.getUsername());
        userEntity.setPreferredCurrency(user.getPreferredCurrency());
        userEntity.setEmail(user.getEmail());
        userEntity.setFirstName(user.getFirstName());
        userEntity.setLastName(user.getLastName());
        return userEntity;
    }
}
