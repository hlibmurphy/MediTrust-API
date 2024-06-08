package com.github.edocapi.dto;

import jakarta.validation.constraints.NotNull;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Set;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

@Accessors(chain = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UpdateScheduleRequestDto {
    private int appointmentsDurationInMins;
    @NotNull
    private Set<DayOfWeek> workingDays;
    private Set<LocalDate> dayOffs;
    @NotNull
    private LocalTime startTime;
    private Set<LocalTime> lunchHours;
    @NotNull
    private LocalTime endTime;
}
