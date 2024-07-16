package com.github.meditrust.service;

import com.github.meditrust.dto.CreateReviewRequestDto;
import com.github.meditrust.dto.ReviewDto;
import com.github.meditrust.model.User;
import java.util.List;
import org.springframework.data.domain.Pageable;

public interface ReviewService {
    List<ReviewDto> findAll(Pageable pageable);

    ReviewDto save(CreateReviewRequestDto reviewRequestDto, User user);

    List<ReviewDto> findByDoctorId(Long doctorId, Pageable pageable);
}
