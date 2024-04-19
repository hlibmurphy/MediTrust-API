package com.github.edocapi.mapper;

import com.github.edocapi.config.MapperConfig;
import com.github.edocapi.dto.UserRegisterRequestDto;
import com.github.edocapi.dto.UserRegisterResponseDto;
import com.github.edocapi.model.User;
import org.mapstruct.Mapper;

@Mapper(config = MapperConfig.class)
public interface UserMapper {
    UserRegisterResponseDto toDto(User user);
    User toModel(UserRegisterRequestDto registerRequestDto);
}
