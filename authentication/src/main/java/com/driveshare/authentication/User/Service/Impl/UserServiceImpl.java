package com.driveshare.authentication.User.Service.Impl;

import java.security.Principal;
import java.util.Optional;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.driveshare.authentication.Exception.ResourceNotFoundException;
import com.driveshare.authentication.Exception.ValidationException;
import com.driveshare.authentication.User.DTO.ChangePasswordRequestDTO;
import com.driveshare.authentication.User.DTO.UserResponseDTO;
import com.driveshare.authentication.User.Entity.User;
import com.driveshare.authentication.User.Repository.UserRepository;
import com.driveshare.authentication.User.Service.UserService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;

   
    @Override
    public Optional<UserResponseDTO> getUserByEmail(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with email: " + email));
        var userResponse = UserResponseDTO.builder()
                .name(user.getName())
                .lastName(user.getLastName())
                .email(user.getUnormilezedEmail())
                .role(user.getRole())
                .build();
        return Optional.of(userResponse);
    }


    @Override
    public void changePassword(ChangePasswordRequestDTO request, Principal connectedUser) {
        var user = (User) ((UsernamePasswordAuthenticationToken) connectedUser).getPrincipal();
        if (!passwordEncoder.matches(request.getCurrentPassword(), user.getPassword())) {
            throw new ValidationException("Wrong password");
        }
        if (!request.getNewPassword().equals(request.getConfirmationPassword())) {
            throw new ValidationException("Password are not the same");
        }
        user.setPassword(passwordEncoder.encode(request.getNewPassword()));
        userRepository.save(user);
    }

}
