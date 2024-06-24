package com.github.edocapi.dto;

import com.github.edocapi.model.Appointment;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class UpdateAppointmentStatusDto {
    private Appointment.Status status;
}
