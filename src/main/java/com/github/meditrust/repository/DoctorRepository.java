package com.github.meditrust.repository;

import com.github.meditrust.model.Doctor;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface DoctorRepository extends JpaRepository<Doctor, Long> {
    @Query("SELECT COUNT(r) FROM Review r WHERE r.doctor.id = :doctorId")
    long countReviewsByDoctorId(@Param("doctorId") Long doctorId);

    @Modifying
    @Query("UPDATE Doctor d "
            + "SET d.ratingSum = d.ratingSum + :newRating, "
            + "d.averageRating = (d.ratingSum + :newRating) / (:numberOfReviews + 1) "
            + "WHERE d.id = :doctorId")
    @Transactional
    void updateRating(@Param("doctorId") Long doctorId,
                      @Param("newRating") int newRating,
                      @Param("numberOfReviews") long numberOfReviews);

    @Query("SELECT d FROM Doctor d JOIN d.specialties s WHERE s.id = :id AND d.isDeleted = false")
    Page<Doctor> findDoctorsBySpecialtyId(@Param("id") Long id, Pageable pageable);
}
