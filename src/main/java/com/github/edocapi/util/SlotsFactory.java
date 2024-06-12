package com.github.edocapi.util;

import com.github.edocapi.model.DoctorSchedule;
import java.time.LocalTime;
import java.util.HashSet;
import java.util.Set;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class SlotsFactory {
    private LocalTime startTime;
    private int durationInMins;
    private int numberOfSlots;

    public static SlotsFactory of(DoctorSchedule schedule) {
        return new SlotsFactory(
                schedule.getWorkingHours().getStartTime(),
                schedule.getAppointmentDurationInMins(),
                schedule.timeSlotsNumber()
        );
    }

    public Set<Slot> create() {
        Set<Slot> slots = new HashSet<>();
        LocalTime currentTime = startTime;
        for (int i = 0; i < numberOfSlots; i++) {
            slots.add(new Slot(currentTime));
            currentTime = currentTime.plusMinutes(durationInMins);
        }
        return slots;
    }
}
