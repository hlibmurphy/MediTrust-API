package com.github.edocapi.service;

import com.github.edocapi.dto.DoctorScheduleDto;
import com.github.edocapi.dto.UpdateScheduleRequestDto;

public interface DoctorScheduleService {
    DoctorScheduleDto update(Long id, UpdateScheduleRequestDto scheduleRequestDto);

    DoctorScheduleDto findByDoctorId(Long id);
}
