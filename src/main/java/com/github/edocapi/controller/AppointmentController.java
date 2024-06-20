package com.github.edocapi.controller;

import com.github.edocapi.dto.AppointmentDto;
import com.github.edocapi.model.User;
import com.github.edocapi.service.AppointmentService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/appointments")
@RequiredArgsConstructor
@Validated
public class AppointmentController {
    private final AppointmentService appointmentService;

    @GetMapping
    @PreAuthorize("hasRole('ROLE_USER')")
    public List<AppointmentDto> getAppointments(Authentication authentication) {
        User user = (User) authentication.getPrincipal();
        return appointmentService.get(user.getId());
    }
}
