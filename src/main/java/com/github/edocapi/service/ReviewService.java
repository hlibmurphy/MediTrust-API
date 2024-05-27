package com.github.edocapi.service;

import com.github.edocapi.dto.CreateReviewRequestDto;
import com.github.edocapi.dto.ReviewDto;
import java.util.List;
import org.springframework.data.domain.Pageable;

public interface ReviewService {
    List<ReviewDto> findAll(Pageable pageable);

    ReviewDto save(CreateReviewRequestDto reviewRequestDto);

    List<ReviewDto> findByDoctorId(Long doctorId, Pageable pageable);
}
