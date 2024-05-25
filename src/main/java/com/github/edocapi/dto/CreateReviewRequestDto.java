package com.github.edocapi.dto;

public record CreateReviewRequestDto(
        String text,
        int rating,
        Long doctorId
) {
}
