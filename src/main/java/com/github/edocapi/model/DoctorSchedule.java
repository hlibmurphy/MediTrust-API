package com.github.edocapi.model;

import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.HashSet;
import java.util.Set;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;

@Entity
@Setter
@Getter
@ToString
@SQLDelete(sql = "UPDATE doctor_schedules SET is_deleted = true WHERE id = ?")
@SQLRestriction(value = "is_deleted = false")
@Table(name = "doctor_schedules")
public class DoctorSchedule {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private int appointmentsDurationInMins = 20;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    @ElementCollection
    private Set<DayOfWeek> workingDays = Set.of(
            DayOfWeek.MONDAY,
            DayOfWeek.TUESDAY,
            DayOfWeek.WEDNESDAY,
            DayOfWeek.THURSDAY,
            DayOfWeek.FRIDAY
    );
    @ElementCollection
    private Set<LocalDate> dayOffs = new HashSet<>();
    @Column(nullable = false)
    private LocalTime startTime = LocalTime.of(8, 0, 0);
    @ElementCollection
    private Set<LocalTime> lunchHours = new HashSet<>();
    @Column(nullable = false)
    private LocalTime endTime = LocalTime.of(18, 0, 0);
    @Column(nullable = false)
    private boolean isDeleted = false;


    public int timeSlotsNumber() {
        return (endTime.getHour() - startTime.getHour()) * (60 / appointmentsDurationInMins);
    }

    public boolean isWorkingDay(LocalDate date) {
        return workingDays.contains(date.getDayOfWeek())
                && !dayOffs.contains(date);
    }
}
