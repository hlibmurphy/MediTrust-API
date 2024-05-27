package com.github.edocapi.service.impl;

import com.github.edocapi.dto.DoctorScheduleDto;
import com.github.edocapi.dto.UpdateScheduleRequestDto;
import com.github.edocapi.mapper.DoctorScheduleMapper;
import com.github.edocapi.model.DoctorSchedule;
import com.github.edocapi.repository.DoctorRepository;
import com.github.edocapi.repository.DoctorScheduleRepository;
import com.github.edocapi.service.DoctorScheduleService;
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
    public DoctorScheduleDto update(Long id, UpdateScheduleRequestDto scheduleRequestDto) {
        DoctorSchedule schedule = doctorScheduleMapper.toModel(scheduleRequestDto);
        DoctorSchedule savedSchedule = doctorScheduleRepository.save(schedule);
        return doctorScheduleMapper.toDto(savedSchedule);
    }
}
