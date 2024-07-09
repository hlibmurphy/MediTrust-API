package com.github.edocapi.controller;

import com.github.edocapi.dto.UserLoginRequestDto;
import com.github.edocapi.dto.UserLoginResponseDto;
import com.github.edocapi.dto.UserRegisterRequestDto;
import com.github.edocapi.dto.UserResponseDto;
import com.github.edocapi.service.AuthenticationService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
@RequestMapping("/auth")
public class AuthController {
    private final AuthenticationService authenticationService;

    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(description = "Register user")
    public UserResponseDto register(
            @Validated @RequestBody UserRegisterRequestDto requestDto) {
        return authenticationService.register(requestDto);
    }

    @PostMapping("/login")
    @Operation(description = "Login user")
    public UserLoginResponseDto login(@Valid @RequestBody UserLoginRequestDto requestDto) {
        return authenticationService.login(requestDto);
    }
}
