package com.github.meditrust.service;

import com.github.meditrust.dto.AvailableSlotsDto;
import java.time.LocalDate;

public interface TimeSlotService {
    AvailableSlotsDto findAvailableSlots(Long doctorId, LocalDate date);
}
