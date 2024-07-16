package com.github.meditrust.service;

import com.github.meditrust.dto.CreateDoctorRequestDto;
import com.github.meditrust.dto.DoctorDto;
import com.github.meditrust.dto.DoctorDtoWithoutScheduleId;
import java.util.List;
import org.springframework.data.domain.Pageable;

public interface DoctorService {
    List<DoctorDtoWithoutScheduleId> findAll(Pageable pageable);

    DoctorDtoWithoutScheduleId findById(Long doctorId);

    DoctorDto save(CreateDoctorRequestDto doctorRequestDto);

    DoctorDtoWithoutScheduleId update(Long doctorId, CreateDoctorRequestDto doctorRequestDto);

    List<DoctorDto> getDoctorsBySpecialtyId(Long specialtyId, Pageable pageable);
}
