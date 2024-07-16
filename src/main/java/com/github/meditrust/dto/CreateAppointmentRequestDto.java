package com.github.meditrust.dto;

import com.github.meditrust.annotation.ValidAppointmentTime;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;
import java.time.LocalTime;
import lombok.Data;

@Data
@ValidAppointmentTime(startTime = "startTime", date = "date")
public class CreateAppointmentRequestDto {
    @NotNull
    private LocalTime startTime;
    @NotNull
    private LocalDate date;
    @NotNull
    private boolean isOnline;
}
