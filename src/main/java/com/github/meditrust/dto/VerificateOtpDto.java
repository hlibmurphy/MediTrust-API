package com.github.meditrust.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class VerificateOtpDto {
    @NotBlank
    private String phone;
    @NotBlank
    private String code;
}
