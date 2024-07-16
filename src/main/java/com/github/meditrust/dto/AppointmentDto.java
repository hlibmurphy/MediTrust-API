package com.github.meditrust.dto;

import com.github.meditrust.model.Appointment;
import com.github.meditrust.model.TimePeriod;
import java.time.LocalDate;
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
    private TimePeriod timePeriod;
    private LocalDate date;
    private boolean isOnline;
    private Long doctorId;
    private Appointment.Status status;
}
