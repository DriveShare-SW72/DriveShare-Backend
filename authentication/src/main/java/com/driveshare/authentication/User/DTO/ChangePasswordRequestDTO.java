package com.driveshare.authentication.User.DTO;

import lombok.Builder;
import lombok.Data;

@Data
@Builder

public class ChangePasswordRequestDTO {
    private String currentPassword;
    private String newPassword;
    private String confirmationPassword;
}
