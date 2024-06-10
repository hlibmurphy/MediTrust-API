package com.github.edocapi.model;

import java.time.LocalTime;
import java.util.HashSet;
import java.util.Set;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class SlotsFactory {
    private LocalTime time;
    private int durationInMins;
    private int numberOfSlots;

    public void update() {
        time = time.plusMinutes(durationInMins);
    }

    public static SlotsFactory of(DoctorSchedule schedule) {
        return new SlotsFactory(
                schedule.getStartTime(),
                schedule.getAppointmentsDurationInMins(),
                schedule.timeSlotsNumber()
        );
    }

    public Set<Slot> create() {
        Set<Slot> slots = new HashSet<>();
        for (int i = 0; i < getNumberOfSlots(); i++) {
            slots.add(new Slot(time));
            update();
        }

        return slots;
    }
}
