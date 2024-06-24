package com.github.edocapi.model;

import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
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
    @Transient
    private static final int HOUR_IN_MINUTES = 60;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private int appointmentDurationInMins = 20;

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
    @Column(name = "day_off")
    private Set<LocalDate> dayOffs = new HashSet<>();
    @ElementCollection
    private Set<TimePeriod> lunchHours = new HashSet<>();
    @Column(nullable = false)
    @Embedded
    private TimePeriod workingHours = new TimePeriod(LocalTime.of(8, 0, 0),
            LocalTime.of(18, 0, 0));
    @Column(nullable = false)
    private boolean isDeleted = false;

    public int timeSlotsNumber() {
        return (workingHours.getEndTime().getHour() - workingHours.getStartTime().getHour())
                * (60 / appointmentDurationInMins);
    }

    public boolean isWorkingDay(LocalDate date) {
        return workingDays.contains(date.getDayOfWeek())
                && !dayOffs.contains(date);
    }
}
