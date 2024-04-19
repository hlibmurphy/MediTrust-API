package com.github.edocapi.controller;

import com.github.edocapi.dto.UserLoginRequestDto;
import com.github.edocapi.dto.UserLoginResponseDto;
import com.github.edocapi.dto.UserRegisterRequestDto;
import com.github.edocapi.dto.UserRegisterResponseDto;
import com.github.edocapi.service.AuthenticationService;
import lombok.AllArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
@RequestMapping("/auth")
public class AuthController {
    private final AuthenticationService authenticationService;

    @PostMapping("/register")
    public UserRegisterResponseDto register(@Validated @RequestBody UserRegisterRequestDto requestDto) {
        return authenticationService.register(requestDto);
    }

    @PostMapping("/login")
    public UserLoginResponseDto login(@RequestBody UserLoginRequestDto requestDto) {
        return authenticationService.login(requestDto);
    }
}