package com.github.edocapi.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import java.util.Set;

public record CreateDoctorRequestDto(
        @NotBlank
        String firstName,
        @NotBlank
        String lastName,
        @NotBlank
        @Pattern(regexp = "^\\+?3?8?(0\\d{9})$")
        String phone,
        @NotNull
        Set<Long> specialtyIds,
        String background,
        @NotNull
        int experience
) {

}
