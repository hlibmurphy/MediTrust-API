package com.github.edocapi.dto;

import java.util.Set;

public record DoctorDtoWithoutScheduleId(
        Long id,
        String firstName,
        String lastName,
        Set<SpecialtyDto> specialty,
        String background,
        int experience,
        double averageRating
) {

}
