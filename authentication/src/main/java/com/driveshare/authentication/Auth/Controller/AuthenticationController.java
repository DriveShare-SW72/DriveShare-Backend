package com.driveshare.authentication.Auth.Controller;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.driveshare.authentication.Auth.DTO.AuthenticationResponseDTO;
import com.driveshare.authentication.Auth.Service.AuthenticationService;
import com.driveshare.authentication.User.DTO.UserLoginDTO;
import com.driveshare.authentication.User.DTO.UserRegistrationDTO;
import com.driveshare.authentication.User.Validation.UserValidationService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;


@RestController
@RequestMapping("/api/v1/auth")
public class AuthenticationController {

    @Autowired
    private  AuthenticationService authenticationService;

    @Autowired
    private  UserValidationService userValidation;

    @Transactional
    @PostMapping("/register")
    public ResponseEntity<AuthenticationResponseDTO> register(@RequestBody UserRegistrationDTO request) {
        userValidation.validateUserRegistration(request);
        AuthenticationResponseDTO registeredUser = authenticationService.register(request);
        return new ResponseEntity<AuthenticationResponseDTO>(registeredUser, HttpStatus.CREATED);
    }

    @PostMapping("/login")
    public ResponseEntity<AuthenticationResponseDTO> authenticate(@RequestBody UserLoginDTO request) {
        AuthenticationResponseDTO loggedStudent = authenticationService.authenticate(request);
        return new ResponseEntity<AuthenticationResponseDTO>(loggedStudent, HttpStatus.OK);
    }

    @PostMapping("/refresh-token")
    public void refreshToken(HttpServletRequest request, HttpServletResponse response) throws IOException {
        authenticationService.refreshToken(request, response);
    }

}
