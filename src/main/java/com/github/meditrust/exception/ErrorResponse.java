package com.github.meditrust.exception;

import java.io.Serializable;
import java.time.LocalDateTime;
import lombok.Data;
import org.springframework.http.HttpStatus;

@Data
public class ErrorResponse implements Serializable {
    private String timeStamp;
    private int status;
    private String error;
    private String message;

    public ErrorResponse(Exception e, HttpStatus status) {
        this.timeStamp = LocalDateTime.now().toString();
        this.message = e.getMessage();
        this.status = status.value();
        this.error = status.name();
    }
}
