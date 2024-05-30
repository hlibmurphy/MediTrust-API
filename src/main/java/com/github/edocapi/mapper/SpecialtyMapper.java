package com.github.edocapi.mapper;

import com.github.edocapi.config.MapperConfig;
import com.github.edocapi.dto.CreateSpecialtyRequestDto;
import com.github.edocapi.dto.SpecialtyDto;
import com.github.edocapi.model.Specialty;
import java.util.List;
import org.mapstruct.Mapper;

@Mapper(config = MapperConfig.class)
public interface SpecialtyMapper {
    SpecialtyDto toDto(Specialty specialty);

    List<SpecialtyDto> toDtos(List<Specialty> specialties);

    Specialty toModel(CreateSpecialtyRequestDto specialtyRequestDto);
}
