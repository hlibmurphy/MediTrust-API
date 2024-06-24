package com.github.edocapi.util;

import com.github.edocapi.model.TimePeriod;
import java.time.LocalTime;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class Slot {
    private LocalTime time;

    public boolean isAvailable(List<TimePeriod> periods) {
        return periods.stream().noneMatch(period -> period.contains(time));
    }
}
