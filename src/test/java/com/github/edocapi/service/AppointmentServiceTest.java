package com.github.edocapi.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.github.edocapi.dto.AppointmentDto;
import com.github.edocapi.mapper.AppointmentMapper;
import com.github.edocapi.model.Appointment;
import com.github.edocapi.model.Doctor;
import com.github.edocapi.model.DoctorSchedule;
import com.github.edocapi.model.Specialty;
import com.github.edocapi.model.User;
import com.github.edocapi.repository.AppointmentRepository;
import com.github.edocapi.service.impl.AppointmentServiceImpl;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class AppointmentServiceTest {
    @Mock
    private AppointmentRepository appointmentRepository;

    @Mock
    private AppointmentMapper appointmentMapper;

    @InjectMocks
    private AppointmentServiceImpl appointmentService;

    @Test
    public void findAppointmentsByDoctorIdAndDate_withDoctorIdAndDate_returnsAppointments() {
        Doctor doctor = createDoctor();
        List<Appointment> appointments = new ArrayList<>();
        appointments.add(createAppointment(doctor));
        appointments.add(createAppointment(doctor));
        when(appointmentRepository.findByDoctorIdAndDate(anyLong(), any(LocalDate.class)))
                .thenReturn(appointments);
        List<AppointmentDto> expected = appointments.stream()
                .map(this::mapToDto)
                .toList();
        when(appointmentMapper.toDtos(appointments)).thenReturn(expected);

        List<AppointmentDto> actual =
                appointmentService.findAppointmentsByDoctorIdAndDate(doctor.getId(),
                        LocalDate.now());
        assertEquals(expected, actual,
                "The retrieved appointment list should be same as expected");
        verify(appointmentRepository, times(1))
                .findByDoctorIdAndDate(anyLong(),
                any(LocalDate.class));
        verify(appointmentMapper, times(1)).toDtos(appointments);
    }

    private AppointmentDto mapToDto(Appointment appointment) {
        return new AppointmentDto(appointment.getId(),
                appointment.getTime(),
                appointment.getDate(),
                appointment.isOnline(),
                1L,
                1L);
    }

    private Appointment createAppointment(Doctor doctor) {
        Appointment appointment = new Appointment();
        appointment.setId(1L);
        appointment.setDoctor(doctor);
        appointment.setDate(LocalDate.now());
        appointment.setTime(LocalTime.of(9, 0));
        appointment.setOnline(false);
        appointment.setPatient(new User());
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
