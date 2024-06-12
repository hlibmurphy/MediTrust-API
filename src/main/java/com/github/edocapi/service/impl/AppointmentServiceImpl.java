package com.github.edocapi.service.impl;

import com.github.edocapi.dto.AppointmentDto;
import com.github.edocapi.dto.AvailableSlotsDto;
import com.github.edocapi.dto.CreateAppointmentRequestDto;
import com.github.edocapi.exception.AppointmentException;
import com.github.edocapi.mapper.AppointmentMapper;
import com.github.edocapi.model.Appointment;
import com.github.edocapi.model.TimePeriod;
import com.github.edocapi.model.User;
import com.github.edocapi.repository.AppointmentRepository;
import com.github.edocapi.repository.DoctorRepository;
import com.github.edocapi.repository.DoctorScheduleRepository;
import com.github.edocapi.repository.UserRepository;
import com.github.edocapi.service.AppointmentService;
import com.github.edocapi.service.TimeSlotService;
import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AppointmentServiceImpl implements AppointmentService {
    private final AppointmentRepository appointmentRepository;
    private final AppointmentMapper appointmentMapper;
    private final DoctorScheduleRepository doctorScheduleRepository;
    private final DoctorRepository doctorRepository;
    private final TimeSlotService timeSlotService;
    private final UserRepository userRepository;

    @Override
    public List<AppointmentDto> findAppointmentsByDoctorIdAndDate(Long doctorId, LocalDate date) {
        List<Appointment> appointments =
                appointmentRepository.findByDoctorIdAndDate(doctorId, date);
        appointments.sort(Comparator.comparing(appointment -> appointment.getTimePeriod()
                .getStartTime()));
        return appointmentMapper.toDtos(appointments);
    }

    @Override
    public AppointmentDto save(Long doctorId, User user,
                               CreateAppointmentRequestDto createAppointmentRequestDto) {
        AvailableSlotsDto availableSlots =
                timeSlotService.findAvailableSlots(doctorId, createAppointmentRequestDto.getDate());

        if (!availableSlots.getAvailableTimes()
                .contains(createAppointmentRequestDto.getStartTime())) {
            throw new AppointmentException("Time slot "
                    + createAppointmentRequestDto.getStartTime()
                    + " is not available");
        }

        Appointment appointment = appointmentMapper.toModel(createAppointmentRequestDto);
        appointment.setTimePeriod(new TimePeriod(
                createAppointmentRequestDto.getStartTime(),
                createAppointmentRequestDto.getStartTime()
                        .plusMinutes(availableSlots.getAppointmentDuration())));
        appointment.setUser(user);
        Appointment savedAppointment = appointmentRepository.save(appointment);
        return appointmentMapper.toDto(savedAppointment);
    }
}
