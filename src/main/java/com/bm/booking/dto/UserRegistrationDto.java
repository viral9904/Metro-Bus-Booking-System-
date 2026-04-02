package com.bm.booking.dto;

import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class UserRegistrationDto {

    @NotBlank
    private String fullName;

    @Email
    @NotBlank
    private String email;
    private String phone;

    @NotBlank
    @Pattern(
            regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&]).{8,}$",
            message = "Password must contain uppercase, lowercase, number, special character and be at least 8 characters"
    )
    private String password;
}