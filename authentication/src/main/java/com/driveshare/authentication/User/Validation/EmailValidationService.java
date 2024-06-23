package com.driveshare.authentication.User.Validation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.driveshare.authentication.Exception.ValidationException;
import com.driveshare.authentication.User.Repository.UserRepository;



@Component
public class EmailValidationService {
    @Autowired
    private UserRepository userRepository;

    public void validateEmail(String email) {
        if (email == null || email.trim().isEmpty()) {
            throw new ValidationException("Email is required");
        }
        if (!email.matches("^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$")) {
            throw new ValidationException("Invalid email format");
        }
        if (userRepository.findByEmail(email).isPresent()) {
            throw new ValidationException("Email already in use");
        }
    }
}
