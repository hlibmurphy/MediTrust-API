package com.github.edocapi.mapper;

import com.github.edocapi.config.MapperConfig;
import com.github.edocapi.dto.CreateReviewRequestDto;
import com.github.edocapi.dto.ReviewDto;
import com.github.edocapi.model.Review;
import java.util.List;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.data.domain.Page;

@Mapper(config = MapperConfig.class)
public interface ReviewMapper {
    ReviewDto toDto(Review review);

    List<ReviewDto> toDtos(Page<Review> reviews);

    @Mapping(target = "doctor", ignore = true)
    Review toModel(CreateReviewRequestDto createReviewRequestDto);
}
