package com.github.meditrust.service.impl;

import com.github.meditrust.dto.DoctorScheduleDto;
import com.github.meditrust.dto.UpdateScheduleRequestDto;
import com.github.meditrust.mapper.DoctorScheduleMapper;
import com.github.meditrust.model.DoctorSchedule;
import com.github.meditrust.repository.DoctorRepository;
import com.github.meditrust.repository.DoctorScheduleRepository;
import com.github.meditrust.service.DoctorScheduleService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DoctorScheduleServiceImpl implements DoctorScheduleService {
    private final DoctorScheduleMapper doctorScheduleMapper;
    private final DoctorRepository doctorRepository;
    private final DoctorScheduleRepository doctorScheduleRepository;

    @Override
    public DoctorScheduleDto findByDoctorId(Long id) {
        DoctorSchedule schedule = doctorRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException("Failed to find doctor with id " + id))
                .getSchedule();
        return doctorScheduleMapper.toDto(schedule);
    }

    @Override
    public DoctorScheduleDto update(Long doctorId, UpdateScheduleRequestDto scheduleRequestDto) {
        DoctorSchedule schedule = doctorScheduleRepository.findByIdWithDetails(doctorId)
                .orElseThrow(() -> new EntityNotFoundException("Failed to find schedule with id "
                        + doctorId));

        doctorScheduleMapper.updateToModel(scheduleRequestDto, schedule);
        DoctorSchedule savedSchedule = doctorScheduleRepository.save(schedule);
        return doctorScheduleMapper.toDto(savedSchedule);
    }
}
