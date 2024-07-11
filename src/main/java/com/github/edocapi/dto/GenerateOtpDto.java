package com.github.edocapi.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class GenerateOtpDto {
    @NotBlank
    private String phone;
}
