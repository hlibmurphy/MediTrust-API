package com.github.edocapi.controller;


import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.edocapi.dto.CreateDoctorRequestDto;
import com.github.edocapi.dto.DoctorDto;
import com.github.edocapi.dto.SpecialtyDto;
import java.util.Set;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class DoctorControllerTest {
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
    public void getAllDoctors_withPageable_ShouldReturnAllDoctors() {

    }

    @Test
    @DisplayName("Create a new doctor")
    // @WithMockUser(username = "admin", roles = {"ADMIN"})
    public void createDoctor_withCreateDoctorRequestDto_ShouldCreateDoctor() throws Exception {
        // Given
        SpecialtyDto specialtyDto = createSpecialtyDto();
        CreateDoctorRequestDto doctorRequestDto = createDoctorRequestDto(specialtyDto);
        DoctorDto expected = createDoctorDto(doctorRequestDto, specialtyDto);
        String jsonRequest = objectMapper.writeValueAsString(doctorRequestDto);

        // When
        MvcResult result = mockMvc.perform(
                post("/api/doctors")
                        .content(jsonRequest)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andReturn();

        // Then

    }

    private SpecialtyDto createSpecialtyDto() {
        SpecialtyDto specialtyDto = new SpecialtyDto()
                .setId(1L)
                .setName("Specialty");

        return specialtyDto;
    }

    private DoctorDto createDoctorDto(CreateDoctorRequestDto requestDto, SpecialtyDto specialtyDto) {
        DoctorDto doctorDto = new DoctorDto()
                .setId(1L)
                .setAverageRating(0.0)
                .setSpecialties(Set.of(specialtyDto));

        return doctorDto;
    }

    private CreateDoctorRequestDto createDoctorRequestDto(SpecialtyDto specialtyDto) {
        CreateDoctorRequestDto createDoctorRequestDto = new CreateDoctorRequestDto()
                .setFirstName("John")
                .setLastName("Doe")
                .setBackground("Background")
                .setPhone("0123456789")
                .setExperience(5)
                .setSpecialtyIds(Set.of(specialtyDto.getId()));

        return createDoctorRequestDto;
    }
}
