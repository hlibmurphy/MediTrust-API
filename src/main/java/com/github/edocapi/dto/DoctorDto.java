package com.github.edocapi.dto;

import java.math.BigDecimal;
import java.util.Set;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
public class DoctorDto {
    private Long id;
    private String firstName;
    private String lastName;
    private BigDecimal price;
    private Set<SpecialtyDto> specialties;
    private String background;
    private String about;
    private String serviceOffered;
    private Long scheduleId;
    private int experience;
    private double averageRating;
}
