package com.github.edocapi.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.anyLong;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.github.edocapi.dto.CreateDoctorRequestDto;
import com.github.edocapi.dto.DoctorDto;
import com.github.edocapi.dto.DoctorDtoWithoutScheduleId;
import com.github.edocapi.dto.SpecialtyDto;
import com.github.edocapi.mapper.DoctorMapper;
import com.github.edocapi.model.Doctor;
import com.github.edocapi.model.DoctorSchedule;
import com.github.edocapi.model.Specialty;
import com.github.edocapi.repository.DoctorRepository;
import com.github.edocapi.repository.SpecialtyRepository;
import com.github.edocapi.service.impl.DoctorServiceImpl;
import jakarta.persistence.EntityNotFoundException;
import java.util.List;
import java.util.Optional;
import java.util.Set;
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
    public void findById_withInvalidId_shouldThrowException() {
        when(doctorRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class,
                () -> doctorService.findById(1L));
        verify(doctorRepository, times(1)).findById(1L);
    }

    @Test
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

    private DoctorDtoWithoutScheduleId mapToDtoWithoutSchedule(Doctor doctor) {
        DoctorDtoWithoutScheduleId dto = new DoctorDtoWithoutScheduleId();
        dto.setId(doctor.getId());
        dto.setFirstName(doctor.getFirstName());
        dto.setLastName(doctor.getLastName());

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
                Set.of(createSpecialtyDto()),
                doctor.getBackground(),
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
                Set.of(1L),
                "background",
                5);
    }

}
