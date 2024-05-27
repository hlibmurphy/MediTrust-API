package com.github.edocapi.service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Set;

public interface TimeSlotService {
    Set<LocalTime> findAvailableSlots(Long doctorId, LocalDate date);
}
