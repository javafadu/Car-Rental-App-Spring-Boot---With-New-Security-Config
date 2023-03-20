package com.ascarrent.exception;

import com.ascarrent.exception.message.ApiResponseError;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.ConversionNotSupportedException;
import org.springframework.beans.TypeMismatchException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;


import java.nio.file.AccessDeniedException;
import java.util.List;
import java.util.stream.Collectors;

@ControllerAdvice // Central Exception Handling
public class AsCarRentExceptionHandler extends ResponseEntityExceptionHandler {
    // AIM : Create Custom Exception System
    // overriding of throwable exceptions and response custom message structure

    Logger logger = LoggerFactory.getLogger(AsCarRentExceptionHandler.class);
    // Factory Design Pattern

    // return Response Entity method
    private ResponseEntity<Object> buildResponseEntity(ApiResponseError error) {
        logger.error(error.getMessage());
        return new ResponseEntity<>(error, error.getStatus());
    }


    // 1- Handle ResourceNotFound Exception
    @ExceptionHandler(ResourceNotFoundException.class)
    protected ResponseEntity<Object> handleResourceNotFoundException(ResourceNotFoundException ex, WebRequest request) {

        ApiResponseError error = new ApiResponseError(
                HttpStatus.NOT_FOUND,
                ex.getMessage(),
                request.getDescription(false));

        return buildResponseEntity(error);

    }

    // 2- Handle MethodArgumentNotValid Exception
    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {

        List<String> errors = ex.getBindingResult().getFieldErrors()
                .stream()
                .map(e -> e.getDefaultMessage())
                .collect(Collectors.toList());

        ApiResponseError error = new ApiResponseError(
                HttpStatus.BAD_REQUEST,
                errors.get(0).toString(), // get the first error message,
                request.getDescription(false)
        );
        return buildResponseEntity(error);
    }

    // 3- Handle TypeMismatch Exception
    @Override
    protected ResponseEntity<Object> handleTypeMismatch(TypeMismatchException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        ApiResponseError error = new ApiResponseError(
                HttpStatus.BAD_REQUEST,
                ex.getMessage(), // get the first error message,
                request.getDescription(false)
        );
        return buildResponseEntity(error);
    }

    // 4- Handle ConversionNotSupported Exception
    @Override
    protected ResponseEntity<Object> handleConversionNotSupported(ConversionNotSupportedException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        ApiResponseError error = new ApiResponseError(
                HttpStatus.INTERNAL_SERVER_ERROR,
                ex.getMessage(), // get the first error message,
                request.getDescription(false)
        );
        return buildResponseEntity(error);
    }

    // 5- Handle HttpMessageNotReadable Exception
    @Override
    protected ResponseEntity<Object> handleHttpMessageNotReadable(HttpMessageNotReadableException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        ApiResponseError error = new ApiResponseError(
                HttpStatus.BAD_REQUEST,
                ex.getMessage(), // get the first error message,
                request.getDescription(false)
        );
        return buildResponseEntity(error);
    }

    // 6- Handle Conflict Exception
    @ExceptionHandler(ConflictException.class)
    protected ResponseEntity<Object> handleConflictException(
            ConflictException ex, WebRequest request) {
        ApiResponseError error = new ApiResponseError(HttpStatus.CONFLICT,
                ex.getMessage(),
                request.getDescription(false));
        return buildResponseEntity(error);
    }

    // 7- Handle AccessDenied Exception
    @ExceptionHandler(AccessDeniedException.class)
    protected ResponseEntity<Object> handleAccessDeniedException(
            AccessDeniedException ex, WebRequest request) {
        ApiResponseError error = new ApiResponseError(HttpStatus.FORBIDDEN,
                ex.getMessage(),
                request.getDescription(false));
        return buildResponseEntity(error);
    }

    // 8- Handle Authentication Exception
    @ExceptionHandler(AuthenticationException.class)
    protected ResponseEntity<Object> handleAuthenticationException(
            AuthenticationException ex, WebRequest request) {
        ApiResponseError error = new ApiResponseError(HttpStatus.BAD_REQUEST,
                ex.getMessage(),
                request.getDescription(false));
        return buildResponseEntity(error);
    }

    // 9- Handle BadRequest Exception
    @ExceptionHandler(BadRequestException.class)
    protected ResponseEntity<Object> handleBadRequestException(
            BadRequestException ex, WebRequest request) {
        ApiResponseError error = new ApiResponseError(HttpStatus.BAD_REQUEST,
                ex.getMessage(),
                request.getDescription(false));
        return buildResponseEntity(error);
    }



    // Handle RunTimeException (in case of any exception situation other than above situations in RunTimeException layer)
    @ExceptionHandler(RuntimeException.class)
    protected ResponseEntity<Object> handleRuntimeException(RuntimeException ex, WebRequest request) {
        ApiResponseError error = new ApiResponseError(HttpStatus.INTERNAL_SERVER_ERROR,
                ex.getMessage(),
                request.getDescription(false));
        return buildResponseEntity(error);
    }

    // Handle Exception (parent of RuntimeException and OtherExceptions layers
    @ExceptionHandler(Exception.class)
    protected ResponseEntity<Object> handleGeneralException(Exception ex, WebRequest request) {
        ApiResponseError error = new ApiResponseError(HttpStatus.INTERNAL_SERVER_ERROR,
                ex.getMessage(),
                request.getDescription(false));
        return buildResponseEntity(error);
    }


}
