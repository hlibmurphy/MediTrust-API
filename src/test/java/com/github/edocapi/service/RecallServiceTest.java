package com.github.edocapi.service;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import com.github.edocapi.dto.RecallDto;
import com.github.edocapi.dto.RecallRequestDto;
import com.github.edocapi.mapper.RecallMapper;
import com.github.edocapi.model.Recall;
import com.github.edocapi.repository.RecallRepository;
import com.github.edocapi.service.impl.RecallServiceImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class RecallServiceTest {
    @Mock
    private RecallRepository recallRepository;

    @Mock
    private RecallMapper recallMapper;

    @InjectMocks
    private RecallServiceImpl recallService;

    @Test
    @DisplayName("Add phone number")
    public void addPhoneNumber_withRecallRequestDto_shouldSavePhoneNumber() {
        Recall recall = createRecall();
        when(recallMapper.toModel(any(RecallRequestDto.class)))
                .thenReturn(recall);
        when(recallRepository.save(any(Recall.class)))
                .thenReturn(recall);
        RecallDto expected = mapToDto(recall);
        when(recallMapper.toDto(any(Recall.class)))
                .thenReturn(expected);
        RecallRequestDto recallRequestDto = createRecallRequestDto();
        RecallDto actual = recallService.addPhoneNumber(recallRequestDto);
        Assertions.assertEquals(expected, actual,
                "The retrieved DTO should match the expected one");
    }

    private RecallRequestDto createRecallRequestDto() {
        RecallRequestDto recallRequestDto = new RecallRequestDto();
        recallRequestDto.setNumber("123");
        return recallRequestDto;
    }

    private RecallDto mapToDto(Recall recall) {
        RecallDto recallDto = new RecallDto();
        recallDto.setId(recall.getId());
        recallDto.setNumber(recall.getNumber());
        recallDto.setStatus(recall.getStatus());
        return recallDto;
    }

    private Recall createRecall() {
        Recall recall = new Recall();
        recall.setId(1L);
        recall.setNumber("123");
        recall.setStatus(Recall.Status.PENDING);
        return recall;
    }
}
