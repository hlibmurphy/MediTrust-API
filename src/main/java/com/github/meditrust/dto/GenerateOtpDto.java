package com.github.meditrust.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class GenerateOtpDto {
    @NotBlank
    private String phone;
}
