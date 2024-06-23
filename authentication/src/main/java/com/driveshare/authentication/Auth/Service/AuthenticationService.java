package com.driveshare.authentication.Auth.Service;

import java.io.IOException;

import com.driveshare.authentication.Auth.DTO.AuthenticationResponseDTO;
import com.driveshare.authentication.User.DTO.UserLoginDTO;
import com.driveshare.authentication.User.DTO.UserRegistrationDTO;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public interface AuthenticationService {

    public AuthenticationResponseDTO register(UserRegistrationDTO userRegistrationDTO);

    public AuthenticationResponseDTO authenticate(UserLoginDTO userLoginDTO);

    public void refreshToken(HttpServletRequest request, HttpServletResponse response) throws IOException;

}