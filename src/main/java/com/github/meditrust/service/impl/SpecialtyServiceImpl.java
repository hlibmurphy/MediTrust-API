package com.github.meditrust.service.impl;

import com.github.meditrust.dto.CreateSpecialtyRequestDto;
import com.github.meditrust.dto.SpecialtyDto;
import com.github.meditrust.mapper.SpecialtyMapper;
import com.github.meditrust.model.Specialty;
import com.github.meditrust.repository.SpecialtyRepository;
import com.github.meditrust.service.SpecialtyService;
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
