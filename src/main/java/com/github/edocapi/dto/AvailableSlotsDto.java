package com.github.edocapi.dto;

import java.time.LocalTime;
import java.util.Set;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class AvailableSlotsDto {
    private int appointmentDuration;
    private Set<LocalTime> availableTimes;
}
