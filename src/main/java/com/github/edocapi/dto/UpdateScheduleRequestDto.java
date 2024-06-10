package com.github.edocapi.dto;

import com.github.edocapi.model.TimePeriod;
import jakarta.validation.constraints.NotNull;
import java.time.DayOfWeek;
import java.time.LocalDate;
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
    private TimePeriod workingHours;
    private Set<TimePeriod> lunchHours;
}
