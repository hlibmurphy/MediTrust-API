package com.github.edocapi.controller;

import com.github.edocapi.dto.CreateSpecialtyRequestDto;
import com.github.edocapi.dto.DoctorDto;
import com.github.edocapi.dto.SpecialtyDto;
import com.github.edocapi.service.DoctorService;
import com.github.edocapi.service.SpecialtyService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/specialties")
@RequiredArgsConstructor
@Validated
public class SpecialtyController {
    private final SpecialtyService specialtyService;
    private final DoctorService doctorService;

    @GetMapping
    public List<SpecialtyDto> getAllSpecialties() {
        return specialtyService.findAll();
    }

    @PostMapping
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(description = "Create a new doctor specialty")
    public SpecialtyDto createSpecialty(
            @Valid @RequestBody CreateSpecialtyRequestDto specialtyRequestDto) {
        return specialtyService.save(specialtyRequestDto);
    }

    @GetMapping("/{id}/doctors")
    @Operation(description = "Get doctor by specialty ID")
    public List<DoctorDto> getDoctorBySpecialtyId(@PathVariable @Positive Long id,
                                                 Pageable pageable) {
        return doctorService.getDoctorsBySpecialtyId(id, pageable);
    }
}
