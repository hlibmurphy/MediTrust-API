package com.github.meditrust.service.impl;

import com.github.meditrust.dto.AppointmentDto;
import com.github.meditrust.dto.AvailableSlotsDto;
import com.github.meditrust.dto.CreateAppointmentRequestDto;
import com.github.meditrust.dto.UpdateAppointmentStatusDto;
import com.github.meditrust.exception.AppointmentException;
import com.github.meditrust.mapper.AppointmentMapper;
import com.github.meditrust.model.Appointment;
import com.github.meditrust.model.TimePeriod;
import com.github.meditrust.model.User;
import com.github.meditrust.repository.AppointmentRepository;
import com.github.meditrust.repository.DoctorRepository;
import com.github.meditrust.repository.DoctorScheduleRepository;
import com.github.meditrust.repository.UserRepository;
import com.github.meditrust.service.AppointmentService;
import com.github.meditrust.service.TimeSlotService;
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
                    + " for date "
                    + createAppointmentRequestDto.getDate()
                    + " is not available");
        }

        Appointment appointment = appointmentMapper.toModel(createAppointmentRequestDto);
        appointment.setTimePeriod(new TimePeriod(
                createAppointmentRequestDto.getStartTime(),
                createAppointmentRequestDto.getStartTime()
                        .plusMinutes(availableSlots.getAppointmentDuration())));
        appointment.setUser(user);
        appointment.setDoctor(doctorRepository.findById(doctorId).get());
        Appointment savedAppointment = appointmentRepository.save(appointment);
        return appointmentMapper.toDto(savedAppointment);
    }

    @Override
    public List<AppointmentDto> get(Long userId) {
        List<Appointment> appointments = appointmentRepository
                .findAppointmentsByUserId(userId);
        return appointmentMapper.toDtos(appointments);
    }

    @Override
    public AppointmentDto updateStatus(Long appointmentId, Long userId,
                                       UpdateAppointmentStatusDto updateAppointmentStatusDto) {
        Appointment appointment = appointmentRepository.findByIdAndUserId(appointmentId, userId)
                .orElseThrow(() -> new AppointmentException("Appointment with id "
                        + appointmentId + " and user id " + userId + " not found"));
        appointment.setStatus(updateAppointmentStatusDto.getStatus());
        Appointment savedAppointment = appointmentRepository.save(appointment);
        return appointmentMapper.toDto(savedAppointment);
    }
}
