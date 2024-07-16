package com.github.meditrust.mapper;

import com.github.meditrust.config.MapperConfig;
import com.github.meditrust.dto.RecallDto;
import com.github.meditrust.dto.RecallRequestDto;
import com.github.meditrust.model.Recall;
import org.mapstruct.Mapper;

@Mapper(config = MapperConfig.class, uses = SpecialtyMapper.class)
public interface RecallMapper {
    Recall toModel(RecallRequestDto recallRequestDto);

    RecallDto toDto(Recall recall);
}
