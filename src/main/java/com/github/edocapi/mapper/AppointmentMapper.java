package com.github.edocapi.mapper;

import com.github.edocapi.config.MapperConfig;
import com.github.edocapi.dto.AppointmentDto;
import com.github.edocapi.dto.CreateAppointmentRequestDto;
import com.github.edocapi.model.Appointment;
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
