package com.github.meditrust.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

@Accessors(chain = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateReviewRequestDto {
    private String text;
    private int rating;
    private Long doctorId;
}
