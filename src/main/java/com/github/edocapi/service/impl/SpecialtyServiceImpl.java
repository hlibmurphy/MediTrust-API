package com.github.edocapi.service.impl;

import com.github.edocapi.dto.CreateSpecialtyRequestDto;
import com.github.edocapi.dto.SpecialtyDto;
import com.github.edocapi.mapper.SpecialtyMapper;
import com.github.edocapi.model.Specialty;
import com.github.edocapi.repository.SpecialtyRepository;
import com.github.edocapi.service.SpecialtyService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SpecialtyServiceImpl implements SpecialtyService {
    private final SpecialtyRepository specialtyRepository;
    private final SpecialtyMapper specialtyMapper;

    @Override
    public List<SpecialtyDto> findAll() {
        List<Specialty> specialties = specialtyRepository.findAll();
        return specialtyMapper.toDtos(specialties);
    }

    @Override
    public SpecialtyDto save(CreateSpecialtyRequestDto specialtyRequestDto) {
        Specialty specialty = specialtyMapper.toModel(specialtyRequestDto);
        Specialty savedSpecialty = specialtyRepository.save(specialty);
        return specialtyMapper.toDto(savedSpecialty);
    }
}
