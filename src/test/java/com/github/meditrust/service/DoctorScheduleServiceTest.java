package com.github.meditrust.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.github.meditrust.dto.DoctorScheduleDto;
import com.github.meditrust.dto.UpdateScheduleRequestDto;
import com.github.meditrust.mapper.DoctorScheduleMapper;
import com.github.meditrust.model.Doctor;
import com.github.meditrust.model.DoctorSchedule;
import com.github.meditrust.model.TimePeriod;
import com.github.meditrust.repository.DoctorRepository;
import com.github.meditrust.repository.DoctorScheduleRepository;
import com.github.meditrust.service.impl.DoctorScheduleServiceImpl;
import jakarta.persistence.EntityNotFoundException;
import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class DoctorScheduleServiceTest {
    @Mock
    private DoctorRepository doctorRepository;

    @Mock
    private DoctorScheduleMapper doctorScheduleMapper;

    @Mock
    private DoctorScheduleRepository doctorScheduleRepository;

    @InjectMocks
    private DoctorScheduleServiceImpl doctorScheduleService;

    @Test
    @DisplayName("Find schedule")
    public void findByDoctorId_withValidDoctorId_returnsDoctorSchedule() {
        DoctorSchedule schedule = createSchedule();
        Doctor doctor = createDoctor(schedule);

        when(doctorRepository.findById(anyLong())).thenReturn(Optional.of(doctor));
        DoctorScheduleDto expected = mapToDto(schedule);
        when(doctorScheduleMapper.toDto(any(DoctorSchedule.class))).thenReturn(expected);

        DoctorScheduleDto actual = doctorScheduleService.findByDoctorId(doctor.getId());
        assertEquals(expected, actual,
                "The retrieved doctor schedule DTO should be the same as the expected");
        verify(doctorRepository, times(1)).findById(anyLong());
        verify(doctorScheduleMapper, times(1))
                .toDto(any(DoctorSchedule.class));
    }

    @Test
    @DisplayName("Find schedule by doctor's ID with invalid ID")
    public void findByDoctorId_withInvalidDoctorId_returnsDoctorSchedule() {
        when(doctorRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class,
                () -> doctorScheduleService.findByDoctorId(1L));
        verify(doctorRepository, times(1)).findById(anyLong());
    }

    @Test
    @DisplayName("Update schedule")
    public void update_withValidIdAndUpdateScheduleRequestDto_shouldUpdateDoctorSchedule() {
        Long scheduleId = 1L;
        UpdateScheduleRequestDto updateRequestDto = createUpdateScheduleRequestDto();
        DoctorSchedule schedule = mapToModel(updateRequestDto);
        schedule.setId(scheduleId);
        when(doctorScheduleRepository.findByIdWithDetails(anyLong()))
                .thenReturn(Optional.of(schedule));
        when(doctorScheduleRepository.save(any(DoctorSchedule.class))).thenReturn(schedule);
        DoctorScheduleDto expected = mapToDto(schedule);
        when(doctorScheduleMapper.toDto(any(DoctorSchedule.class))).thenReturn(expected);

        DoctorScheduleDto actual = doctorScheduleService.update(schedule.getId(), updateRequestDto);
        assertEquals(expected, actual,
                "The retrieved doctor schedule DTO should be the same as the expected");
        verify(doctorScheduleRepository, times(1)).save(any(DoctorSchedule.class));
        verify(doctorScheduleMapper, times(1)).toDto(any(DoctorSchedule.class));
    }

    private DoctorSchedule mapToModel(UpdateScheduleRequestDto updateRequestDto) {
        DoctorSchedule schedule = new DoctorSchedule();
        schedule.getWorkingHours().setStartTime(updateRequestDto.getWorkingHours().getStartTime());
        schedule.getWorkingHours().setEndTime(updateRequestDto.getWorkingHours().getEndTime());
        schedule.setWorkingDays(updateRequestDto.getWorkingDays());
        schedule.setLunchHours(updateRequestDto.getLunchHours());
        schedule.setAppointmentDurationInMins(updateRequestDto.getAppointmentDurationInMins());
        schedule.setDayOffs(updateRequestDto.getDayOffs());
        return schedule;
    }

    private UpdateScheduleRequestDto createUpdateScheduleRequestDto() {
        return new UpdateScheduleRequestDto(
                20,
                Set.of(
                        DayOfWeek.MONDAY,
                        DayOfWeek.TUESDAY,
                        DayOfWeek.WEDNESDAY,
                        DayOfWeek.THURSDAY,
                        DayOfWeek.FRIDAY,
                        DayOfWeek.SATURDAY,
                        DayOfWeek.SUNDAY),
                Set.of(),
                new TimePeriod(LocalTime.of(8, 0, 0),
                        LocalTime.of(16, 0, 0)),
                Set.of()
        );
    }

    private DoctorScheduleDto mapToDto(DoctorSchedule schedule) {
        DoctorScheduleDto dto = new DoctorScheduleDto(
                schedule.getId(),
                schedule.getAppointmentDurationInMins(),
                schedule.getWorkingDays(),
                schedule.getDayOffs(),
                new TimePeriod(schedule.getWorkingHours().getStartTime(),
                        schedule.getWorkingHours().getEndTime()),
                Set.of()
        );
        return dto;
    }

    private Doctor createDoctor(DoctorSchedule doctorSchedule) {
        Doctor doctor = new Doctor();
        doctor.setId(1L);
        doctor.setSchedule(doctorSchedule);
        return doctor;
    }

    private DoctorSchedule createSchedule() {
        DoctorSchedule schedule = new DoctorSchedule();
        LocalTime startTime = LocalTime.of(8, 0);
        LocalTime endTime = LocalTime.of(16, 0);
        Set<TimePeriod> lunchHours = new HashSet<>();
        schedule.getWorkingHours().setStartTime(startTime);
        schedule.getWorkingHours().setEndTime(endTime);
        schedule.setLunchHours(lunchHours);
        schedule.setId(1L);
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
}
