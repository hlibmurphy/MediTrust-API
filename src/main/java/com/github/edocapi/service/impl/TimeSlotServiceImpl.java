package com.github.edocapi.service.impl;

import com.github.edocapi.model.Appointment;
import com.github.edocapi.model.Doctor;
import com.github.edocapi.model.DoctorSchedule;
import com.github.edocapi.repository.AppointmentRepository;
import com.github.edocapi.repository.DoctorRepository;
import com.github.edocapi.service.TimeSlotService;
import jakarta.persistence.EntityNotFoundException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.LinkedHashSet;
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

        if (!schedule.getWorkingDays().contains(date.getDayOfWeek())
                || schedule.getDayOffs().contains(date)) {
            return Set.of();
        }

        List<Appointment> appointments = appointmentRepository
                .findByDoctorIdAndDate(doctorId, date);

        Set<LocalTime> availableTimes = new LinkedHashSet<>();
        Set<LocalTime> bookedHours = appointments.stream()
                .map(Appointment::getTime)
                .collect(Collectors.toSet());
        Set<LocalTime> lunchHours = schedule.getLunchHours();
        int workTime = (schedule.getEndTime().getHour() - schedule.getStartTime().getHour());
        int duration = schedule.getAppointmentsDurationInMins();
        int timeSlotsNumber = workTime * (HOUR_IN_MINUTES / duration);

        LocalTime slot = schedule.getStartTime();
        for (int i = 0; i < timeSlotsNumber; i++) {
            if (!bookedHours.contains(slot)
                    && !lunchHours.stream()
                            .map(LocalTime::getHour)
                            .collect(Collectors.toSet()).contains(slot.getHour())) {
                availableTimes.add(LocalTime.of(slot.getHour(), slot.getMinute()));
            }

            slot = slot.plusMinutes(duration);
        }

        return availableTimes;
    }
}
