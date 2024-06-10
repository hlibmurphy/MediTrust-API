package com.github.edocapi.dto;

import com.github.edocapi.model.TimePeriod;
import java.time.DayOfWeek;
import java.time.LocalDate;
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
    private TimePeriod workingHours;
    private Set<TimePeriod> lunchHours;
}
