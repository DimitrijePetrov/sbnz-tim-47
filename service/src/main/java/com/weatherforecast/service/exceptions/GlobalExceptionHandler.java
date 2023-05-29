package com.weatherforecast.service.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

import javax.validation.ValidationException;
import java.util.Date;
import java.util.Objects;

@ControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler({BadRequestException.class})
    public ResponseEntity<ExceptionResponse> handleBadRequestException(RuntimeException ex, WebRequest request) {
        return new ResponseEntity<>(getExceptionResponse(ex.getMessage(), HttpStatus.BAD_REQUEST), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler({NoResponseFromAPIException.class})
    public ResponseEntity<ExceptionResponse> handleNoResponseFromAPIException(RuntimeException ex, WebRequest request) {
        return new ResponseEntity<>(getExceptionResponse(ex.getMessage(), HttpStatus.NOT_FOUND), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler({MethodArgumentNotValidException.class})
    public ResponseEntity<ExceptionResponse> handleMethodArgumentNotValidException(MethodArgumentNotValidException ex, WebRequest request) {
        return new ResponseEntity<>(getExceptionResponse(Objects.requireNonNull(ex.getFieldError()).getDefaultMessage(), HttpStatus.BAD_REQUEST), HttpStatus.BAD_REQUEST);
    }

    private static ExceptionResponse getExceptionResponse(String message, HttpStatus status){
        ExceptionResponse error = new ExceptionResponse();
        error.setStatusCode(status.value());
        error.setMessage(message);
        error.setTimeStamp(new Date());
        return error;
    }
}
