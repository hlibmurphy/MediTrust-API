package com.github.meditrust.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
public class UserLoginRequestDto {
    @NotEmpty
    @Pattern(regexp = "^\\+?3?8?(0\\d{9})$")
    private String phone;

    @NotEmpty
    private String password;
}
