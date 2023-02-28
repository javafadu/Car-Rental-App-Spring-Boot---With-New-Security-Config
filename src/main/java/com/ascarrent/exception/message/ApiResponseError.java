package com.ascarrent.exception.message;

import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;

public class ApiResponseError {
    // AIM: General Exception Template for Custom Error Messages

    private HttpStatus status;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime timeStamp;

    private String message;

    private String requestURI;

    // Constructors
    private ApiResponseError() {
        // do not use default constructor outside of this class
        // i want to use parameterized constructor to create an object (1 or 2 or 3 parameterized)
        timeStamp = LocalDateTime.now();
    }

    public ApiResponseError(HttpStatus status) {
        this(); // above private constructor
        this.message="Unexpected Error";
        this.status=status;
    }
    public ApiResponseError(HttpStatus status, String message, String requestURI) {
        this(status); // above 1 parameter constructor
        this.message=message;
        this.requestURI=requestURI;
    }

    // Getter Setters

    public HttpStatus getStatus() {
        return status;
    }

    public void setStatus(HttpStatus status) {
        this.status = status;
    }

    public LocalDateTime getTimeStamp() {
        return timeStamp;
    }


    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getRequestURI() {
        return requestURI;
    }

    public void setRequestURI(String requestURI) {
        this.requestURI = requestURI;
    }
}
