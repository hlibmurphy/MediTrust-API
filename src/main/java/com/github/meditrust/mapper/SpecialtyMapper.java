package com.github.meditrust.mapper;

import com.github.meditrust.config.MapperConfig;
import com.github.meditrust.dto.CreateSpecialtyRequestDto;
import com.github.meditrust.dto.SpecialtyDto;
import com.github.meditrust.model.Specialty;
import java.util.List;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(config = MapperConfig.class)
public interface SpecialtyMapper {
    SpecialtyDto toDto(Specialty specialty);

    List<SpecialtyDto> toDtos(List<Specialty> specialties);

    @Mapping(target = "id", ignore = true)
    Specialty toModel(CreateSpecialtyRequestDto specialtyRequestDto);
}
