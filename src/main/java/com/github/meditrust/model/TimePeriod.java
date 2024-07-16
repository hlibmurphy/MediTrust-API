package com.github.meditrust.model;

import jakarta.persistence.Embeddable;
import java.time.LocalTime;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@EqualsAndHashCode
@ToString
@Embeddable
@AllArgsConstructor
@NoArgsConstructor
public class TimePeriod {
    private LocalTime startTime;
    private LocalTime endTime;

    public boolean contains(LocalTime time) {
        return !time.isBefore(startTime) && time.isBefore(endTime);
    }
}
