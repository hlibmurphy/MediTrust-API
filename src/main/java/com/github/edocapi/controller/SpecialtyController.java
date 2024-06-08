package com.github.edocapi.controller;

import com.github.edocapi.dto.CreateSpecialtyRequestDto;
import com.github.edocapi.dto.SpecialtyDto;
import com.github.edocapi.service.SpecialtyService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/specialties")
@RequiredArgsConstructor
public class SpecialtyController {
    private final SpecialtyService specialtySevice;

    @GetMapping
    public List<SpecialtyDto> getAllSpecialties() {
        return specialtySevice.findAll();
    }

    @PostMapping
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @ResponseStatus(HttpStatus.CREATED)
    public SpecialtyDto createSpecialty(
            @RequestBody CreateSpecialtyRequestDto specialtyRequestDto) {
        return specialtySevice.save(specialtyRequestDto);
    }
}
