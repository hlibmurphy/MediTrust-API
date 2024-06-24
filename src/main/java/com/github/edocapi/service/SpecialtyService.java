package com.github.edocapi.service;

import com.github.edocapi.dto.CreateSpecialtyRequestDto;
import com.github.edocapi.dto.SpecialtyDto;
import java.util.List;

public interface SpecialtyService {
    List<SpecialtyDto> findAll();

    SpecialtyDto save(CreateSpecialtyRequestDto specialtyRequestDto);
}
