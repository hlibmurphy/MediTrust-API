package com.github.edocapi.service;

import com.github.edocapi.dto.AppointmentDto;
import java.time.LocalDate;
import java.util.List;

public interface AppointmentService {
    List<AppointmentDto> findAppointmentsByDoctorIdAndDate(Long doctorId, LocalDate date);
}
