package com.github.meditrust.mapper;

import com.github.meditrust.config.MapperConfig;
import com.github.meditrust.dto.AppointmentDto;
import com.github.meditrust.dto.CreateAppointmentRequestDto;
import com.github.meditrust.model.Appointment;
import java.util.List;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(config = MapperConfig.class)
public interface AppointmentMapper {
    @Mapping(target = "doctorId", source = "doctor.id")
    AppointmentDto toDto(Appointment appointment);

    List<AppointmentDto> toDtos(List<Appointment> appointments);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "doctor", ignore = true)
    @Mapping(target = "user", ignore = true)
    @Mapping(target = "timePeriod", ignore = true)
    @Mapping(target = "status", ignore = true)
    Appointment toModel(CreateAppointmentRequestDto appointmentRequestDto);
}
