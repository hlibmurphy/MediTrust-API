package com.github.edocapi.service.impl;

import com.github.edocapi.dto.UserResponseDto;
import com.github.edocapi.mapper.UserMapper;
import com.github.edocapi.model.User;
import com.github.edocapi.repository.UserRepository;
import com.github.edocapi.service.UserService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;

    @Override
    public UserResponseDto delete(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException(
                        "User with id " + userId + " not found"));
        userRepository.deleteById(userId);

        return userMapper.toDto(user);
    }
}
