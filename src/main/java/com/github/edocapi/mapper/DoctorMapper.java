package com.github.edocapi.mapper;

import com.github.edocapi.config.MapperConfig;
import com.github.edocapi.dto.CreateDoctorRequestDto;
import com.github.edocapi.dto.DoctorDto;
import com.github.edocapi.dto.DoctorDtoWithoutScheduleId;
import com.github.edocapi.model.Doctor;
import java.util.List;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.data.domain.Page;

@Mapper(config = MapperConfig.class, uses = SpecialtyMapper.class)
public interface DoctorMapper {
    @Mapping(target = "scheduleId", source = "schedule.id")
    DoctorDto toDto(Doctor doctor);

    DoctorDtoWithoutScheduleId toDtoWithoutSchedule(Doctor doctor);

    List<DoctorDtoWithoutScheduleId> toDtosWithoutSchedule(Page<Doctor> doctors);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "reviews", ignore = true)
    @Mapping(target = "ratingSum", ignore = true)
    @Mapping(target = "averageRating", ignore = true)
    @Mapping(target = "specialties", ignore = true)
    Doctor toModel(CreateDoctorRequestDto doctorRequestDto);
}
