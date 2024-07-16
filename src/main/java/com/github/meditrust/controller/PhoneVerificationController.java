package com.github.meditrust.controller;

import com.github.meditrust.dto.GenerateOtpDto;
import com.github.meditrust.dto.VerificateOtpDto;
import com.github.meditrust.service.OtpService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/verification")
@RequiredArgsConstructor
public class PhoneVerificationController {
    private final OtpService otpService;

    @PostMapping("/generateOtp")
    @Operation(description = "Generate an OTP code and send it to a specified phone")
    public ResponseEntity<String> generateOtp(@RequestBody GenerateOtpDto generateOtpDto) {
        return otpService.generateOtp(generateOtpDto);
    }

    @PostMapping("/verifyOtp")
    @Operation(description = "Verify an OTP code")
    public ResponseEntity<String> verifyOtp(@RequestBody VerificateOtpDto verificateOtpDto) {
        return otpService.verifyOtp(verificateOtpDto);
    }
}
