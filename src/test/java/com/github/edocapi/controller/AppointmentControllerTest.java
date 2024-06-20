package com.github.edocapi.controller;

import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.edocapi.dto.AppointmentDto;
import com.github.edocapi.model.Appointment;
import com.github.edocapi.model.Doctor;
import com.github.edocapi.model.TimePeriod;
import com.github.edocapi.model.User;
import java.time.LocalDate;
import java.time.LocalTime;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class AppointmentControllerTest {
    protected static MockMvc mockMvc;
    private static final LocalDate TOMORROW_DATE = LocalDate.now().plusDays(1);

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
    @DisplayName("Get user's appointments")
    @Sql(scripts = {
            "classpath:db/users/add-user-with-id-500-to-users-table.sql",
            "classpath:db/users_roles/add-specific-user-and-role-to-users-roles-table.sql",
            "classpath:db/doctor_schedules/add-schedule-to-doctor-schedules-table.sql",
            "classpath:db/doctors/add-doctor-to-doctors-table.sql",
            "classpath:db/appointments/add-appointment-with-specific-user-to-appointments-table.sql"
    }, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = {
            "classpath:db/appointments/"
                    + "remove-appointment-with-specific-user-from-appointments-table.sql",
            "classpath:db/doctors/remove-doctor-from-doctors-table.sql",
            "classpath:db/doctor_schedules/remove-schedule-from-doctor-schedules-table.sql",
            "classpath:db/users_roles/remove-specific-user-and-role-from-users-roles-table.sql",
            "classpath:db/users/remove-user-with-id-500-from-users-table.sql"
    }, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    @WithUserDetails("0222222222")
    public void getAppointments_withAuthentication_shouldReturnAppointments()
            throws Exception {
        User user = new User();
        Doctor doctor = createDoctor();
        AppointmentDto[] expected = {mapToDto(createAppointment(user, doctor,
                LocalTime.of(9, 0, 0),
                LocalTime.of(10, 0, 0)))};
        MvcResult result = mockMvc.perform(
                        get("/appointments")
                )
                .andExpect(status().isOk())
                .andReturn();
        AppointmentDto[] actual = objectMapper.readValue(
                result.getResponse().getContentAsString(), AppointmentDto[].class);

        Assertions.assertEquals(expected[0], actual[0],
                "The retrieved DTO should match the expected one");
    }

    private Doctor createDoctor() {
        Doctor doctor = new Doctor();
        doctor.setId(1L);
        return doctor;
    }

    private AppointmentDto mapToDto(Appointment appointment) {
        AppointmentDto appointmentDto = new AppointmentDto();
        appointmentDto.setDoctorId(appointment.getDoctor().getId());
        appointmentDto.setId(appointment.getId());
        appointmentDto.setStatus(appointment.getStatus());
        appointmentDto.setOnline(appointment.isOnline());
        appointmentDto.setTimePeriod(appointment.getTimePeriod());
        appointmentDto.setDate(appointment.getDate());
        return appointmentDto;
    }

    private Appointment createAppointment(User user, Doctor doctor,
                                          LocalTime startTime, LocalTime endTime) {
        Appointment appointment = new Appointment();
        appointment.setId(1L);
        TimePeriod timePeriod = new TimePeriod(startTime, endTime);
        appointment.setTimePeriod(timePeriod);
        appointment.setOnline(false);
        appointment.setDate(TOMORROW_DATE);
        appointment.setDoctor(doctor);
        appointment.setUser(user);
        appointment.setStatus(Appointment.Status.SCHEDULED);
        return appointment;
    }
}
