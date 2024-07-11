package com.github.edocapi.service;

import com.github.edocapi.dto.GenerateOtpDto;
import com.github.edocapi.dto.VerificateOtpDto;
import org.springframework.http.ResponseEntity;

public interface OtpService {
    ResponseEntity<String> generateOtp(GenerateOtpDto generateOtpDto);

    ResponseEntity<String> verifyOtp(VerificateOtpDto verificateOtpDto);
}
