package com.github.edocapi.controller;

import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.edocapi.dto.UserResponseDto;
import java.util.Set;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class UserControllerTest {
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
    @Sql(scripts = "classpath:db/users/add-user-with-id-500-to-users-table.sql",
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = "classpath:db/users/remove-user-with-id-500-from-users-table.sql",
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    public void delete_withAuthentication_shouldDeleteUser() throws Exception {
        MvcResult result = mockMvc.perform(
                delete("/users/500")
        ).andExpect(status().isOk()).andReturn();
        UserResponseDto actual = objectMapper
                .readValue(result.getResponse().getContentAsString(), UserResponseDto.class);
        UserResponseDto expected = createUser();

        Assertions.assertEquals(expected, actual,
                "The retrieved DTO should match the expected one");
    }

    private UserResponseDto createUser() {
        UserResponseDto user = new UserResponseDto();
        user.setId(500L);
        user.setFirstName("First Name");
        user.setLastName("Last Name");
        user.setEmail("test2_email@email.com");
        user.setPhone("0222222222");
        user.setRoles(Set.of());
        return user;
    }
}
