package com.github.edocapi.repository;

import com.github.edocapi.model.Appointment;
import java.time.LocalDate;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AppointmentRepository extends JpaRepository<Appointment, Long> {
    List<Appointment> findByDoctorIdAndDate(Long doctorId, LocalDate date);

    List<Appointment> findAppointmentsByDateAndStatus(LocalDate date, Appointment.Status status);

    List<Appointment> findAppointmentsByUserId(Long userId);
}
