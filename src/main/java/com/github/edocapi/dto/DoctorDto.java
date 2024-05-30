package com.github.edocapi.dto;

import java.util.Set;

public record DoctorDto(
        Long id,
        String firstName,
        String lastName,
        Set<SpecialtyDto> specialties,
        String background,
        Long scheduleId,
        int experience,
        double averageRating
) {
}
