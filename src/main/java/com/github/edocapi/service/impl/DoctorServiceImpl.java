package com.github.edocapi.service.impl;

import com.github.edocapi.model.Doctor;
import com.github.edocapi.repository.DoctorRepository;
import com.github.edocapi.service.DoctorService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class DoctorServiceImpl implements DoctorService {

    private final DoctorRepository doctorRepository;

    @Override
    public List<Doctor> findAll() {
        return doctorRepository.findAll();
    }
}
