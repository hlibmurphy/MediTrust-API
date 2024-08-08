package com.github.meditrust.controller;

import com.github.meditrust.dto.AppointmentDto;
import com.github.meditrust.dto.UpdateAppointmentStatusDto;
import com.github.meditrust.model.User;
import com.github.meditrust.service.AppointmentService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
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
    @Operation(description = "Get user's appointment")
    public List<AppointmentDto> getAppointments(Authentication authentication) {
        User user = (User) authentication.getPrincipal();
        return appointmentService.get(user.getId());
    }

    @PatchMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_USER')")
    @Operation(description = "Update user's own appointment status")
    public AppointmentDto updateAppointmentStatus(Authentication authentication,
                                            @PathVariable Long id,
                                            @Valid @RequestBody
                                            UpdateAppointmentStatusDto updateAppointmentStatusDto) {
        User user = (User) authentication.getPrincipal();
        return appointmentService.updateStatus(id, user.getId(), updateAppointmentStatusDto);
    }
}