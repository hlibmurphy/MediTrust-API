package com.github.edocapi.dto;

import com.github.edocapi.model.Specialty;
import lombok.Data;

@Data
public class DoctorDtoWithoutScheduleId {
    private Long id;
    private String firstName;
    private String lastName;
    private Specialty specialty;
    private String background;
    private int experience;
    private double averageRating;
}
