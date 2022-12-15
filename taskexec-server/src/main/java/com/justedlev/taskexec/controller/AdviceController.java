package com.justedlev.taskexec.controller;

import com.justedlev.taskexec.model.response.ErrorDetailsResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import javax.persistence.EntityNotFoundException;

@ControllerAdvice
public class AdviceController extends ResponseEntityExceptionHandler {
    @ExceptionHandler(value = IllegalArgumentException.class)
    public ResponseEntity<ErrorDetailsResponse> handle(Exception ex, WebRequest request) {
        ErrorDetailsResponse response = ErrorDetailsResponse.builder()
                .details(request.getDescription(false))
                .message(ex.getMessage())
                .build();

        return new ResponseEntity<>(response, HttpStatus.CONFLICT);
    }

    @ExceptionHandler(value = EntityNotFoundException.class)
    public ResponseEntity<ErrorDetailsResponse> handleNotFoundException(Exception ex, WebRequest request) {
        ErrorDetailsResponse response = ErrorDetailsResponse.builder()
                .details(request.getDescription(false))
                .message(ex.getMessage())
                .build();

        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }
}
