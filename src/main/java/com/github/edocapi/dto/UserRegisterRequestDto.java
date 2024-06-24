package com.github.edocapi.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
public class UserRegisterRequestDto {
    @NotEmpty
    private String firstName;

    @NotEmpty
    private String lastName;

    @NotBlank
    @Pattern(regexp = "^\\+?3?8?(0\\d{9})$")
    private String phone;

    @Email
    private String email;

    @NotEmpty
    private String password;

    @NotEmpty
    private String repeatPassword;
}
