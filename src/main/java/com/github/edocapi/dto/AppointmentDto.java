package com.github.edocapi.dto;

import java.time.LocalDate;
import java.time.LocalTime;

public record AppointmentDto(
        Long id,
        LocalTime time,
        LocalDate date,
        boolean isOnline,
        Long doctorId,
        Long userId
) {
}
