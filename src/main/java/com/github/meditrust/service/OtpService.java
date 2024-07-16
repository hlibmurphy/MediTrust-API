package com.github.meditrust.service;

import com.github.meditrust.dto.GenerateOtpDto;
import com.github.meditrust.dto.VerificateOtpDto;
import org.springframework.http.ResponseEntity;

public interface OtpService {
    ResponseEntity<String> generateOtp(GenerateOtpDto generateOtpDto);

    ResponseEntity<String> verifyOtp(VerificateOtpDto verificateOtpDto);
}
