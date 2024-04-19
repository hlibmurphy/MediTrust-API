package com.github.edocapi.dto;

import jakarta.validation.constraints.Email;
import lombok.Data;
import jakarta.validation.constraints.NotEmpty;

@Data
public class UserRegisterRequestDto {
    @NotEmpty
    private String firstName;

    @NotEmpty
    private String lastName;

    @NotEmpty
    private String phone;

    @Email
    private String email;

    @NotEmpty
    private String password;

    @NotEmpty
    private String repeatPassword;
}
