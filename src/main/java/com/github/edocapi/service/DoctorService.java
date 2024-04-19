package com.github.edocapi.service;

import com.github.edocapi.model.Doctor;

import java.util.List;

public interface DoctorService {
    List<Doctor> findAll();
}
