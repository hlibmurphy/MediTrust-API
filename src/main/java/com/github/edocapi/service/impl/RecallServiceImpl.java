package com.github.edocapi.service.impl;

import com.github.edocapi.dto.RecallDto;
import com.github.edocapi.dto.RecallRequestDto;
import com.github.edocapi.mapper.RecallMapper;
import com.github.edocapi.model.Recall;
import com.github.edocapi.repository.RecallRepository;
import com.github.edocapi.service.RecallService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RecallServiceImpl implements RecallService {
    private final RecallRepository recallRepository;
    private final RecallMapper recallMapper;

    @Override
    public RecallDto addPhoneNumber(RecallRequestDto requestDto) {
        Recall recall = recallMapper.toModel(requestDto);
        recallRepository.save(recall);
        return recallMapper.toDto(recall);
    }
}
