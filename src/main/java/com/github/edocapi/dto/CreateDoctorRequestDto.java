package com.github.edocapi.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import java.util.Set;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
@AllArgsConstructor
@NoArgsConstructor
public class CreateDoctorRequestDto{
        @NotBlank
        private String firstName;
        @NotBlank
        private String lastName;
        @NotBlank
        @Pattern(regexp = "^\\+?3?8?(0\\d{9})$")
        private String phone;
        @NotNull
        private Set<Long> specialtyIds;
        private String background;
        @NotNull
        private int experience;
}
