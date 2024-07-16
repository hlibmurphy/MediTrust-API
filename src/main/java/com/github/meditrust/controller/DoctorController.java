package com.github.meditrust.controller;

import com.github.meditrust.dto.AppointmentDto;
import com.github.meditrust.dto.AvailableSlotsDto;
import com.github.meditrust.dto.CreateAppointmentRequestDto;
import com.github.meditrust.dto.CreateDoctorRequestDto;
import com.github.meditrust.dto.DoctorDto;
import com.github.meditrust.dto.DoctorDtoWithoutScheduleId;
import com.github.meditrust.dto.DoctorScheduleDto;
import com.github.meditrust.dto.ReviewDto;
import com.github.meditrust.dto.UpdateScheduleRequestDto;
import com.github.meditrust.model.User;
import com.github.meditrust.service.AppointmentService;
import com.github.meditrust.service.DoctorScheduleService;
import com.github.meditrust.service.DoctorService;
import com.github.meditrust.service.ReviewService;
import com.github.meditrust.service.impl.TimeSlotServiceImpl;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import java.time.LocalDate;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/doctors")
@RequiredArgsConstructor
@Validated
public class DoctorController {
    private final DoctorService doctorService;
    private final ReviewService reviewService;
    private final DoctorScheduleService doctorScheduleService;
    private final TimeSlotServiceImpl timeSlotService;
    private final AppointmentService appointmentService;

    @GetMapping
    @Operation(description = "Get all doctors")
    public List<DoctorDtoWithoutScheduleId> getAllDoctors(Pageable pageable) {
        return doctorService.findAll(pageable);
    }

    @GetMapping("/{id}")
    @Operation(description = "Update user's own appointment status")
    public DoctorDtoWithoutScheduleId getDoctorById(@PathVariable @Positive Long id) {
        return doctorService.findById(id);
    }

    @GetMapping("/{id}/reviews")
    @Operation(description = "Get all reviews by doctor ID")
    public List<ReviewDto> getReviewsByDoctorId(@PathVariable @Positive Long id,
                                                Pageable pageable) {
        return reviewService.findByDoctorId(id, pageable);
    }

    @GetMapping("/{id}/available-slots")
    @Operation(description = "Get available time slots for doctor by ID")
    public AvailableSlotsDto getAvailableTimeSlotsByDoctorId(
            @PathVariable Long id,
            @RequestParam
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
            LocalDate date
    ) {
        return timeSlotService.findAvailableSlots(id, date);
    }

    @GetMapping("/{id}/appointments")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @Operation(description = "Get appointments by doctor's ID")
    public List<AppointmentDto> getAppointmentsByDoctorId(
            @PathVariable @Positive Long id,
            @RequestParam
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
            LocalDate date
    ) {
        return appointmentService.findAppointmentsByDoctorIdAndDate(id, date);
    }

    @GetMapping("/{id}/schedule")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @Operation(description = "Get schedule by doctor's ID")
    public DoctorScheduleDto getDoctorSchedule(@PathVariable @Positive Long id) {
        return doctorScheduleService.findByDoctorId(id);
    }

    @PostMapping
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(description = "Create a new doctor")
    public DoctorDto createDoctor(@Valid @RequestBody CreateDoctorRequestDto doctorRequestDto) {
        return doctorService.save(doctorRequestDto);
    }

    @PostMapping("/{id}/appointment")
    @PreAuthorize("hasRole('ROLE_USER')")
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(description = "Create a new appointment")
    public AppointmentDto createAppointment(
            @PathVariable @Positive Long id,
            Authentication authentication,
            @Valid @RequestBody CreateAppointmentRequestDto createAppointmentRequestDto) {
        User user = (User) authentication.getPrincipal();
        return appointmentService.save(id, user, createAppointmentRequestDto);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @Operation(description = "Update doctor")
    public DoctorDtoWithoutScheduleId updateDoctor(
            @PathVariable @Positive Long id,
            @Valid @RequestBody
            CreateDoctorRequestDto doctorRequestDto) {
        return doctorService.update(id, doctorRequestDto);
    }

    @PutMapping("/{id}/schedule")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @Operation(description = "Update doctor's schedule")
    public DoctorScheduleDto updateSchedule(
            @PathVariable @Positive Long id,
            @Valid @RequestBody UpdateScheduleRequestDto scheduleRequestDto) {
        return doctorScheduleService.update(id, scheduleRequestDto);
    }
}
