package com.github.edocapi.controller;

import com.github.edocapi.dto.RecallDto;
import com.github.edocapi.dto.RecallRequestDto;
import com.github.edocapi.service.RecallService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/recall")
@RequiredArgsConstructor
@Validated
public class RecallController {
    private final RecallService recallService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public RecallDto addNumber(@Valid @RequestBody RecallRequestDto requestDto) {
        return recallService.addNumber(requestDto);
    }
}
