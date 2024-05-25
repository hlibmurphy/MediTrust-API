package com.github.edocapi.service;

import com.github.edocapi.dto.CreateDoctorRequestDto;
import com.github.edocapi.dto.DoctorDto;
import java.util.List;
import org.springframework.data.domain.Pageable;

public interface DoctorService {
    List<DoctorDto> findAll(Pageable pageable);

    DoctorDto findById(Long doctorId);

    DoctorDto save(CreateDoctorRequestDto doctorRequestDto);

    DoctorDto update(Long doctorId, CreateDoctorRequestDto doctorRequestDto);
}
