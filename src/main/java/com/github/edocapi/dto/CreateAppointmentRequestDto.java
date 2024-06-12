package com.github.edocapi.dto;

import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;
import java.time.LocalTime;
import lombok.Data;

@Data
public class CreateAppointmentRequestDto {
    @NotNull
    private LocalTime startTime;
    @NotNull
    private LocalDate date;
    @NotNull
    private boolean isOnline;
    @NotNull
    private Long doctorId;
}
