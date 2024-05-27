package com.github.edocapi.dto;

import jakarta.validation.constraints.NotNull;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Set;

public record UpdateScheduleRequestDto(
        int appointmentsDurationInMins,
        @NotNull
        Set<DayOfWeek> workingDays,
        Set<LocalDate> dayOffs,
        @NotNull
        LocalTime startTime,
        Set<LocalTime> lunchHours,
        @NotNull
        LocalTime endTime
) {
}
