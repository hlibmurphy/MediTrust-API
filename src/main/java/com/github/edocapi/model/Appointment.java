package com.github.edocapi.model;

import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.time.LocalDate;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;

@Entity
@Setter
@Getter
@RequiredArgsConstructor
@SQLDelete(sql = "UPDATE appointments SET is_deleted = true WHERE id = ?")
@SQLRestriction(value = "is_deleted = false")
@Table(name = "appointments")
public class Appointment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Embedded
    @Column(nullable = false)
    private TimePeriod timePeriod = new TimePeriod();
    @Column(nullable = false)
    private LocalDate date;
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Status status = Status.SCHEDULED;
    @Column(nullable = false)
    private boolean isOnline = false;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(nullable = false)
    private Doctor doctor;

    @Column(nullable = false)
    private boolean isDeleted = false;

    public enum Status {
        SCHEDULED,
        COMPLETED,
        IN_PROGRESS,
        CANCELED
    }
}
