package com.driveshare.authentication.User.DTO;

import com.driveshare.authentication.User.Entity.Role;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserResponseDTO {
    
    private String email;
    private String name;
    private String lastName;
    private Role role;

}