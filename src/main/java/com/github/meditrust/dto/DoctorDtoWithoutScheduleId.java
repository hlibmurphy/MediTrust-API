package com.github.meditrust.dto;

import java.util.Set;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

@Accessors(chain = true)
@AllArgsConstructor
@NoArgsConstructor
@Data
public class DoctorDtoWithoutScheduleId {
    private Long id;
    private String firstName;
    private String lastName;
    private Set<SpecialtyDto> specialties;
    private String background;
    private int experience;
    private double averageRating;
}
