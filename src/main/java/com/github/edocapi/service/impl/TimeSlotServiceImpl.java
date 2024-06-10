package com.github.edocapi.service.impl;

import com.github.edocapi.model.Appointment;
import com.github.edocapi.model.Doctor;
import com.github.edocapi.model.DoctorSchedule;
import com.github.edocapi.model.Slot;
import com.github.edocapi.model.SlotsFactory;
import com.github.edocapi.repository.AppointmentRepository;
import com.github.edocapi.repository.DoctorRepository;
import com.github.edocapi.service.TimeSlotService;
import jakarta.persistence.EntityNotFoundException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TimeSlotServiceImpl implements TimeSlotService {
    private static final int HOUR_IN_MINUTES = 60;
    private final AppointmentRepository appointmentRepository;
    private final DoctorRepository doctorRepository;

    @Override
    public Set<LocalTime> findAvailableSlots(Long doctorId, LocalDate date) {
        Doctor doctor = doctorRepository.findById(doctorId).orElseThrow(
                () -> new EntityNotFoundException("Failed to find doctor with id " + doctorId)
        );

        DoctorSchedule schedule = doctor.getSchedule();
        if (!schedule.isWorkingDay(date)) {
            return Set.of();
        }

        Set<LocalTime> appointments = getAppointments(doctorId, date);
        return SlotsFactory.of(schedule).create().stream()
                .filter(slot -> slot.isAvailable(appointments, schedule.getLunchHours()))
                .map(Slot::getTime)
                .collect(Collectors.toSet());
    }

    private Set<LocalTime> getAppointments(Long doctorId, LocalDate date) {
        List<Appointment> appointments = appointmentRepository
                .findByDoctorIdAndDate(doctorId, date);

        return appointments.stream()
                .map(Appointment::getTime)
                .collect(Collectors.toSet());
    }
}
