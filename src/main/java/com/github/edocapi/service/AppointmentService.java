package com.github.edocapi.service;

import com.github.edocapi.dto.AppointmentDto;
import com.github.edocapi.dto.CreateAppointmentRequestDto;
import com.github.edocapi.model.User;
import java.time.LocalDate;
import java.util.List;

public interface AppointmentService {
    List<AppointmentDto> findAppointmentsByDoctorIdAndDate(Long doctorId, LocalDate date);

    AppointmentDto save(Long doctorId, User user,
                        CreateAppointmentRequestDto createAppointmentRequestDto);
}
