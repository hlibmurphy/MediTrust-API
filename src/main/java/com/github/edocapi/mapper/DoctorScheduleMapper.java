package com.github.edocapi.mapper;

import com.github.edocapi.config.MapperConfig;
import com.github.edocapi.dto.DoctorScheduleDto;
import com.github.edocapi.dto.UpdateScheduleRequestDto;
import com.github.edocapi.model.DoctorSchedule;
import org.mapstruct.Mapper;

@Mapper(config = MapperConfig.class)
public interface DoctorScheduleMapper {
    DoctorScheduleDto toDto(DoctorSchedule schedule);

    DoctorSchedule toModel(UpdateScheduleRequestDto scheduleRequestDto);
}
