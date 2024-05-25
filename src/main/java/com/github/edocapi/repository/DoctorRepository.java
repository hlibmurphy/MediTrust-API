package com.github.edocapi.repository;

import com.github.edocapi.model.Doctor;
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
            + "d.averageRating = d.ratingSum / (:numberOfReviews + 1) "
            + "WHERE d.id = :doctorId")
    void updateRating(@Param("doctorId") Long doctorId,
                      @Param("newRating") int newRating,
                      @Param("numberOfReviews") long numberOfReviews);
}
