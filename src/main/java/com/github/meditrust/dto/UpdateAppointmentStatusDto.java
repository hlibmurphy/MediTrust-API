package com.github.meditrust.dto;

import com.github.meditrust.model.Appointment;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class UpdateAppointmentStatusDto {
    private Appointment.Status status;
}
