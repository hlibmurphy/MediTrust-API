package com.github.edocapi.dto;

import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

@Data
public class UserLoginRequestDto {
    @NotEmpty
    private String phone;

    @NotEmpty
    private String password;
}
