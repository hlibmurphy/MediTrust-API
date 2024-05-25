package com.github.edocapi.mapper;

import com.github.edocapi.config.MapperConfig;
import com.github.edocapi.dto.CreateDoctorRequestDto;
import com.github.edocapi.dto.DoctorDto;
import com.github.edocapi.model.Doctor;
import java.util.List;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.data.domain.Page;

@Mapper(config = MapperConfig.class)
public interface DoctorMapper {
    DoctorDto toDto(Doctor doctor);

    List<DoctorDto> toDtos(Page<Doctor> doctors);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "reviews", ignore = true)
    @Mapping(target = "ratingSum", ignore = true)
    @Mapping(target = "averageRating", ignore = true)
    Doctor toModel(CreateDoctorRequestDto doctorRequestDto);
}
