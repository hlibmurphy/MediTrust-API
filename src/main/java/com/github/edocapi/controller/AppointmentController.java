package com.github.edocapi.controller;

import com.github.edocapi.dto.AppointmentDto;
import com.github.edocapi.service.AppointmentService;
import java.time.LocalDate;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/appointments")
@RequiredArgsConstructor
public class AppointmentController {
    private final AppointmentService appointmentService;

    @GetMapping
    public List<AppointmentDto> getAppointments(@RequestParam Long doctorId,
                                               @RequestParam
                                               @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
                                               LocalDate date) {
        return appointmentService.findAppointmentsByDoctorIdAndDate(doctorId, date);
    }
}
