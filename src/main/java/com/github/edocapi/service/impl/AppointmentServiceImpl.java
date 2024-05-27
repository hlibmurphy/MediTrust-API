package com.github.edocapi.service.impl;

import com.github.edocapi.dto.AppointmentDto;
import com.github.edocapi.mapper.AppointmentMapper;
import com.github.edocapi.model.Appointment;
import com.github.edocapi.repository.AppointmentRepository;
import com.github.edocapi.service.AppointmentService;
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

    @Override
    public List<AppointmentDto> findAppointmentsByDoctorIdAndDate(Long doctorId, LocalDate date) {
        List<Appointment> appointments =
                appointmentRepository.findByDoctorIdAndDate(doctorId, date);
        appointments.sort(Comparator.comparing(Appointment::getTime));
        return appointmentMapper.toDtos(appointments);
    }
}
