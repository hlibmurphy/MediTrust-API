package com.github.edocapi.service;

import com.github.edocapi.dto.UserResponseDto;

public interface UserService {
    UserResponseDto delete(Long userId);
}
