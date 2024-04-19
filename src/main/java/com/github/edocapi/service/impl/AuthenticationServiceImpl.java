package com.github.edocapi.service.impl;

import com.github.edocapi.dto.UserLoginRequestDto;
import com.github.edocapi.dto.UserLoginResponseDto;
import com.github.edocapi.dto.UserRegisterRequestDto;
import com.github.edocapi.dto.UserRegisterResponseDto;
import com.github.edocapi.exception.RegistrationException;
import com.github.edocapi.mapper.UserMapper;
import com.github.edocapi.model.Role;
import com.github.edocapi.model.User;
import com.github.edocapi.repository.RoleRepository;
import com.github.edocapi.repository.UserRepository;
import com.github.edocapi.security.JwtUtil;
import com.github.edocapi.service.AuthenticationService;
import lombok.RequiredArgsConstructor;
import org.apache.catalina.Authenticator;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Set;

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
    public UserLoginResponseDto login(UserLoginRequestDto requestDto) {
        final Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(requestDto.getPhone(),
                        requestDto.getPassword())
        );

        String token = jwtUtil.generateToken(authentication.getName());
        return new UserLoginResponseDto(token);
    }

    @Override
    public UserRegisterResponseDto register(UserRegisterRequestDto requestDto) {
        if (userRepository.findByPhone(requestDto.getPhone()).isPresent()) {
            throw new RegistrationException("User with this phone already exists: "
                    + requestDto.getPhone());
        }

        User user = userMapper.toModel(requestDto);
        user.setPassword(passwordEncoder.encode(requestDto.getPassword()));

        Role userRole = roleRepository.findByName(Role.RoleName.ROLE_USER).orElseThrow(
                () -> new RuntimeException("Cannot find a role" + Role.RoleName.ROLE_USER));
        userRole.setName(Role.RoleName.ROLE_USER);
        user.setRoles(Set.of(userRole));

        User savedUser = userRepository.save(user);
        return userMapper.toDto(savedUser);
    }
}
