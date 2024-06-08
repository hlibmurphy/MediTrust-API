package com.github.edocapi.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.github.edocapi.model.Doctor;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.Rollback;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class DoctorRepositoryTest {
    @Autowired
    private DoctorRepository doctorRepository;

    @Autowired
    private EntityManager entityManager;

    @Test
    @Transactional
    @Rollback
    public void updateRating_withDoctorIdAndRatingAndNumberOfReviews_shouldUpdateRating() {
        Long doctorId = 1L;
        int newRating = 1;
        long numberOfReviews = 1;

        Doctor doctor = createDoctor(doctorId, 5, 5.0);
        Doctor savedDoctor = doctorRepository.saveAndFlush(doctor);
        Long savedDoctorId = savedDoctor.getId();

        entityManager.flush();
        entityManager.clear();

        doctorRepository.updateRating(savedDoctorId, newRating, numberOfReviews);

        entityManager.flush();
        entityManager.clear();

        Doctor updatedDoctor = doctorRepository.findById(savedDoctorId).orElseThrow(
                () -> new EntityNotFoundException("Doctor should be present")
        );

        int expectedRatingSum = 6;
        double expectedAverageRating = 3.0;

        assertEquals(expectedRatingSum, updatedDoctor.getRatingSum(),
                "Rating sum should be updated correctly");
        assertEquals(expectedAverageRating, updatedDoctor.getAverageRating(),
                0.001, "Average rating should be updated correctly");
    }

    private Doctor createDoctor(Long doctorId, int ratingSum, double averageRating) {
        Doctor doctor = new Doctor();
        doctor.setId(doctorId);
        doctor.setFirstName("John");
        doctor.setLastName("Smith");
        doctor.setRatingSum(ratingSum);
        doctor.setAverageRating(averageRating);
        return doctor;
    }
}
