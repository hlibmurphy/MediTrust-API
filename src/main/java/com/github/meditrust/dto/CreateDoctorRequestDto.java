package com.github.meditrust.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import java.math.BigDecimal;
import java.util.Set;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
@AllArgsConstructor
@NoArgsConstructor
public class CreateDoctorRequestDto {
    @NotBlank
    private String firstName;
    @NotBlank
    private String lastName;
    @NotBlank
    @Pattern(regexp = "^\\+?3?8?(0\\d{9})$")
    private String phone;
    private BigDecimal price;
    @NotNull
    private Set<Long> specialtyIds;
    @NotNull
    private int experience;
    private String background;
    private String about;
    private String serviceOffered;
}
