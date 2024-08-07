package com.github.meditrust.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.anyLong;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.github.meditrust.dto.CreateDoctorRequestDto;
import com.github.meditrust.dto.DoctorDto;
import com.github.meditrust.dto.DoctorDtoWithoutScheduleId;
import com.github.meditrust.dto.SpecialtyDto;
import com.github.meditrust.mapper.DoctorMapper;
import com.github.meditrust.model.Doctor;
import com.github.meditrust.model.DoctorSchedule;
import com.github.meditrust.model.Specialty;
import com.github.meditrust.repository.DoctorRepository;
import com.github.meditrust.repository.SpecialtyRepository;
import com.github.meditrust.service.impl.DoctorServiceImpl;
import jakarta.persistence.EntityNotFoundException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

@ExtendWith(MockitoExtension.class)
public class DoctorServiceTest {
    @Mock
    private DoctorRepository doctorRepository;

    @Mock
    private SpecialtyRepository specialtyRepository;

    @Mock
    private DoctorMapper doctorMapper;

    @InjectMocks
    private DoctorServiceImpl doctorService;

    @Test
    @DisplayName("Find all doctors")
    public void findAll_withPageable_shouldReturnAllDoctors() {
        List<Doctor> doctors = List.of(createDoctor());
        Page<Doctor> pagedDoctors = new PageImpl<>(doctors);
        when(doctorRepository.findAll(Pageable.unpaged())).thenReturn(pagedDoctors);
        when(doctorMapper.toDtosWithoutSchedule(any()))
                .thenReturn(doctors.stream()
                        .map(this::mapToDtoWithoutSchedule)
                        .toList());

        List<DoctorDtoWithoutScheduleId> actual = doctorService.findAll(Pageable.unpaged());

        assertEquals(doctors.size(), actual.size());
        verify(doctorRepository, times(1)).findAll(Pageable.unpaged());
        verify(doctorMapper, times(1)).toDtosWithoutSchedule(any());
    }

    @Test
    @DisplayName("Find doctor by ID")
    public void findById_withValidId_shouldReturnDoctor() {
        Doctor doctor = createDoctor();
        DoctorDtoWithoutScheduleId expected = mapToDtoWithoutSchedule(doctor);

        when(doctorRepository.findById(anyLong())).thenReturn(Optional.of(doctor));
        when(doctorMapper.toDtoWithoutSchedule(doctor)).thenReturn(expected);

        DoctorDtoWithoutScheduleId actual = doctorService.findById(1L);

        assertEquals(expected, actual, "The doctor should have been found");
        verify(doctorRepository, times(1)).findById(1L);
        verify(doctorMapper, times(1)).toDtoWithoutSchedule(doctor);
    }

    @Test
    @DisplayName("Find doctor by invalid ID")
    public void findById_withInvalidId_shouldThrowException() {
        when(doctorRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class,
                () -> doctorService.findById(1L));
        verify(doctorRepository, times(1)).findById(1L);
    }

    @Test
    @DisplayName("Save doctor")
    public void save_withDoctor_shouldSaveDoctor() {
        CreateDoctorRequestDto doctorRequestDto = createDoctorRequestDto();
        Doctor doctor = createDoctor();
        Specialty specialty = new Specialty();

        when(doctorMapper.toModel(doctorRequestDto)).thenReturn(doctor);
        when(specialtyRepository.findById(any())).thenReturn(Optional.of(specialty));
        when(doctorRepository.save(any(Doctor.class))).thenReturn(doctor);
        DoctorDto expected = mapToDto(doctor);
        when(doctorMapper.toDto(doctor)).thenReturn(expected);

        DoctorDto actual = doctorService.save(doctorRequestDto);

        assertEquals(expected, actual,
                "The retrieved doctor should have been the same as expected");
        verify(doctorRepository, times(1)).save(doctor);
        verify(doctorMapper, times(1)).toModel(doctorRequestDto);
        verify(doctorMapper, times(1)).toDto(doctor);
    }

    @Test
    @DisplayName("Update doctor")
    public void update_withDoctor_shouldUpdateDoctor() {
        Long doctorId = 1L;
        CreateDoctorRequestDto doctorRequestDto = createDoctorRequestDto();
        Doctor doctor = createDoctor();
        DoctorDtoWithoutScheduleId expected = mapToDtoWithoutSchedule(doctor);
        Specialty specialty = new Specialty();

        when(doctorRepository.findById(doctorId)).thenReturn(Optional.of(doctor));
        when(doctorMapper.toModel(doctorRequestDto)).thenReturn(doctor);
        when(specialtyRepository.findById(any())).thenReturn(Optional.of(specialty));
        when(doctorRepository.save(doctor)).thenReturn(doctor);
        when(doctorMapper.toDtoWithoutSchedule(doctor)).thenReturn(expected);

        DoctorDtoWithoutScheduleId actual = doctorService.update(doctorId, doctorRequestDto);

        assertEquals(expected, actual);
        verify(doctorRepository, times(1)).findById(doctorId);
        verify(doctorRepository, times(1)).save(doctor);
        verify(doctorMapper, times(1)).toModel(doctorRequestDto);
        verify(doctorMapper, times(1)).toDtoWithoutSchedule(doctor);
    }

    @Test
    @DisplayName("Get doctors by specialty")
    public void getDoctorsBySpecialtyId_withValidSpecialtyId_shouldReturnDoctors() {
        List<Doctor> doctors = new ArrayList<>();
        doctors.add(createDoctor());
        doctors.add(createDoctor());

        when(doctorRepository.findDoctorsBySpecialtyId(anyLong(), any(Pageable.class)))
                .thenReturn(new PageImpl<>(doctors));
        List<DoctorDto> expected = doctors.stream()
                .map(this::mapToDto)
                .toList();
        when(doctorMapper.toDtos(doctors)).thenReturn(expected);
        List<DoctorDto> actual = doctorService.getDoctorsBySpecialtyId(1L, Pageable.unpaged());
        Assertions.assertEquals(expected, actual, "The retrieved DTOs should match");
    }

    private DoctorDtoWithoutScheduleId mapToDtoWithoutSchedule(Doctor doctor) {
        DoctorDtoWithoutScheduleId dto = new DoctorDtoWithoutScheduleId(
                doctor.getId(),
                doctor.getFirstName(),
                doctor.getLastName(),
                Set.of(createSpecialtyDto()),
                doctor.getBackground(),
                doctor.getExperience(),
                doctor.getAverageRating()
        );

        return dto;
    }

    private Doctor createDoctor() {
        Doctor doctor = new Doctor();
        doctor.setId(1L);
        doctor.setFirstName("First Name");
        doctor.setLastName("Last Name");
        doctor.setBackground("background");
        doctor.setExperience(5);
        doctor.setAverageRating(5);
        Specialty specialty = new Specialty();
        specialty.setId(1L);
        doctor.setSpecialties(Set.of(specialty));
        DoctorSchedule schedule = new DoctorSchedule();
        schedule.setId(1L);
        doctor.setSchedule(schedule);

        return doctor;
    }

    private DoctorDto mapToDto(Doctor doctor) {
        return new DoctorDto(
                doctor.getId(),
                doctor.getFirstName(),
                doctor.getLastName(),
                doctor.getPrice(),
                Set.of(createSpecialtyDto()),
                doctor.getBackground(),
                doctor.getAbout(),
                doctor.getServiceOffered(),
                doctor.getSchedule().getId(),
                doctor.getExperience(),
                doctor.getAverageRating()
        );
    }

    private SpecialtyDto createSpecialtyDto() {
        return new SpecialtyDto(1L, "Doctor specialty");
    }

    private CreateDoctorRequestDto createDoctorRequestDto() {
        return new CreateDoctorRequestDto(
                "First Name",
                "Last Name",
                "0535638593",
                BigDecimal.valueOf(1000),
                Set.of(1L),
                5,
                "background",
                "About",
                "Service Offered"
        );
    }

}
