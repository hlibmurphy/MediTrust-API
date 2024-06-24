package com.github.edocapi.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.edocapi.dto.UserLoginRequestDto;
import com.github.edocapi.dto.UserLoginResponseDto;
import com.github.edocapi.dto.UserRegisterRequestDto;
import com.github.edocapi.dto.UserResponseDto;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class AuthControllerTest {
    protected static MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper = new ObjectMapper();

    @BeforeAll
    static void beforeAll(@Autowired WebApplicationContext applicationContext) {
        mockMvc = MockMvcBuilders
                .webAppContextSetup(applicationContext)
                .apply(springSecurity())
                .build();
    }

    @Test
    @DisplayName("Register user")
    @Sql(scripts = "classpath:db/users/remove-user-from-users-table.sql",
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void register_withUserRegisterRequestDto_shouldReturnAndRegisterUser() throws Exception {
        UserRegisterRequestDto userRegisterRequestDto = createUserRegisterRequestDto();
        String jsonRequest = objectMapper.writeValueAsString(userRegisterRequestDto);

        MvcResult result = mockMvc.perform(
                        post("/auth/register")
                                .content(jsonRequest)
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isCreated())
                .andReturn();

        UserResponseDto expected = createUserResponseDto(userRegisterRequestDto);

        UserResponseDto actual = objectMapper.readValue(
                result.getResponse().getContentAsString(), UserResponseDto.class);
        assertEquals(expected.getPhone(), actual.getPhone());
        assertEquals(expected.getEmail(), actual.getEmail());
    }

    @Test
    @DisplayName("Login into account")
    @Sql(scripts = "classpath:db/users/add-user-to-users-table.sql",
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = "classpath:db/users/remove-user-from-users-table.sql",
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void login_withUserLoginRequestDto_shouldReturnToken() throws Exception {
        UserLoginRequestDto userLoginRequestDto = createUserLoginRequestDto();
        String jsonRequest = objectMapper.writeValueAsString(userLoginRequestDto);

        MvcResult result = mockMvc.perform(
                        post("/auth/login")
                                .content(jsonRequest)
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andReturn();

        UserLoginResponseDto actual = objectMapper.readValue(
                result.getResponse().getContentAsString(), UserLoginResponseDto.class);

        assertNotNull(actual.getToken());
    }

    private UserLoginRequestDto createUserLoginRequestDto() {
        UserLoginRequestDto userLoginRequestDto = new UserLoginRequestDto();
        userLoginRequestDto.setPhone("0111111111");
        userLoginRequestDto.setPassword("12345678");
        return userLoginRequestDto;
    }

    private UserResponseDto createUserResponseDto(UserRegisterRequestDto userRegisterRequestDto) {
        UserResponseDto userResponseDto = new UserResponseDto();
        userResponseDto.setId(1L);
        userResponseDto.setEmail(userRegisterRequestDto.getEmail());
        userResponseDto.setFirstName(userRegisterRequestDto.getFirstName());
        userResponseDto.setLastName(userRegisterRequestDto.getLastName());
        userResponseDto.setPhone(userRegisterRequestDto.getPhone());
        return userResponseDto;
    }

    private UserRegisterRequestDto createUserRegisterRequestDto() {
        UserRegisterRequestDto userRegisterRequestDto = new UserRegisterRequestDto();
        userRegisterRequestDto.setPhone("0211111111");
        userRegisterRequestDto.setEmail("test_email@test.com");
        userRegisterRequestDto.setFirstName("First Name");
        userRegisterRequestDto.setLastName("Last Name");
        userRegisterRequestDto.setPassword("password");
        userRegisterRequestDto.setRepeatPassword("password");
        return userRegisterRequestDto;
    }
}
