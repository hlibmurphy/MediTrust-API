package com.github.edocapi.dto;

public record ReviewDto(
        Long id,
        String text,
        int rating,
        DoctorDtoWithoutScheduleId doctor
) {
}
