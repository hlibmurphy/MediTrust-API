package com.github.meditrust.controller;

import com.github.meditrust.dto.RecallDto;
import com.github.meditrust.dto.RecallRequestDto;
import com.github.meditrust.service.RecallService;
import io.swagger.v3.oas.annotations.Operation;
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
    @Operation(description = "Add phone number to list of numbers to call")
    public RecallDto addPhoneNumber(@Valid @RequestBody RecallRequestDto requestDto) {
        return recallService.addPhoneNumber(requestDto);
    }
}