package com.github.meditrust.controller;

import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.meditrust.dto.CreateSpecialtyRequestDto;
import com.github.meditrust.dto.DoctorDto;
import com.github.meditrust.dto.SpecialtyDto;
import java.math.BigDecimal;
import java.util.Set;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class SpecialtyControllerTest {
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
    @DisplayName("Get all specialties")
    @Sql(scripts = "classpath:db/specialties/add-specialty-to-specialties-table.sql",
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = "classpath:db/specialties/remove-specialty-from-specialties-table.sql",
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void getAllSpecialties_withNoArgs_shouldReturnAllSpecialties() throws Exception {
        MvcResult result = mockMvc.perform(
                        get("/specialties")
                )
                .andExpect(status().isOk())
                .andReturn();

        SpecialtyDto expected = createSpecialtyDto();
        SpecialtyDto[] actual = objectMapper.readValue(
                result.getResponse().getContentAsString(), SpecialtyDto[].class);
        Assertions.assertEquals(expected, actual[0],
                "The retrieved DTOs must match the expected ones.");
    }

    @Test
    @DisplayName("Create a specialty")
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    @Sql(scripts = "classpath:db/specialties/remove-specialty-from-specialties-table.sql",
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void createSpecialty_withCreateSpecialtyRequestDto_shouldCreateSpecialty()
            throws Exception {
        CreateSpecialtyRequestDto createSpecialtyRequestDto = createSpecialtyRequestDto();
        String jsonRequest = objectMapper.writeValueAsString(createSpecialtyRequestDto);
        MvcResult result = mockMvc.perform(
                        post("/specialties")
                                .content(jsonRequest)
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isCreated())
                .andReturn();

        SpecialtyDto expected = createSpecialtyDto(createSpecialtyRequestDto);
        SpecialtyDto actual = objectMapper.readValue(
                result.getResponse().getContentAsString(), SpecialtyDto.class);
        Assertions.assertEquals(expected, actual,
                "The retrieved DTOs must match the expected ones.");
    }

    @Test
    @Sql(scripts = {
            "classpath:db/specialties/add-specialty-to-specialties-table.sql",
            "classpath:db/doctor_schedules/add-schedule-to-doctor-schedules-table.sql",
            "classpath:db/doctors/add-doctor-to-doctors-table.sql",
            "classpath:db/doctors_specialties/add-specialty-to-doctors-specialties-table.sql"
    },
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = {
            "classpath:db/doctors_specialties/remove-specialty-from-doctors-specialties-table.sql",
            "classpath:db/doctors/remove-doctor-from-doctors-table.sql",
            "classpath:db/doctor_schedules/remove-schedule-from-doctor-schedules-table.sql",
            "classpath:db/specialties/remove-specialty-from-specialties-table.sql"
    },
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void getDoctorsBySpecialtyId_withValidSpecialtyId_shouldReturnDoctorsBySpecialtyId()
            throws Exception {
        MvcResult result = mockMvc.perform(
                        get("/specialties/1/doctors")
                )
                .andExpect(status().isOk())
                .andReturn();
        DoctorDto[] expected = {createDoctorDto()};
        DoctorDto[] actual =
                objectMapper.readValue(result.getResponse().getContentAsString(),
                        DoctorDto[].class);
        Assertions.assertEquals(expected[0], actual[0]);
    }

    private DoctorDto createDoctorDto() {
        DoctorDto doctorDto = new DoctorDto();
        doctorDto.setId(1L);
        doctorDto.setFirstName("John");
        doctorDto.setLastName("Doe");
        doctorDto.setAverageRating(0);
        SpecialtyDto specialtyDto = new SpecialtyDto();
        specialtyDto.setId(1L);
        specialtyDto.setName("Test Specialty");
        doctorDto.setSpecialties(Set.of(specialtyDto));
        doctorDto.setExperience(5);
        doctorDto.setScheduleId(1L);
        doctorDto.setBackground("background");
        doctorDto.setPrice(new BigDecimal("1000.00"));
        doctorDto.setServiceOffered("Service Offered");
        doctorDto.setAbout("About");
        return doctorDto;
    }

    private CreateSpecialtyRequestDto createSpecialtyRequestDto() {
        CreateSpecialtyRequestDto createSpecialtyRequestDto = new CreateSpecialtyRequestDto();
        createSpecialtyRequestDto.setName("Test Specialty");
        return createSpecialtyRequestDto;
    }

    private SpecialtyDto createSpecialtyDto(CreateSpecialtyRequestDto createSpecialtyRequestDto) {
        return new SpecialtyDto()
                .setName(createSpecialtyRequestDto.getName())
                .setId(1L);
    }

    private SpecialtyDto createSpecialtyDto() {
        return new SpecialtyDto()
                .setId(1L)
                .setName("Test Specialty");
    }
}
