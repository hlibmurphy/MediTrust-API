package com.github.meditrust.mapper;

import com.github.meditrust.config.MapperConfig;
import com.github.meditrust.dto.CreateReviewRequestDto;
import com.github.meditrust.dto.ReviewDto;
import com.github.meditrust.model.Review;
import java.util.List;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.data.domain.Page;

@Mapper(config = MapperConfig.class)
public interface ReviewMapper {
    ReviewDto toDto(Review review);

    List<ReviewDto> toDtos(Page<Review> reviews);

    @Mapping(target = "doctor", ignore = true)
    @Mapping(target = "user", ignore = true)
    @Mapping(target = "id", ignore = true)
    Review toModel(CreateReviewRequestDto createReviewRequestDto);
}
