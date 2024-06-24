package com.github.edocapi.dto;

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
public class AvailableSlotsDto {
    private int appointmentDuration;
    private Set<LocalTime> availableTimes;
}
