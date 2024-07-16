package com.github.meditrust.service.impl;

import com.github.meditrust.dto.UserLoginRequestDto;
import com.github.meditrust.dto.UserLoginResponseDto;
import com.github.meditrust.dto.UserRegisterRequestDto;
import com.github.meditrust.dto.UserResponseDto;
import com.github.meditrust.exception.RegistrationException;
import com.github.meditrust.mapper.UserMapper;
import com.github.meditrust.model.Role;
import com.github.meditrust.model.User;
import com.github.meditrust.repository.RoleRepository;
import com.github.meditrust.repository.UserRepository;
import com.github.meditrust.service.AuthenticationService;
import com.github.meditrust.util.JwtUtil;
import jakarta.persistence.EntityNotFoundException;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthenticationServiceImpl implements AuthenticationService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;
    private final RoleRepository roleRepository;
    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;

    @Override
    public UserLoginResponseDto login(UserLoginRequestDto loginRequestDto) {
        loginRequestDto.setPhone(parsePhoneNumber(loginRequestDto.getPhone()));

        final Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequestDto.getPhone(),
                        loginRequestDto.getPassword())
        );

        String token = jwtUtil.generateToken(authentication.getName());
        return new UserLoginResponseDto(token);
    }

    @Override
    public UserResponseDto register(UserRegisterRequestDto registerRequestDto) {
        registerRequestDto.setPhone(parsePhoneNumber(registerRequestDto.getPhone()));

        if (userRepository.findByPhone(registerRequestDto.getPhone()).isPresent()) {
            throw new RegistrationException("User with this phone already exists: "
                    + registerRequestDto.getPhone());
        }

        User user = userMapper.toModel(registerRequestDto);
        user.setPassword(passwordEncoder.encode(registerRequestDto.getPassword()));

        Role userRole = roleRepository.findByName(Role.RoleName.ROLE_USER).orElseThrow(
                () -> new EntityNotFoundException("Cannot find a role " + Role.RoleName.ROLE_USER));
        userRole.setName(Role.RoleName.ROLE_USER);
        user.setRoles(Set.of(userRole));

        User savedUser = userRepository.save(user);
        return userMapper.toDto(savedUser);
    }

    private String parsePhoneNumber(String phone) {
        return phone.substring(phone.length() - 10);
    }
}
