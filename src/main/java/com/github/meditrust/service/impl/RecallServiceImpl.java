package com.github.meditrust.service.impl;

import com.github.meditrust.dto.RecallDto;
import com.github.meditrust.dto.RecallRequestDto;
import com.github.meditrust.mapper.RecallMapper;
import com.github.meditrust.model.Recall;
import com.github.meditrust.repository.RecallRepository;
import com.github.meditrust.service.RecallService;
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
