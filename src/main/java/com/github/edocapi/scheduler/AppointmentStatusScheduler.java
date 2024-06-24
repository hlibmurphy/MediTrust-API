package com.github.edocapi.scheduler;

import com.github.edocapi.model.Appointment;
import com.github.edocapi.repository.AppointmentRepository;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import lombok.AllArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@AllArgsConstructor
public class AppointmentStatusScheduler {
    private AppointmentRepository appointmentRepository;

    @Scheduled(fixedRate = 60000)
    @Transactional
    public void updateScheduledAppointments() {
        List<Appointment> appointments = appointmentRepository
                .findAppointmentsByDateAndStatus(LocalDate.now(), Appointment.Status.SCHEDULED);

        for (Appointment appointment : appointments) {
            if (appointment.getTimePeriod().getStartTime().isBefore(LocalTime.now())
                    || appointment.getTimePeriod().getStartTime().equals(LocalTime.now())) {
                appointment.setStatus(Appointment.Status.IN_PROGRESS);
                appointmentRepository.save(appointment);
            }
        }
    }

    @Scheduled(fixedRate = 60000)
    @Transactional
    public void updateInProgressAppointments() {
        List<Appointment> appointments = appointmentRepository
                .findAppointmentsByDateAndStatus(LocalDate.now(), Appointment.Status.IN_PROGRESS);

        for (Appointment appointment : appointments) {
            if (appointment.getTimePeriod().getEndTime().isBefore(LocalTime.now())
                    || appointment.getTimePeriod().getEndTime().equals(LocalTime.now())) {
                appointment.setStatus(Appointment.Status.COMPLETED);
                appointmentRepository.save(appointment);
            }
        }
    }
}
