package com.github.meditrust.service;

import com.github.meditrust.dto.DoctorScheduleDto;
import com.github.meditrust.dto.UpdateScheduleRequestDto;

public interface DoctorScheduleService {
    DoctorScheduleDto update(Long id, UpdateScheduleRequestDto scheduleRequestDto);

    DoctorScheduleDto findByDoctorId(Long id);
}
