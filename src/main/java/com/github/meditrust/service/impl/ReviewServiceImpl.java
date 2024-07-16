package com.github.meditrust.service.impl;

import com.github.meditrust.dto.CreateReviewRequestDto;
import com.github.meditrust.dto.ReviewDto;
import com.github.meditrust.mapper.ReviewMapper;
import com.github.meditrust.model.Review;
import com.github.meditrust.model.User;
import com.github.meditrust.repository.DoctorRepository;
import com.github.meditrust.repository.ReviewRepository;
import com.github.meditrust.service.ReviewService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ReviewServiceImpl implements ReviewService {
    private final ReviewRepository reviewRepository;
    private final ReviewMapper reviewMapper;
    private final DoctorRepository doctorRepository;

    @Override
    public List<ReviewDto> findAll(Pageable pageable) {
        Page<Review> reviews = reviewRepository.findAll(pageable);
        return reviewMapper.toDtos(reviews);
    }

    @Override
    @Transactional
    public ReviewDto save(CreateReviewRequestDto reviewRequestDto, User user) {
        Review review = reviewMapper.toModel(reviewRequestDto);
        review.setUser(user);
        Long doctorId = reviewRequestDto.getDoctorId();
        review.setDoctor(doctorRepository.findById(doctorId).orElseThrow(
                () -> new EntityNotFoundException("Doctor with id " + doctorId + " not found"))
        );

        long numberOfReviews = doctorRepository.countReviewsByDoctorId(doctorId);
        doctorRepository.updateRating(doctorId,
                reviewRequestDto.getRating(),
                numberOfReviews);

        Review savedReview = reviewRepository.save(review);
        return reviewMapper.toDto(savedReview);
    }

    @Override
    public List<ReviewDto> findByDoctorId(Long doctorId, Pageable pageable) {
        Page<Review> reviews = reviewRepository.findByDoctorId(doctorId, pageable);
        return reviewMapper.toDtos(reviews);
    }
}
