package com.github.edocapi.dto;

import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;
import java.time.LocalTime;

public record CreateAppointmentRequestDto(
        @NotNull
        LocalDate date,
        @NotNull
        LocalTime time,
        boolean isOnline,
        String address) {

}
