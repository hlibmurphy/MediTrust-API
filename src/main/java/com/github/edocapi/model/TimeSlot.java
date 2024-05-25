package com.github.edocapi.model;

import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Table;
import jakarta.persistence.Version;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;

@Entity
@Getter
@Setter
@RequiredArgsConstructor
@SQLDelete(sql = "UPDATE time_slots SET is_deleted = true WHERE id = ?")
@SQLRestriction(value = "is_deleted = false")
@Table(name = "time_slots")
public class TimeSlot {
    @EmbeddedId
    private TimeSlotId id;

    @Column(nullable = false)
    private boolean isAvailable = true;

    @Enumerated(EnumType.STRING)
    private Status status = Status.OPEN;

    @Version
    private Integer version;

    @Column(nullable = false)
    private boolean isDeleted = false;

    public enum Status {
        OPEN,
        RESERVED
    }
}
