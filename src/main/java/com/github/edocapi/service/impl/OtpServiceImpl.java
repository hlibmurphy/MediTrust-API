package com.github.edocapi.service.impl;

import com.github.edocapi.dto.GenerateOtpDto;
import com.github.edocapi.dto.VerificateOtpDto;
import com.github.edocapi.service.OtpService;
import com.twilio.Twilio;
import com.twilio.rest.verify.v2.service.Verification;
import com.twilio.rest.verify.v2.service.VerificationCheck;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class OtpServiceImpl implements OtpService {
    @Value("${twilio.account-sid}")
    private String accountSid;

    @Value("${twilio.auth-token}")
    private String authToken;

    @Value("${twilio.service-sid}")
    private String serviceSid;

    @Override
    public ResponseEntity<String> generateOtp(GenerateOtpDto generateOtpDto) {
        Twilio.init(accountSid, authToken);
        Verification verification = Verification.creator(
                serviceSid, generateOtpDto.getPhone(),
                "sms").create();
        System.out.println(verification.getStatus());
        return new ResponseEntity<>("Code has been successfully sent to "
                + generateOtpDto.getPhone(), HttpStatus.OK);
    }

    @Override
    public ResponseEntity<String> verifyOtp(VerificateOtpDto verificateOtpDto) {
        Twilio.init(accountSid, authToken);
        try {
            VerificationCheck verificationCheck = VerificationCheck.creator(serviceSid)
                    .setTo(verificateOtpDto.getPhone())
                    .setCode(verificateOtpDto.getCode())
                    .create();

            System.out.println(verificationCheck.getStatus());
        } catch (Exception e) {
            return new ResponseEntity<>("Invalid code",
                    HttpStatus.BAD_REQUEST);
        }

        return new ResponseEntity<>("Phone was successfully verified", HttpStatus.OK);
    }
}
