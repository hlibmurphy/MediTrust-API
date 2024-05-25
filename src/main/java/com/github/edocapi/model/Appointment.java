package com.github.edocapi.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
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
    @OneToOne(fetch = FetchType.EAGER)
    private TimeSlot timeSlot;
    private boolean isOnline;
    private String address;

    @ManyToOne(fetch = FetchType.EAGER)
    private User patient;

    @ManyToOne(fetch = FetchType.EAGER)
    private Doctor doctor;

    @Column(nullable = false)
    private boolean isDeleted = false;
}
