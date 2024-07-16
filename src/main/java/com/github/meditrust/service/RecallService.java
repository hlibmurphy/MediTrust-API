package com.github.meditrust.service;

import com.github.meditrust.dto.RecallDto;
import com.github.meditrust.dto.RecallRequestDto;

public interface RecallService {
    RecallDto addPhoneNumber(RecallRequestDto requestDto);
}
