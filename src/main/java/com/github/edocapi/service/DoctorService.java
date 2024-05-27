package com.github.edocapi.service;

import com.github.edocapi.dto.CreateDoctorRequestDto;
import com.github.edocapi.dto.DoctorDto;
import com.github.edocapi.dto.DoctorDtoWithoutScheduleId;
import java.util.List;
import org.springframework.data.domain.Pageable;

public interface DoctorService {
    List<DoctorDtoWithoutScheduleId> findAll(Pageable pageable);

    DoctorDtoWithoutScheduleId findById(Long doctorId);

    DoctorDto save(CreateDoctorRequestDto doctorRequestDto);

    DoctorDtoWithoutScheduleId update(Long doctorId, CreateDoctorRequestDto doctorRequestDto);
}
