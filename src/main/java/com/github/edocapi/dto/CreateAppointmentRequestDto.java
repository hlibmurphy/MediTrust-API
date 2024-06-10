package com.github.edocapi.dto;

import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;
import java.time.LocalTime;
import org.springframework.boot.context.properties.bind.DefaultValue;

public record CreateAppointmentRequestDto(
        @NotNull
        LocalTime startTime,
        @NotNull
        LocalTime endTime,
        @NotNull
        LocalDate date,
        @DefaultValue(value = "false")
        boolean isOnline,
        @NotNull
        Long patientId,
        @NotNull
        Long doctorId
) {

}
