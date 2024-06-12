package com.github.edocapi.service;

import com.github.edocapi.dto.AvailableSlotsDto;
import java.time.LocalDate;

public interface TimeSlotService {
    AvailableSlotsDto findAvailableSlots(Long doctorId, LocalDate date);
}
