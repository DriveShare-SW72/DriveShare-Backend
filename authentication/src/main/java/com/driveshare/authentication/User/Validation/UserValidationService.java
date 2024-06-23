package com.driveshare.authentication.User.Validation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.driveshare.authentication.User.DTO.UserRegistrationDTO;

@Component
public class UserValidationService {


    @Autowired
    private EmailValidationService emailValidationService;

    @Autowired
    private PasswordValidationService passwordValidationService;

    @Autowired
    private NameValidationService nameValidationService;

    public void validateUserRegistration(UserRegistrationDTO user) {
        emailValidationService.validateEmail(user.getEmail());
        passwordValidationService.validatePassword(user.getPassword());
        nameValidationService.validateName(user.getName());
        nameValidationService.validateLastName(user.getLastName());
    }
}
