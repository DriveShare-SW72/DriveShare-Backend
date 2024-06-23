package com.driveshare.authentication.User.Validation;

import org.springframework.stereotype.Component;

import com.driveshare.authentication.Exception.ValidationException;

@Component
public class NameValidationService {

    public void validateName(String name) {
        if (name == null || name.trim().isEmpty()) {
            throw new ValidationException("First name is required");
        }
    }

    public void validateLastName(String lastName) {
        if (lastName == null || lastName.trim().isEmpty()) {
            throw new ValidationException("Last name is required");
        }
    }
}
