package com.github.edocapi.controller;

import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.edocapi.dto.AppointmentDto;
import com.github.edocapi.dto.CreateDoctorRequestDto;
import com.github.edocapi.dto.DoctorDto;
import com.github.edocapi.dto.DoctorDtoWithoutScheduleId;
import com.github.edocapi.dto.DoctorScheduleDto;
import com.github.edocapi.dto.ReviewDto;
import com.github.edocapi.dto.SpecialtyDto;
import com.github.edocapi.dto.UpdateScheduleRequestDto;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
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
    @DisplayName("Get all doctors")
    @Sql(scripts = {
            "classpath:db/doctor_schedules/add-schedule-to-doctor-schedules-table.sql",
            "classpath:db/doctors/add-doctor-to-doctors-table.sql"
    },
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = {
            "classpath:db/doctors/remove-doctor-from-doctors-table.sql",
            "classpath:db/doctor_schedules/remove-schedule-from-doctor-schedules-table.sql"
    },
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void getAllDoctors_withPageable_ShouldReturnAllDoctors() throws Exception {
        MvcResult result = mockMvc.perform(
                        get("/doctors"))
                .andExpect(status().isOk())
                .andReturn();

        DoctorDto[] doctors = objectMapper.readValue(result.getResponse().getContentAsString(),
                DoctorDto[].class);
        Assertions.assertEquals(1, doctors.length);
        Assertions.assertEquals("John", doctors[0].getFirstName());
        Assertions.assertEquals("Doe", doctors[0].getLastName());
    }

    @Test
    @DisplayName("Get reviews by doctor's ID")
    @Sql(scripts = {
            "classpath:db/doctor_schedules/add-schedule-to-doctor-schedules-table.sql",
            "classpath:db/doctors/add-doctor-to-doctors-table.sql",
            "classpath:db/reviews/add-review-to-reviews-table.sql"
    },
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = {
            "classpath:db/reviews/remove-review-from-reviews-table.sql",
            "classpath:db/doctors/remove-doctor-from-doctors-table.sql",
            "classpath:db/doctor_schedules/remove-schedule-from-doctor-schedules-table.sql"
    },
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void getReviewsByDoctorId_withValidDoctorIdAndPageable_shouldReturnReviewsByDoctorId()
            throws Exception {
        MvcResult result = mockMvc.perform(
                        get("/doctors/1/reviews?page=0"))
                .andExpect(status().isOk())
                .andReturn();
        ReviewDto[] reviews = objectMapper.readValue(result.getResponse().getContentAsString(),
                ReviewDto[].class);

        Assertions.assertEquals(1, reviews.length);
        Assertions.assertEquals(1L, reviews[0].getId());
        Assertions.assertEquals("Text", reviews[0].getText());
    }

    @Test
    @DisplayName("Get available time slots by doctor's ID")
    @Sql(scripts = {
            "classpath:db/doctor_schedules/add-schedule-to-doctor-schedules-table.sql",
            "classpath:db/doctors/add-doctor-to-doctors-table.sql"
    },
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = {
            "classpath:db/doctors/remove-doctor-from-doctors-table.sql",
            "classpath:db/doctor_schedules/remove-schedule-from-doctor-schedules-table.sql"
    },
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void getAvailableTimeSlotsByDoctorId_withValidDoctorIdAndDate_shouldReturnSlots()
            throws Exception {
        String date = LocalDate.now().toString();
        MvcResult result = mockMvc.perform(
                        get("/doctors/1/available-slots?date=" + date))
                .andExpect(status().isOk())
                .andReturn();
        Set<LocalTime> expected = Set.of(LocalTime.of(8, 0, 0));

        Set<LocalTime> actual = objectMapper.readValue(
                result.getResponse().getContentAsString(),
                new TypeReference<>() {
                });
        Assertions.assertEquals(expected, actual);
    }

    @Test
    @DisplayName("Get schedule by doctor's ID")
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    @Sql(scripts = {
            "classpath:db/doctor_schedules/add-schedule-to-doctor-schedules-table.sql",
            "classpath:db/doctors/add-doctor-to-doctors-table.sql"
    },
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = {
            "classpath:db/doctors/remove-doctor-from-doctors-table.sql",
            "classpath:db/doctor_schedules/remove-schedule-from-doctor-schedules-table.sql"
    },
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void getDoctorSchedule_withValidDoctorId_shouldReturnDoctorSchedule()
            throws Exception {
        MvcResult result = mockMvc.perform(
                        get("/doctors/1/schedule"))
                .andExpect(status().isOk())
                .andReturn();
        DoctorScheduleDto expected = createDoctorScheduleDto();

        DoctorScheduleDto actual = objectMapper.readValue(
                result.getResponse().getContentAsString(),
                DoctorScheduleDto.class);

        Assertions.assertEquals(expected, actual,
                "The retrieved doctor schedule should match the expected one");
    }

    @Test
    @DisplayName("Get appointments by doctor's ID")
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    @Sql(scripts = {
            "classpath:db/doctor_schedules/add-schedule-to-doctor-schedules-table.sql",
            "classpath:db/doctors/add-doctor-to-doctors-table.sql",
            "classpath:db/users/add-user-to-users-table.sql",
            "classpath:db/appointments/add-appointment-to-appointments-table.sql"
    },
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = {
            "classpath:db/appointments/remove-appointment-from-appointments-table.sql",
            "classpath:db/doctors/remove-doctor-from-doctors-table.sql",
            "classpath:db/doctor_schedules/remove-schedule-from-doctor-schedules-table.sql",
            "classpath:db/users/remove-user-from-users-table.sql"
    },
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void getAppointmentsByDoctorId_withValidDoctorId_shouldReturnAppointmentsByDoctorId()
            throws Exception {
        String date = LocalDate.now().toString();
        MvcResult result = mockMvc.perform(
                        get("/doctors/1/appointments?date=" + date))
                .andExpect(status().isOk())
                .andReturn();
        AppointmentDto[] expected = {createAppointmentDto()};

        AppointmentDto[] actual = objectMapper.readValue(
                result.getResponse().getContentAsString(), AppointmentDto[].class);
        Assertions.assertEquals(expected[0], actual[0]);
    }

    @Test
    @DisplayName("Create a new doctor")
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    @Sql(scripts = "classpath:db/specialties/add-dentist-to-specialties-table.sql",
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = {
            "classpath:db/specialties/remove-dentist-from-specialties-table.sql",
            "classpath:db/doctors/remove-doctor-from-doctors-table.sql",
            "classpath:db/doctor_schedules/remove-schedule-from-doctor-schedules-table.sql"
    },
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void createDoctor_withCreateDoctorRequestDto_shouldCreateDoctor() throws Exception {
        SpecialtyDto specialtyDto = createSpecialtyDto();
        CreateDoctorRequestDto doctorRequestDto = createDoctorRequestDto(specialtyDto);
        DoctorDto expected = createDoctorDto(doctorRequestDto, specialtyDto);
        String jsonRequest = objectMapper.writeValueAsString(doctorRequestDto);

        MvcResult result = mockMvc.perform(
                post("/doctors")
                        .content(jsonRequest)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andReturn();

        DoctorDto actual = objectMapper.readValue(result.getResponse().getContentAsString(),
                DoctorDto.class);
        Assertions.assertEquals(expected, actual);
    }

    @Test
    @DisplayName("Update doctor by ID")
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    @Sql(scripts = {
            "classpath:db/doctor_schedules/add-schedule-to-doctor-schedules-table.sql",
            "classpath:db/doctors/add-doctor-to-doctors-table.sql",
            "classpath:db/doctors_specialties/add-specialty-to-doctors-specialties-table.sql"
    },
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = {
            "classpath:db/doctors_specialties/remove-specialty-from-doctors-specialties-table.sql",
            "classpath:db/doctors/remove-doctor-from-doctors-table.sql",
            "classpath:db/doctor_schedules/remove-schedule-from-doctor-schedules-table.sql"
    },
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void updateDoctor_withUpdateDoctorRequestDto_shouldUpdateDoctor() throws Exception {
        SpecialtyDto specialtyDto = createSpecialtyDto();
        CreateDoctorRequestDto createDoctorRequestDto = createDoctorRequestDto("Bob",
                specialtyDto);
        MvcResult result = mockMvc.perform(
                        put("/doctors/1")
                                .content(objectMapper.writeValueAsString(createDoctorRequestDto))
                                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        DoctorDtoWithoutScheduleId expected = createDoctorDtoWithoutScheduleId(
                createDoctorRequestDto,
                specialtyDto);

        DoctorDtoWithoutScheduleId actual = objectMapper.readValue(
                result.getResponse().getContentAsString(), DoctorDtoWithoutScheduleId.class);
        Assertions.assertEquals(expected, actual,
                "The retrieved doctor should match the updated one");
    }

    @Test
    @DisplayName("Update schedule by doctor's ID")
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    @Sql(scripts = {
            "classpath:db/doctor_schedules/add-schedule-to-doctor-schedules-table.sql",
            "classpath:db/doctors/add-doctor-to-doctors-table.sql"
    },
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = {
            "classpath:db/doctors/remove-doctor-from-doctors-table.sql",
            "classpath:db/doctor_schedules/remove-schedule-from-doctor-schedules-table.sql"
    },
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void updateSchedule_withValidDoctorIdAndUpdateScheduleRequestDto_shouldUpdateSchedule()
            throws Exception {
        UpdateScheduleRequestDto updateScheduleRequestDto = createUpdateScheduleRequestDto();
        MvcResult result = mockMvc.perform(
                        put("/doctors/1/schedule")
                                .content(objectMapper.writeValueAsString(updateScheduleRequestDto))
                                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        DoctorScheduleDto expected = createDoctorScheduleDto(updateScheduleRequestDto);
        DoctorScheduleDto actual = objectMapper.readValue(result.getResponse().getContentAsString(),
                DoctorScheduleDto.class);

        Assertions.assertEquals(expected, actual,
                "The retrieved schedule DTO should match the updated one");
    }

    private UpdateScheduleRequestDto createUpdateScheduleRequestDto() {
        return new UpdateScheduleRequestDto()
                .setStartTime(LocalTime.of(9, 0, 0))
                .setEndTime(LocalTime.of(18, 0, 0))
                .setDayOffs(Set.of())
                .setLunchHours(Set.of())
                .setWorkingDays(Set.of(
                        DayOfWeek.MONDAY,
                        DayOfWeek.TUESDAY,
                        DayOfWeek.WEDNESDAY,
                        DayOfWeek.THURSDAY,
                        DayOfWeek.FRIDAY,
                        DayOfWeek.SATURDAY,
                        DayOfWeek.SUNDAY
                        )
                )
                .setAppointmentsDurationInMins(60);
    }

    private DoctorScheduleDto createDoctorScheduleDto(UpdateScheduleRequestDto
                                                              updateScheduleRequestDto) {
        DoctorScheduleDto doctorScheduleDto = new DoctorScheduleDto();
        doctorScheduleDto.setId(1L);
        doctorScheduleDto.setAppointmentsDurationInMins(
                updateScheduleRequestDto.getAppointmentsDurationInMins());
        doctorScheduleDto.setStartTime(updateScheduleRequestDto.getStartTime());
        doctorScheduleDto.setEndTime(updateScheduleRequestDto.getEndTime());
        doctorScheduleDto.setDayOffs(updateScheduleRequestDto.getDayOffs());
        doctorScheduleDto.setLunchHours(updateScheduleRequestDto.getLunchHours());
        doctorScheduleDto.setWorkingDays(updateScheduleRequestDto.getWorkingDays());
        return doctorScheduleDto;
    }

    private DoctorScheduleDto createDoctorScheduleDto() {
        DoctorScheduleDto doctorScheduleDto = new DoctorScheduleDto();
        doctorScheduleDto.setId(1L);
        doctorScheduleDto.setAppointmentsDurationInMins(60);
        doctorScheduleDto.setStartTime(LocalTime.of(8, 0, 0));
        doctorScheduleDto.setEndTime(LocalTime.of(9, 0, 0));
        doctorScheduleDto.setDayOffs(Set.of());
        doctorScheduleDto.setLunchHours(Set.of());
        doctorScheduleDto.setWorkingDays(Set.of(
                        DayOfWeek.MONDAY,
                        DayOfWeek.TUESDAY,
                        DayOfWeek.WEDNESDAY,
                        DayOfWeek.THURSDAY,
                        DayOfWeek.FRIDAY,
                        DayOfWeek.SATURDAY,
                        DayOfWeek.SUNDAY
                )
        );
        return doctorScheduleDto;
    }

    private AppointmentDto createAppointmentDto() {
        AppointmentDto appointmentDto = new AppointmentDto();
        appointmentDto.setId(1L);
        appointmentDto.setDoctorId(1L);
        appointmentDto.setUserId(1L);
        appointmentDto.setOnline(false);
        appointmentDto.setTime(LocalTime.of(9, 0, 0));
        appointmentDto.setDate(LocalDate.now());
        return appointmentDto;
    }

    private SpecialtyDto createSpecialtyDto() {
        return new SpecialtyDto()
                .setId(1L)
                .setName("Dentist");
    }

    private DoctorDto createDoctorDto(CreateDoctorRequestDto requestDto,
                                      SpecialtyDto specialtyDto) {
        DoctorScheduleDto doctorScheduleDto = new DoctorScheduleDto()
                .setId(1L);

        return new DoctorDto()
                .setId(1L)
                .setFirstName(requestDto.getFirstName())
                .setLastName(requestDto.getLastName())
                .setBackground(requestDto.getBackground())
                .setExperience(requestDto.getExperience())
                .setScheduleId(doctorScheduleDto.getId())
                .setAverageRating(0.0)
                .setSpecialties(Set.of(specialtyDto));
    }

    private DoctorDtoWithoutScheduleId createDoctorDtoWithoutScheduleId(
            CreateDoctorRequestDto requestDto,
            SpecialtyDto specialtyDto) {
        DoctorScheduleDto doctorScheduleDto = new DoctorScheduleDto()
                .setId(1L);

        return new DoctorDtoWithoutScheduleId()
                .setId(1L)
                .setFirstName(requestDto.getFirstName())
                .setLastName(requestDto.getLastName())
                .setBackground(requestDto.getBackground())
                .setExperience(requestDto.getExperience())
                .setAverageRating(0.0)
                .setSpecialties(Set.of(specialtyDto));
    }

    private CreateDoctorRequestDto createDoctorRequestDto(SpecialtyDto specialtyDto) {

        return new CreateDoctorRequestDto()
                .setFirstName("John")
                .setLastName("Doe")
                .setBackground("Background")
                .setPhone("0123456789")
                .setExperience(5)
                .setSpecialtyIds(Set.of(specialtyDto.getId()));
    }

    private CreateDoctorRequestDto createDoctorRequestDto(String firstName,
                                                          SpecialtyDto specialtyDto) {

        return new CreateDoctorRequestDto()
                .setFirstName(firstName)
                .setLastName("Doe")
                .setBackground("Background")
                .setPhone("0123456789")
                .setExperience(5)
                .setSpecialtyIds(Set.of(specialtyDto.getId()));
    }
}
