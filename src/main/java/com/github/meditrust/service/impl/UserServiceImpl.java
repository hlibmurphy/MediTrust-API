package com.github.meditrust.service.impl;

import com.github.meditrust.dto.UserResponseDto;
import com.github.meditrust.mapper.UserMapper;
import com.github.meditrust.model.User;
import com.github.meditrust.repository.UserRepository;
import com.github.meditrust.service.UserService;
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
