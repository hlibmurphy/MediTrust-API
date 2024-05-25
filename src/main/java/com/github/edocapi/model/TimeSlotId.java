package com.github.edocapi.model;

import jakarta.persistence.Embeddable;
import java.io.Serializable;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.Setter;

@Embeddable
@Getter
@Setter
class TimeSlotId implements Serializable {
    private Long doctorId;
    private LocalDateTime dateTime;
}
