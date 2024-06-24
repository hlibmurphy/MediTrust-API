package com.github.edocapi.dto;

import com.github.edocapi.annotation.ValidAppointmentTime;
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
