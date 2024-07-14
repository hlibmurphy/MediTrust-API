package com.github.edocapi.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class RecallRequestDto {
    @NotBlank
    @Pattern(regexp = "^\\+?3?8?(0\\d{9})$")
    private String number;
}
