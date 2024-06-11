package com.github.edocapi.service.impl;

import com.github.edocapi.dto.CreateReviewRequestDto;
import com.github.edocapi.dto.ReviewDto;
import com.github.edocapi.mapper.ReviewMapper;
import com.github.edocapi.model.Review;
import com.github.edocapi.model.User;
import com.github.edocapi.repository.DoctorRepository;
import com.github.edocapi.repository.ReviewRepository;
import com.github.edocapi.service.ReviewService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
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
        review.setAuthor(user);
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
