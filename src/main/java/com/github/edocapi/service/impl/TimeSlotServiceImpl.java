package com.github.edocapi.service.impl;

import com.github.edocapi.model.Appointment;
import com.github.edocapi.model.Doctor;
import com.github.edocapi.model.DoctorSchedule;
import com.github.edocapi.model.TimePeriod;
import com.github.edocapi.repository.AppointmentRepository;
import com.github.edocapi.repository.DoctorRepository;
import com.github.edocapi.service.TimeSlotService;
import com.github.edocapi.util.Slot;
import com.github.edocapi.util.SlotsFactory;
import jakarta.persistence.EntityNotFoundException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TimeSlotServiceImpl implements TimeSlotService {
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

        List<TimePeriod> periods = new ArrayList<>();
        periods.addAll(getAppointments(doctorId, date).stream()
                .map(Appointment::getTimePeriod)
                .toList());
        periods.addAll(schedule.getLunchHours());
        return SlotsFactory.of(schedule).create().stream()
                .filter(slot -> slot.isAvailable(periods))
                .map(Slot::getTime)
                .collect(Collectors.toSet());
    }

    private List<Appointment> getAppointments(Long doctorId, LocalDate date) {
        return appointmentRepository.findByDoctorIdAndDate(doctorId, date);
    }
}
