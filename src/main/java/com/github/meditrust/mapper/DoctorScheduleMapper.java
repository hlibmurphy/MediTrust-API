package com.github.meditrust.mapper;

import com.github.meditrust.config.MapperConfig;
import com.github.meditrust.dto.DoctorScheduleDto;
import com.github.meditrust.dto.UpdateScheduleRequestDto;
import com.github.meditrust.model.DoctorSchedule;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(config = MapperConfig.class)
public interface DoctorScheduleMapper {
    @Mapping(target = "id", ignore = true)
    void updateToModel(UpdateScheduleRequestDto updateScheduleRequestDto,
                       @MappingTarget DoctorSchedule doctorSchedule);

    DoctorScheduleDto toDto(DoctorSchedule schedule);

    @Mapping(target = "id", ignore = true)
    DoctorSchedule toModel(UpdateScheduleRequestDto scheduleRequestDto);
}
