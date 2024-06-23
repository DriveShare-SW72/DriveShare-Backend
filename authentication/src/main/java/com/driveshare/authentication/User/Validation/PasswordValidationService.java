package com.driveshare.authentication.User.Validation;

import org.springframework.stereotype.Component;

import com.driveshare.authentication.Exception.ValidationException;

@Component
public class PasswordValidationService {
    public void validatePassword(String password) {
        if (password == null || password.isEmpty()) {
            throw new ValidationException("Password is required");
        }
        if (password.length() < 8) {
            throw new ValidationException("Password must be at least 8 characters long");
        }
        if (!password.matches("^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=!])(?=\\S+$).{8,}$")) {
            throw new ValidationException(
                    "Password must contain at least one number, one uppercase, one lowercase letter, and one special character");
        }

    }
}
