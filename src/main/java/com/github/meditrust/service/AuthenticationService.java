package com.github.meditrust.service;

import com.github.meditrust.dto.UserLoginRequestDto;
import com.github.meditrust.dto.UserLoginResponseDto;
import com.github.meditrust.dto.UserRegisterRequestDto;
import com.github.meditrust.dto.UserResponseDto;

public interface AuthenticationService {
    UserLoginResponseDto login(UserLoginRequestDto requestDto);

    UserResponseDto register(UserRegisterRequestDto requestDto);
}
