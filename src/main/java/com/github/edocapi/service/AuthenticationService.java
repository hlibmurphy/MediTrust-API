package com.github.edocapi.service;

import com.github.edocapi.dto.UserLoginRequestDto;
import com.github.edocapi.dto.UserLoginResponseDto;
import com.github.edocapi.dto.UserRegisterRequestDto;
import com.github.edocapi.dto.UserRegisterResponseDto;

public interface AuthenticationService {
    UserLoginResponseDto login(UserLoginRequestDto requestDto);

    UserRegisterResponseDto register(UserRegisterRequestDto requestDto);
}
