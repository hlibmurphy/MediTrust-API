package com.github.meditrust.service;

import com.github.meditrust.dto.CreateSpecialtyRequestDto;
import com.github.meditrust.dto.SpecialtyDto;
import java.util.List;

public interface SpecialtyService {
    List<SpecialtyDto> findAll();

    SpecialtyDto save(CreateSpecialtyRequestDto specialtyRequestDto);
}
