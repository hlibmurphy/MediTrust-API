package com.github.edocapi.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

public record CreateDoctorRequestDto(
        @NotBlank
        String firstName,
        @NotBlank
        String lastName,
        @NotBlank
        @Pattern(regexp = "^\\+?3?8?(0\\d{9})$")
        String phone,
        @NotBlank
        String specialty,
        String background,
        @NotNull
        int experience
) {

}
