package com.github.edocapi.dto;

import com.github.edocapi.model.Role;
import java.util.Set;
import lombok.Data;

@Data
public class UserRegisterResponseDto {
    private String phone;
    private String email;
    private String firstName;
    private String lastName;
    private Set<Role> roles;
}
