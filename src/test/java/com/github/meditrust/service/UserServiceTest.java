package com.github.meditrust.service;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.github.meditrust.dto.UserResponseDto;
import com.github.meditrust.mapper.UserMapper;
import com.github.meditrust.model.Role;
import com.github.meditrust.model.User;
import com.github.meditrust.repository.UserRepository;
import com.github.meditrust.service.impl.UserServiceImpl;
import java.util.Optional;
import java.util.Set;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {
    @Mock
    private UserRepository userRepository;

    @Mock
    private UserMapper userMapper;

    @InjectMocks
    private UserServiceImpl userService;

    @Test
    public void delete_withUserId_shouldDeleteUser() {
        User user = createUser();
        UserResponseDto expected = mapToDto(user);
        when(userRepository.findById(anyLong()))
                .thenReturn(Optional.of(user));
        when(userMapper.toDto(any(User.class)))
                .thenReturn(expected);

        UserResponseDto actual = userService.delete(user.getId());

        verify(userRepository).deleteById(anyLong());
        Assertions.assertEquals(expected, actual,
                "The retrieved DTO should match the expected one");
    }

    private User createUser() {
        User user = new User();
        user.setId(1L);
        user.setFirstName("firstName");
        user.setLastName("lastName");
        user.setEmail("email");
        user.setPhone("123");
        user.setRoles(Set.of(new Role()));
        user.setPassword("password");
        return user;
    }

    private UserResponseDto mapToDto(User user) {
        UserResponseDto userResponseDto = new UserResponseDto();
        userResponseDto.setId(user.getId());
        userResponseDto.setFirstName(user.getFirstName());
        userResponseDto.setLastName(user.getLastName());
        userResponseDto.setEmail(user.getEmail());
        userResponseDto.setPhone(user.getPhone());
        userResponseDto.setRoles(user.getRoles());
        return userResponseDto;
    }
}
