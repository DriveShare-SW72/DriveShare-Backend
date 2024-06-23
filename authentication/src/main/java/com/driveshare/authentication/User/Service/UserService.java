package com.driveshare.authentication.User.Service;

import java.security.Principal;
import java.util.Optional;

import com.driveshare.authentication.User.DTO.ChangePasswordRequestDTO;
import com.driveshare.authentication.User.DTO.UserResponseDTO;

public interface UserService {
    public abstract Optional<UserResponseDTO> getUserByEmail(String email);
    public abstract void changePassword(ChangePasswordRequestDTO request, Principal connectedUser);
}

