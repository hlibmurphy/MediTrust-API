package com.github.edocapi.service.impl;

import com.github.edocapi.dto.CreateDoctorRequestDto;
import com.github.edocapi.dto.DoctorDto;
import com.github.edocapi.dto.DoctorDtoWithoutScheduleId;
import com.github.edocapi.mapper.DoctorMapper;
import com.github.edocapi.model.Doctor;
import com.github.edocapi.model.DoctorSchedule;
import com.github.edocapi.repository.DoctorRepository;
import com.github.edocapi.repository.SpecialtyRepository;
import com.github.edocapi.service.DoctorService;
import jakarta.persistence.EntityNotFoundException;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DoctorServiceImpl implements DoctorService {

    private final DoctorRepository doctorRepository;
    private final DoctorMapper doctorMapper;
    private final SpecialtyRepository specialtyRepository;

    @Override
    public List<DoctorDtoWithoutScheduleId> findAll(Pageable pageable) {
        Page<Doctor> doctors = doctorRepository.findAll(pageable);
        return doctorMapper.toDtosWithoutSchedule(doctors);
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
        Doctor doctor = toModelWithSpecialty(doctorRequestDto);

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
        Doctor newDoctor = toModelWithSpecialty(doctorRequestDto);
        newDoctor.setId(doctorId);
        Doctor savedDoctor = doctorRepository.save(newDoctor);

        return doctorMapper.toDtoWithoutSchedule(savedDoctor);
    }

    private Doctor toModelWithSpecialty(CreateDoctorRequestDto doctorRequestDto) {
        Doctor doctor = doctorMapper.toModel(doctorRequestDto);
        doctor.setSpecialties(doctorRequestDto.specialtyIds().stream()
                .map(id -> specialtyRepository.findById(id)
                        .orElseThrow(
                                () -> new EntityNotFoundException(
                                        "No specialty with id " + id + " found")))
                .collect(Collectors.toSet()));
        return doctor;
    }
}
