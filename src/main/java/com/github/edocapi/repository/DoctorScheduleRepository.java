package com.github.edocapi.repository;

import com.github.edocapi.model.DoctorSchedule;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface DoctorScheduleRepository extends JpaRepository<DoctorSchedule, Long> {
    @Query("SELECT ds FROM DoctorSchedule ds LEFT JOIN FETCH ds.workingDays "
            + "LEFT JOIN FETCH ds.dayOffs LEFT JOIN FETCH ds.lunchHours WHERE ds.id = :id")
    Optional<DoctorSchedule> findByIdWithDetails(@Param("id") Long id);
}
