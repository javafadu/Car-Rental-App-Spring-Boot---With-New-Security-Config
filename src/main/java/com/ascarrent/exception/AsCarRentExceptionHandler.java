package com.ascarrent.exception;

import com.ascarrent.exception.message.ApiResponseError;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice // Central Exception Handling
public class AsCarRentExceptionHandler extends ResponseEntityExceptionHandler {
    // AIM : Create Custom Exception System
    // overriding of throwable exceptions and response custom message structure


    // return Response Entity method
    private ResponseEntity<Object> buildResponseEntity(ApiResponseError error) {
        return new ResponseEntity<>(error, error.getStatus());
    }



    @ExceptionHandler(ResourceNotFoundException.class)
    protected ResponseEntity<Object> handleResourceNotFoundException(ResourceNotFoundException ex, WebRequest request) {

        ApiResponseError error = new ApiResponseError(
                HttpStatus.NOT_FOUND,
                ex.getMessage(),
                request.getDescription(false));

        return buildResponseEntity(error);

    }

}
