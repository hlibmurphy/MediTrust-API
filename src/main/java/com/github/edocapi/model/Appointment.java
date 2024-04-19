package com.github.edocapi.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;

import java.time.LocalDate;
import java.time.LocalTime;

@Entity
@Setter
@Getter
@ToString
@RequiredArgsConstructor
@SQLDelete(sql = "UPDATE appointments SET is_deleted = true WHERE id = ?")
@SQLRestriction(value = "is_deleted = false")
@Table(name = "appointments")
public class Appointment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private LocalDate date;
    private LocalTime time;
    private boolean isOnline;
    private String address;

    @ManyToOne(fetch = FetchType.LAZY)
    private User patient;

    @ManyToOne(fetch = FetchType.LAZY)
    private Doctor doctor;

    @Column(nullable = false)
    private boolean isDeleted = false;
}
