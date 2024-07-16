package com.github.meditrust.service;

import com.github.meditrust.dto.AppointmentDto;
import com.github.meditrust.dto.CreateAppointmentRequestDto;
import com.github.meditrust.dto.UpdateAppointmentStatusDto;
import com.github.meditrust.model.User;
import java.time.LocalDate;
import java.util.List;

public interface AppointmentService {
    List<AppointmentDto> findAppointmentsByDoctorIdAndDate(Long doctorId, LocalDate date);

    AppointmentDto save(Long doctorId, User user,
                        CreateAppointmentRequestDto createAppointmentRequestDto);

    List<AppointmentDto> get(Long id);

    AppointmentDto updateStatus(Long id, Long userId,
                                UpdateAppointmentStatusDto updateAppointmentStatusDto);
}
