package com.github.edocapi.model;

import java.time.LocalTime;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class Slot {
    private LocalTime time;

    public boolean isAvailable(Set<LocalTime> appointmentsHours, Set<LocalTime> lunchHours) {
        return !appointmentsHours.contains(time)
                && !lunchHours.stream()
                .map(LocalTime::getHour)
                .collect(Collectors.toSet()).contains(time.getHour());
    }
}
