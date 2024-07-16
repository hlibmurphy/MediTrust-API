package com.github.meditrust.service.impl;

import com.github.meditrust.dto.AvailableSlotsDto;
import com.github.meditrust.model.Appointment;
import com.github.meditrust.model.Doctor;
import com.github.meditrust.model.DoctorSchedule;
import com.github.meditrust.model.TimePeriod;
import com.github.meditrust.repository.AppointmentRepository;
import com.github.meditrust.repository.DoctorRepository;
import com.github.meditrust.service.TimeSlotService;
import com.github.meditrust.util.Slot;
import com.github.meditrust.util.SlotsFactory;
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
    public AvailableSlotsDto findAvailableSlots(Long doctorId, LocalDate date) {
        Doctor doctor = doctorRepository.findById(doctorId).orElseThrow(
                () -> new EntityNotFoundException("Failed to find doctor with id " + doctorId)
        );
        DoctorSchedule schedule = doctor.getSchedule();

        if (!schedule.isWorkingDay(date)) {
            return new AvailableSlotsDto(schedule.getAppointmentDurationInMins(), Set.of());
        }

        List<TimePeriod> periods = new ArrayList<>();
        periods.addAll(getAppointments(doctorId, date).stream()
                .map(Appointment::getTimePeriod)
                .toList());
        periods.addAll(schedule.getLunchHours());
        Set<LocalTime> availableSlots = SlotsFactory.of(schedule).create().stream()
                .filter(slot -> slot.isAvailable(periods))
                .map(Slot::getTime)
                .collect(Collectors.toSet());
        return new AvailableSlotsDto(schedule.getAppointmentDurationInMins(), availableSlots);
    }

    private List<Appointment> getAppointments(Long doctorId, LocalDate date) {
        return appointmentRepository.findByDoctorIdAndDate(doctorId, date);
    }
}
