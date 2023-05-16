package com.expense_tracker.validators;

import com.expense_tracker.exceptions.ValidationException;
import com.expense_tracker.model.db.UserEntity;
import lombok.experimental.UtilityClass;

@UtilityClass
public class UserValidator {

    public static void validateUser(UserEntity userEntity) {

        boolean isValid = true;
        StringBuilder errorMessage = new StringBuilder();

        if (userEntity.getFirstName() == null || userEntity.getFirstName().isEmpty()) {
            isValid = false;
            errorMessage.append("First name is required. \n");
        }

        if (userEntity.getLastName() == null || userEntity.getLastName().isEmpty()) {
            isValid = false;
            errorMessage.append("Last name is required. \n");
        }

        if (userEntity.getEmail() == null || userEntity.getEmail().isEmpty()) {
            isValid = false;
            errorMessage.append("Email is required. \n");
        }
        else if (!isEmailValid(userEntity.getEmail())) {
            isValid = false;
            errorMessage.append("Email is not valid. \n");
        }

        if (userEntity.getUsername() == null || userEntity.getUsername().isEmpty()) {
            isValid = false;
            errorMessage.append("Username is required. \n");
        }

        if (userEntity.getPassword() == null || userEntity.getPassword().isEmpty()) {
            isValid = false;
            errorMessage.append("Password is required. \n");
        }

        if (!isValid) {
            throw new ValidationException(errorMessage.toString());
        }
    }

    private boolean isEmailValid(String email) {
        String emailRegex ="^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$";
        return email.matches(emailRegex);
    }
}
