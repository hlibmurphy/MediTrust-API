package com.github.meditrust.mapper;

import com.github.meditrust.config.MapperConfig;
import com.github.meditrust.dto.UserRegisterRequestDto;
import com.github.meditrust.dto.UserResponseDto;
import com.github.meditrust.model.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(config = MapperConfig.class)
public interface UserMapper {
    UserResponseDto toDto(User user);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "roles", ignore = true)
    User toModel(UserRegisterRequestDto registerRequestDto);
}
