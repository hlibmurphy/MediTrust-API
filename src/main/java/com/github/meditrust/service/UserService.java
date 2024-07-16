package com.github.meditrust.service;

import com.github.meditrust.dto.UserResponseDto;

public interface UserService {
    UserResponseDto delete(Long userId);
}
