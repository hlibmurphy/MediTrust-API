package com.github.edocapi.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.github.edocapi.dto.DoctorScheduleDto;
import com.github.edocapi.dto.UpdateScheduleRequestDto;
import com.github.edocapi.mapper.DoctorScheduleMapper;
import com.github.edocapi.model.Doctor;
import com.github.edocapi.model.DoctorSchedule;
import com.github.edocapi.repository.DoctorRepository;
import com.github.edocapi.repository.DoctorScheduleRepository;
import com.github.edocapi.service.impl.DoctorScheduleServiceImpl;
import jakarta.persistence.EntityNotFoundException;
import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.Optional;
import java.util.Set;
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
    public void findByDoctorId_withInvalidDoctorId_returnsDoctorSchedule() {
        when(doctorRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class,
                () -> doctorScheduleService.findByDoctorId(1L));
        verify(doctorRepository, times(1)).findById(anyLong());
    }

    @Test
    public void update_withValidIdAndUpdateScheduleRequestDto_shouldUpdateDoctorSchedule() {
        Long scheduleId = 1L;
        UpdateScheduleRequestDto updateRequestDto = createUpdateScheduleRequestDto();
        DoctorSchedule schedule = mapToModel(updateRequestDto);
        when(doctorScheduleMapper.toModel(any(UpdateScheduleRequestDto.class)))
                .thenReturn(schedule);
        schedule.setId(scheduleId);
        when(doctorScheduleRepository.save(any(DoctorSchedule.class))).thenReturn(schedule);
        DoctorScheduleDto expected = mapToDto(schedule);
        when(doctorScheduleMapper.toDto(any(DoctorSchedule.class))).thenReturn(expected);

        DoctorScheduleDto actual = doctorScheduleService.update(schedule.getId(), updateRequestDto);
        assertEquals(expected, actual,
                "The retrieved doctor schedule DTO should be the same as the expected");
        verify(doctorScheduleMapper, times(1)).toModel(any(UpdateScheduleRequestDto.class));
        verify(doctorScheduleRepository, times(1)).save(any(DoctorSchedule.class));
        verify(doctorScheduleMapper, times(1)).toDto(any(DoctorSchedule.class));
    }

    private DoctorSchedule mapToModel(UpdateScheduleRequestDto updateRequestDto) {
        DoctorSchedule schedule = new DoctorSchedule();
        schedule.setStartTime(updateRequestDto.startTime());
        schedule.setEndTime(updateRequestDto.endTime());
        schedule.setWorkingDays(updateRequestDto.workingDays());
        schedule.setLunchHours(updateRequestDto.lunchHours());
        schedule.setAppointmentsDurationInMins(updateRequestDto.appointmentsDurationInMins());
        schedule.setDayOffs(updateRequestDto.dayOffs());
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
                LocalTime.of(8, 0, 0),
                Set.of(),
                LocalTime.of(16, 0, 0)
        );
    }

    private DoctorScheduleDto mapToDto(DoctorSchedule schedule) {
        DoctorScheduleDto dto = new DoctorScheduleDto(
                schedule.getId(),
                schedule.getAppointmentsDurationInMins(),
                schedule.getWorkingDays(),
                schedule.getDayOffs(),
                schedule.getStartTime(),
                schedule.getLunchHours(),
                schedule.getEndTime()
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
        Set<LocalTime> lunchHours = Set.of(LocalTime.of(23, 0));
        schedule.setStartTime(startTime);
        schedule.setEndTime(endTime);
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
