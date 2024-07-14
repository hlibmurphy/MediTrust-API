package com.github.edocapi.service;

import com.github.edocapi.dto.RecallDto;
import com.github.edocapi.dto.RecallRequestDto;

public interface RecallService {
    RecallDto addPhoneNumber(RecallRequestDto requestDto);
}
