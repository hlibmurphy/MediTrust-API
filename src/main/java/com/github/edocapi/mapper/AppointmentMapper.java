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
    @Mapping(target = "userId", source = "patient.id")
    @Mapping(target = "startTime", source = "timePeriod.startTime")
    @Mapping(target = "endTime", source = "timePeriod.endTime")
    AppointmentDto toDto(Appointment appointment);

    List<AppointmentDto> toDtos(List<Appointment> appointments);

    @Mapping(target = "doctor", ignore = true)
    @Mapping(target = "patient", ignore = true)
    Appointment toModel(CreateAppointmentRequestDto appointmentRequestDto);
}
