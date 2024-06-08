package com.github.edocapi.dto;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Set;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
public class DoctorScheduleDto {
    private Long id;
    private int appointmentsDurationInMins;
    private Set<DayOfWeek> workingDays;
    private Set<LocalDate> dayOffs;
    private LocalTime startTime;
    private Set<LocalTime> lunchHours;
    private LocalTime endTime;
}
