package com.github.edocapi.mapper;

import com.github.edocapi.config.MapperConfig;
import com.github.edocapi.dto.RecallDto;
import com.github.edocapi.dto.RecallRequestDto;
import com.github.edocapi.model.Recall;
import org.mapstruct.Mapper;

@Mapper(config = MapperConfig.class, uses = SpecialtyMapper.class)
public interface RecallMapper {
    Recall toModel(RecallRequestDto recallRequestDto);

    RecallDto toDto(Recall recall);
}
