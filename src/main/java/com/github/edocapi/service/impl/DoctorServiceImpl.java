package com.github.edocapi.service.impl;

import com.github.edocapi.dto.CreateDoctorRequestDto;
import com.github.edocapi.dto.DoctorDto;
import com.github.edocapi.dto.DoctorDtoWithoutScheduleId;
import com.github.edocapi.mapper.DoctorMapper;
import com.github.edocapi.model.Doctor;
import com.github.edocapi.model.DoctorSchedule;
import com.github.edocapi.repository.DoctorRepository;
import com.github.edocapi.service.DoctorService;
import jakarta.persistence.EntityNotFoundException;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DoctorServiceImpl implements DoctorService {

    private final DoctorRepository doctorRepository;
    private final DoctorMapper doctorMapper;

    @Override
    public List<DoctorDtoWithoutScheduleId> findAll(Pageable pageable) {
        Page<Doctor> doctors = doctorRepository.findAll(pageable);
        return doctorMapper.toDtos(doctors);
    }

    @Override
    public DoctorDtoWithoutScheduleId findById(Long doctorId) {
        Doctor doctor = doctorRepository.findById(doctorId).orElseThrow(
                () -> new EntityNotFoundException(
                        "Doctor with id " + doctorId + " not found"));
        return doctorMapper.toDtoWithoutSchedule(doctor);
    }

    @Override
    public DoctorDto save(CreateDoctorRequestDto doctorRequestDto) {
        Doctor doctor = doctorMapper.toModel(doctorRequestDto);

        DoctorSchedule doctorSchedule = new DoctorSchedule();
        doctor.setSchedule(doctorSchedule);
        Doctor savedDoctor = doctorRepository.save(doctor);

        return doctorMapper.toDto(savedDoctor);
    }

    @Override
    public DoctorDtoWithoutScheduleId update(Long doctorId,
                                             CreateDoctorRequestDto doctorRequestDto) {
        doctorRepository.findById(doctorId).orElseThrow(
                () -> new EntityNotFoundException(
                        "Doctor with id " + doctorId + " not found")
        );
        Doctor newDoctor = doctorMapper.toModel(doctorRequestDto);
        newDoctor.setId(doctorId);
        Doctor savedDoctor = doctorRepository.save(newDoctor);

        return doctorMapper.toDtoWithoutSchedule(savedDoctor);
    }
}
