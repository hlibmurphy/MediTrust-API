package com.github.edocapi.dto;

import lombok.Data;

@Data
public class DoctorDto {
    private int id;
    private String firstName;
    private String lastName;
    private String specialty;
    private String background;
    private int experience;
    private double averageRating;
}
