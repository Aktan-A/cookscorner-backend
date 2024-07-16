package com.neobis.cookscorner.dto.auth;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RegisterRequestDto {

    @NotEmpty(message = "Name is required")
    @Size(max = 300, message = "Name must not be longer than 300 characters")
    private String name;

    @NotEmpty(message = "Email is required")
    @Email(message = "Invalid email format", regexp = "^[a-zA-Z0-9_!#$%&'*+/=?`{|}~^.-]+@[a-zA-Z0-9.-]+$")
    @Size(max = 300, message = "Email must not be longer than 300 characters")
    private String email;

    @NotEmpty(message = "Password is required")
    private String password;

}
