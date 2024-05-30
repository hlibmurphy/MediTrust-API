package com.github.edocapi.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.github.edocapi.dto.CreateSpecialtyRequestDto;
import com.github.edocapi.dto.SpecialtyDto;
import com.github.edocapi.mapper.SpecialtyMapper;
import com.github.edocapi.model.Specialty;
import com.github.edocapi.repository.SpecialtyRepository;
import com.github.edocapi.service.impl.SpecialtyServiceImpl;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class SpecialtyServiceTest {
    @Mock
    private SpecialtyRepository specialtyRepository;

    @Mock
    private SpecialtyMapper specialtyMapper;

    @InjectMocks
    private SpecialtyServiceImpl specialtyService;

    @Test
    public void findAll_withNoArgs_shouldReturnAllSpecialties() {
        List<Specialty> specialties = List.of(createSpecialty());

        when(specialtyRepository.findAll()).thenReturn(specialties);

        List<SpecialtyDto> expected = specialties.stream()
                .map(this::mapToDto)
                .toList();
        when(specialtyMapper.toDtos(any())).thenReturn(expected);
        List<SpecialtyDto> actual = specialtyService.findAll();

        assertEquals(expected, actual,
                "Retrieved specialties should match with expected ones");
        verify(specialtyRepository, times(1)).findAll();
        verify(specialtyMapper, times(1)).toDtos(any());
    }

    @Test
    public void save_withCreateSpecialtyRequestDto_shouldSaveSpecialty() {
        CreateSpecialtyRequestDto specialtyRequestDto = createSpecialtyRequestDto();
        Specialty specialty = createSpecialty();
        when(specialtyMapper.toModel(specialtyRequestDto)).thenReturn(specialty);
        when(specialtyRepository.save(any())).thenReturn(specialty);
        SpecialtyDto expected = mapToDto(specialty);
        when(specialtyMapper.toDto(any(Specialty.class))).thenReturn(expected);

        SpecialtyDto actual = specialtyService.save(specialtyRequestDto);
        assertEquals(expected, actual,
                "The retrieved specialty DTO should match the expected one");
        verify(specialtyMapper, times(1)).toModel(any());
        verify(specialtyRepository, times(1)).save(any());
        verify(specialtyMapper, times(1)).toDto(any(Specialty.class));
    }

    private CreateSpecialtyRequestDto createSpecialtyRequestDto() {
        return new CreateSpecialtyRequestDto("Specialty name");
    }

    private SpecialtyDto mapToDto(Specialty specialty) {
        return new SpecialtyDto(specialty.getId(),
                specialty.getName());
    }

    private Specialty createSpecialty() {
        Specialty specialty = new Specialty();
        specialty.setId(1L);
        specialty.setName("Test Specialty");
        return specialty;
    }
}
