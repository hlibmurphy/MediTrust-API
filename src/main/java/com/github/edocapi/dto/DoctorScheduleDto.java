package com.github.edocapi.dto;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Set;

public record DoctorScheduleDto(
        Long id,
        int appointmentsDurationInMins,
        Set<DayOfWeek> workingDays,
        Set<LocalDate> dayOffs,
        LocalTime startTime,
        Set<LocalTime> lunchHours,
        LocalTime endTime
) {
}
