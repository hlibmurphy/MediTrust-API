package com.github.edocapi.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.anyInt;
import static org.mockito.Mockito.anyLong;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.github.edocapi.dto.CreateReviewRequestDto;
import com.github.edocapi.dto.DoctorDtoWithoutScheduleId;
import com.github.edocapi.dto.ReviewDto;
import com.github.edocapi.dto.SpecialtyDto;
import com.github.edocapi.mapper.ReviewMapper;
import com.github.edocapi.model.Doctor;
import com.github.edocapi.model.Review;
import com.github.edocapi.model.User;
import com.github.edocapi.repository.DoctorRepository;
import com.github.edocapi.repository.ReviewRepository;
import com.github.edocapi.service.impl.ReviewServiceImpl;
import jakarta.persistence.EntityNotFoundException;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

@ExtendWith(MockitoExtension.class)
public class ReviewServiceTest {

    @Mock
    private ReviewRepository reviewRepository;

    @Mock
    private ReviewMapper reviewMapper;

    @Mock
    private DoctorRepository doctorRepository;

    @InjectMocks
    private ReviewServiceImpl reviewService;

    @Test
    @DisplayName("Find all reviews")
    public void findAll_withPageable_shouldReturnAllReviews() {
        List<Review> reviews = List.of(createReview(1L));
        Page<Review> pagedReviews = new PageImpl<>(reviews);
        when(reviewRepository.findAll(Pageable.unpaged())).thenReturn(pagedReviews);
        List<ReviewDto> expected = reviews.stream()
                .map(this::mapToDto)
                .toList();
        when(reviewMapper.toDtos(any()))
                .thenReturn(expected);

        List<ReviewDto> actual = reviewService.findAll(Pageable.unpaged());

        assertEquals(expected, actual,
                "The retrieved review DTOs should match expected ones");
        verify(reviewRepository, times(1)).findAll(Pageable.unpaged());
        verify(reviewMapper, times(1)).toDtos(any());
    }

    @Test
    @DisplayName("Save a review")
    public void save_withCreateReviewRequestDto_shouldSaveReview() {
        Doctor doctor = createDoctor(1L);
        Review review = createReview(doctor.getId());

        when(reviewMapper.toModel(any(CreateReviewRequestDto.class))).thenReturn(review);
        when(doctorRepository.findById(anyLong())).thenReturn(Optional.of(doctor));
        when(reviewRepository.save(any(Review.class))).thenReturn(review);
        ReviewDto expected = mapToDto(review);
        when(reviewMapper.toDto(any(Review.class))).thenReturn(expected);
        User user = new User();
        user.setId(1L);

        CreateReviewRequestDto reviewRequestDto = createReviewRequestDto(doctor.getId());
        ReviewDto actual = reviewService.save(reviewRequestDto, user);

        assertEquals(expected, actual,
                "The retrieved Review DTO should match expected one");
        verify(reviewMapper, times(1)).toModel(reviewRequestDto);
        verify(doctorRepository, times(1)).findById(reviewRequestDto.getDoctorId());
        verify(doctorRepository, times(1)).updateRating(anyLong(), anyInt(), anyLong());
        verify(reviewRepository, times(1)).save(review);
        verify(reviewMapper, times(1)).toDto(review);
    }

    @Test
    @DisplayName("Save a review with invalid doctor ID")
    public void save_withInvalidDoctorId_shouldThrowEntityNotFoundException() {
        CreateReviewRequestDto reviewRequestDto = createReviewRequestDto(1L);
        Review review = createReview(reviewRequestDto.getDoctorId());
        when(reviewMapper.toModel(any(CreateReviewRequestDto.class))).thenReturn(review);
        when(doctorRepository.findById(anyLong())).thenReturn(Optional.empty());
        User user = new User();
        user.setId(1L);
        assertThrows(EntityNotFoundException.class,
                () -> reviewService.save(reviewRequestDto, user));

        verify(doctorRepository, times(1))
                .findById(reviewRequestDto.getDoctorId());
    }

    @Test
    @DisplayName("Find reviews by doctor's ID")
    public void findByDoctorId_withPageable_shouldReturnReviewsForDoctor() {
        List<Review> reviews = List.of(createReview(1L));
        Page<Review> pagedReviews = new PageImpl<>(reviews);

        when(reviewRepository.findByDoctorId(anyLong(), any(Pageable.class)))
                .thenReturn(pagedReviews);
        when(reviewMapper.toDtos(any())).thenReturn(reviews.stream().map(this::mapToDto).toList());

        List<ReviewDto> actual = reviewService.findByDoctorId(1L, Pageable.unpaged());

        assertEquals(reviews.size(), actual.size());
        verify(reviewRepository, times(1))
                .findByDoctorId(anyLong(), any(Pageable.class));
        verify(reviewMapper, times(1)).toDtos(any());
    }

    private Review createReview(Long doctorId) {
        Review review = new Review();
        review.setId(1L);
        review.setText("Great doctor");
        review.setRating(5);
        review.setDoctor(createDoctor(doctorId));
        return review;
    }

    private Doctor createDoctor(Long id) {
        Doctor doctor = new Doctor();
        doctor.setId(id);
        doctor.setFirstName("First Name");
        doctor.setLastName("Last Name");
        return doctor;
    }

    private ReviewDto mapToDto(Review review) {
        DoctorDtoWithoutScheduleId doctorDto = new DoctorDtoWithoutScheduleId(
                1L,
                "First Name",
                "Last Name",
                Set.of(
                        new SpecialtyDto(
                        1L,
                        "Specialty")
                ),
                "Background",
                5,
                5
        );
        return new ReviewDto(review.getId(),
                review.getText(),
                review.getRating(),
                doctorDto);
    }

    private CreateReviewRequestDto createReviewRequestDto(Long doctorId) {
        return new CreateReviewRequestDto("Great doctor", 5, doctorId);
    }
}
