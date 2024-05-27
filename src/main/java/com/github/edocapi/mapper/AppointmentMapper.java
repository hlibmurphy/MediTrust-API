package com.github.edocapi.mapper;

import com.github.edocapi.config.MapperConfig;
import com.github.edocapi.dto.AppointmentDto;
import com.github.edocapi.dto.CreateAppointmentRequestDto;
import com.github.edocapi.model.Appointment;
import java.util.List;
import org.mapstruct.Mapper;

@Mapper(config = MapperConfig.class)
public interface AppointmentMapper {
    AppointmentDto toDto(Appointment appointment);

    List<AppointmentDto> toDtos(List<Appointment> appointments);

    Appointment toModel(CreateAppointmentRequestDto appointmentRequestDto);
}
