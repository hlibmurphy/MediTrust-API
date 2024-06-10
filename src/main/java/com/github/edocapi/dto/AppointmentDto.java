package com.github.edocapi.dto;

import java.time.LocalDate;
import java.time.LocalTime;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
@AllArgsConstructor
@NoArgsConstructor
public class AppointmentDto {
    private Long id;
    private LocalTime startTime;
    private LocalTime endTime;
    private LocalDate date;
    private boolean isOnline;
    private Long doctorId;
    private Long userId;
}
