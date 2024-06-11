package com.github.edocapi.mapper;

import com.github.edocapi.config.MapperConfig;
import com.github.edocapi.dto.DoctorScheduleDto;
import com.github.edocapi.dto.UpdateScheduleRequestDto;
import com.github.edocapi.model.DoctorSchedule;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(config = MapperConfig.class)
public interface DoctorScheduleMapper {
    void updateToModel(UpdateScheduleRequestDto updateScheduleRequestDto,
                       @MappingTarget DoctorSchedule doctorSchedule);

    DoctorScheduleDto toDto(DoctorSchedule schedule);

    DoctorSchedule toModel(UpdateScheduleRequestDto scheduleRequestDto);
}
