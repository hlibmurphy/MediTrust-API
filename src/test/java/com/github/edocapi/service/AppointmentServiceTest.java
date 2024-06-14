package com.github.edocapi.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.github.edocapi.dto.AppointmentDto;
import com.github.edocapi.dto.AvailableSlotsDto;
import com.github.edocapi.dto.CreateAppointmentRequestDto;
import com.github.edocapi.exception.AppointmentException;
import com.github.edocapi.mapper.AppointmentMapper;
import com.github.edocapi.model.Appointment;
import com.github.edocapi.model.Doctor;
import com.github.edocapi.model.DoctorSchedule;
import com.github.edocapi.model.Role;
import com.github.edocapi.model.Specialty;
import com.github.edocapi.model.TimePeriod;
import com.github.edocapi.model.User;
import com.github.edocapi.repository.AppointmentRepository;
import com.github.edocapi.repository.DoctorRepository;
import com.github.edocapi.service.impl.AppointmentServiceImpl;
import com.github.edocapi.service.impl.TimeSlotServiceImpl;
import jakarta.persistence.EntityNotFoundException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class AppointmentServiceTest {
    private static final LocalDate TOMORROW_DATE = LocalDate.now().plusDays(1);
    @Mock
    private AppointmentRepository appointmentRepository;

    @Mock
    private AppointmentMapper appointmentMapper;

    @Mock
    private TimeSlotServiceImpl timeSlotService;

    @Mock
    private DoctorRepository doctorRepository;

    @InjectMocks
    private AppointmentServiceImpl appointmentService;

    @Test
    @DisplayName("Find appointment")
    public void findAppointmentsByDoctorIdAndDate_withDoctorIdAndDate_returnsAppointments() {
        Doctor doctor = createDoctor();
        List<Appointment> appointments = new ArrayList<>();
        appointments.add(createAppointment(doctor, LocalTime.of(8, 0, 0),
                LocalTime.of(9, 0, 0)));
        appointments.add(createAppointment(doctor, LocalTime.of(9, 0, 0),
                LocalTime.of(10, 0, 0)));
        when(appointmentRepository.findByDoctorIdAndDate(anyLong(), any(LocalDate.class)))
                .thenReturn(appointments);
        List<AppointmentDto> expected = appointments.stream()
                .map(this::mapToDto)
                .toList();
        when(appointmentMapper.toDtos(appointments)).thenReturn(expected);

        List<AppointmentDto> actual =
                appointmentService.findAppointmentsByDoctorIdAndDate(doctor.getId(),
                        TOMORROW_DATE);
        assertEquals(expected, actual,
                "The retrieved appointment list should be same as expected");
        verify(appointmentRepository, times(1))
                .findByDoctorIdAndDate(anyLong(),
                any(LocalDate.class));
        verify(appointmentMapper, times(1)).toDtos(appointments);
    }

    @Test
    @DisplayName("Save appointment")
    public void save_withDoctorIdAndUserAndRequestDto_shouldReturnAppointment() {
        AvailableSlotsDto availableSlotsDto = createAvailableSlotsDto();
        when(timeSlotService.findAvailableSlots(anyLong(), any(LocalDate.class)))
                .thenReturn(availableSlotsDto);
        Doctor doctor = createDoctor();
        Appointment appointment = createAppointment(doctor, LocalTime.of(9, 0, 0),
                LocalTime.of(10, 0, 0));
        when(appointmentMapper.toModel(any(CreateAppointmentRequestDto.class)))
                .thenReturn(appointment);
        when(doctorRepository.findById(anyLong())).thenReturn(Optional.of(doctor));
        when(appointmentRepository.save(any(Appointment.class))).thenReturn(appointment);
        AppointmentDto expected = mapToDto(appointment);
        when(appointmentMapper.toDto(appointment)).thenReturn(expected);

        User user = createUser();
        CreateAppointmentRequestDto appointmentRequestDto = createAppointmentRequestDto(
                LocalTime.of(8, 0, 0)
        );
        AppointmentDto actual = appointmentService.save(doctor.getId(), user,
                appointmentRequestDto);
        Assertions.assertEquals(expected, actual,
                "The saved appointment should be same as expected");
    }

    @Test
    @DisplayName("Save appointment with unavailable time")
    public void save_withValidDoctorIdAndUnavailableTime_shouldThrowException() {
        AvailableSlotsDto availableSlotsDto = createAvailableSlotsDto();
        when(timeSlotService.findAvailableSlots(anyLong(), any(LocalDate.class)))
                .thenReturn(availableSlotsDto);

        Assertions.assertThrows(AppointmentException.class, () -> {
            User user = createUser();
            CreateAppointmentRequestDto appointmentRequestDto = createAppointmentRequestDto(
                    LocalTime.of(13, 0, 0));
            appointmentService.save(1L, user,
                    appointmentRequestDto);
        });
    }

    @Test
    @DisplayName("Save appointment with invalid doctor ID")
    public void save_withInvalidDoctorIdAndUserAndRequestDto_shouldThrowException() {
        when(timeSlotService.findAvailableSlots(anyLong(), any(LocalDate.class)))
                .thenThrow(EntityNotFoundException.class);

        Assertions.assertThrows(EntityNotFoundException.class, () -> {
            User user = createUser();
            CreateAppointmentRequestDto appointmentRequestDto = createAppointmentRequestDto(
                    LocalTime.of(8, 0, 0));
            appointmentService.save(1L, user,
                    appointmentRequestDto);
        });
    }

    private User createUser() {
        User user = new User();
        user.setId(1L);
        Role role = new Role();
        role.setName(Role.RoleName.ROLE_USER);
        role.setId(1L);
        user.setRoles(Set.of());
        user.setPassword("password");
        user.setPhone("0111111111");
        return user;
    }

    private CreateAppointmentRequestDto createAppointmentRequestDto(LocalTime startTime) {
        CreateAppointmentRequestDto requestDto = new CreateAppointmentRequestDto();
        requestDto.setStartTime(startTime);
        requestDto.setOnline(false);
        requestDto.setDate(TOMORROW_DATE);

        return requestDto;
    }

    private AvailableSlotsDto createAvailableSlotsDto() {
        return new AvailableSlotsDto().setAvailableTimes(Set.of(
                        LocalTime.of(8, 0, 0),
                        LocalTime.of(9, 0, 0),
                        LocalTime.of(10, 0, 0),
                        LocalTime.of(11, 0, 0)
                )).setAppointmentDuration(60);
    }

    private AppointmentDto mapToDto(Appointment appointment) {
        return new AppointmentDto(appointment.getId(),
                appointment.getTimePeriod(),
                appointment.getDate(),
                appointment.isOnline(),
                1L
        );
    }

    private Appointment createAppointment(Doctor doctor, LocalTime startTime, LocalTime endTime) {
        Appointment appointment = new Appointment();
        appointment.setId(1L);
        appointment.setDoctor(doctor);
        appointment.setDate(TOMORROW_DATE);
        TimePeriod timePeriod = new TimePeriod(startTime, endTime);
        appointment.setTimePeriod(timePeriod);
        appointment.setOnline(false);
        appointment.setUser(new User());
        return appointment;
    }

    private Doctor createDoctor() {
        Doctor doctor = new Doctor();
        doctor.setId(1L);
        doctor.setFirstName("First Name");
        doctor.setLastName("Last Name");
        doctor.setBackground("background");
        doctor.setExperience(5);
        doctor.setAverageRating(5);
        Specialty specialty = new Specialty();
        specialty.setId(1L);
        doctor.setSpecialties(Set.of(specialty));
        DoctorSchedule schedule = new DoctorSchedule();
        schedule.setId(1L);
        doctor.setSchedule(schedule);

        return doctor;
    }
}
