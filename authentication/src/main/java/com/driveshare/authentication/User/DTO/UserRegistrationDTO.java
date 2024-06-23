package com.driveshare.authentication.User.DTO;

import com.driveshare.authentication.User.Entity.Role;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserRegistrationDTO {
    private String email;
    private String password;
    private String name;
    private String lastName;
    private Role role;
}