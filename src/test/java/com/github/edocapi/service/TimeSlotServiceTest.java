package com.github.edocapi.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.anyLong;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.github.edocapi.model.Appointment;
import com.github.edocapi.model.Doctor;
import com.github.edocapi.model.DoctorSchedule;
import com.github.edocapi.model.Specialty;
import com.github.edocapi.repository.AppointmentRepository;
import com.github.edocapi.repository.DoctorRepository;
import com.github.edocapi.service.impl.TimeSlotServiceImpl;
import jakarta.persistence.EntityNotFoundException;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class TimeSlotServiceTest {
    @Mock
    private DoctorRepository doctorRepository;
    @Mock
    private AppointmentRepository appointmentRepository;
    @InjectMocks
    private TimeSlotServiceImpl timeSlotService;

    @Test
    public void findAvailableSlots_withValidDoctorIdAndTodaysDate_shouldReturnAvailableSlots() {
        Set<LocalTime> lunchHours = Set.of(LocalTime.of(10, 0, 0));
        LocalTime startTime = LocalTime.of(8, 0);
        LocalTime endTime = LocalTime.of(16, 0);
        DoctorSchedule schedule = createSchedule(lunchHours, startTime, endTime, 60);
        Doctor doctor = createDoctor(schedule);
        LocalTime appointmentTime = LocalTime.of(9, 0, 0);

        when(doctorRepository.findById(anyLong())).thenReturn(Optional.of(doctor));
        when(appointmentRepository.findByDoctorIdAndDate(anyLong(), any(LocalDate.class)))
                .thenReturn(List.of(createAppointment(doctor, LocalDate.now(), appointmentTime)));

        int expectedAvailableSlots = 6;
        Set<LocalTime> actual = timeSlotService.findAvailableSlots(doctor.getId(), LocalDate.now());
        assertEquals(expectedAvailableSlots, actual.size());
        verify(doctorRepository, times(1)).findById(anyLong());
        verify(appointmentRepository, times(1))
                .findByDoctorIdAndDate(anyLong(), any(LocalDate.class));
    }

    @Test
    public void findAvailableSlots_withInvalidDoctorId_shouldThrowException() {
        when(doctorRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class,
                () -> timeSlotService.findAvailableSlots(1L, LocalDate.now()));
    }

    @Test
    public void findAvailableSlots_onDayOff_shouldReturnEmptySet() {
        DoctorSchedule schedule = createScheduleWithDayOffs(Set.of(LocalDate.now()));
        Doctor doctor = createDoctor(schedule);

        when(doctorRepository.findById(anyLong())).thenReturn(Optional.of(doctor));

        int expectedAvailableSlots = 0;
        Set<LocalTime> actual = timeSlotService.findAvailableSlots(doctor.getId(), LocalDate.now());
        assertEquals(expectedAvailableSlots, actual.size());
        verify(doctorRepository, times(1)).findById(anyLong());
    }

    @Test
    public void findAvailableSlots_onNonWorkingDay_shouldReturnEmptySet() {
        DoctorSchedule schedule = createScheduleWithEmptyWorkingDays();
        Doctor doctor = createDoctor(schedule);

        when(doctorRepository.findById(anyLong())).thenReturn(Optional.of(doctor));

        int expectedAvailableSlots = 0;
        Set<LocalTime> actual = timeSlotService.findAvailableSlots(doctor.getId(), LocalDate.now());
        assertEquals(expectedAvailableSlots, actual.size());
        verify(doctorRepository, times(1)).findById(anyLong());

    }

    private DoctorSchedule createScheduleWithEmptyWorkingDays() {
        DoctorSchedule schedule = new DoctorSchedule();
        schedule.setId(1L);
        schedule.setWorkingDays(Set.of());
        return schedule;
    }

    private DoctorSchedule createScheduleWithDayOffs(Set<LocalDate> dayOffs) {
        DoctorSchedule schedule = new DoctorSchedule();
        schedule.setId(1L);
        schedule.setDayOffs(dayOffs);
        schedule.setWorkingDays(Set.of(
                DayOfWeek.MONDAY,
                DayOfWeek.TUESDAY,
                DayOfWeek.WEDNESDAY,
                DayOfWeek.THURSDAY,
                DayOfWeek.FRIDAY,
                DayOfWeek.SATURDAY,
                DayOfWeek.SUNDAY
        ));

        return schedule;
    }

    private DoctorSchedule createSchedule(Set<LocalTime> lunchHours, LocalTime startTime,
                                          LocalTime endTime,
                                          int appointmentTime) {
        DoctorSchedule schedule = new DoctorSchedule();
        schedule.setStartTime(startTime);
        schedule.setEndTime(endTime);
        schedule.setLunchHours(lunchHours);
        schedule.setId(1L);
        schedule.setAppointmentsDurationInMins(appointmentTime);
        schedule.setWorkingDays(Set.of(
                DayOfWeek.MONDAY,
                DayOfWeek.TUESDAY,
                DayOfWeek.WEDNESDAY,
                DayOfWeek.THURSDAY,
                DayOfWeek.FRIDAY,
                DayOfWeek.SATURDAY,
                DayOfWeek.SUNDAY
        ));

        return schedule;
    }

    private Appointment createAppointment(Doctor doctor, LocalDate date, LocalTime time) {
        Appointment appointment = new Appointment();
        appointment.setDoctor(doctor);
        appointment.setDate(date);
        appointment.setTime(time);

        return appointment;
    }

    private Doctor createDoctor(DoctorSchedule schedule) {
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
        doctor.setSchedule(schedule);

        return doctor;
    }
}
