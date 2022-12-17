package com.justedlev.taskexec.controller;

import com.justedlev.taskexec.model.response.ErrorDetailsResponse;
import feign.FeignException;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import javax.persistence.EntityNotFoundException;

@Slf4j
@ControllerAdvice
public class AdviceController extends ResponseEntityExceptionHandler {
    @ExceptionHandler(value = IllegalArgumentException.class)
    public ResponseEntity<ErrorDetailsResponse> handle(Exception ex, WebRequest request) {
        log.error(ex.getMessage());
        ex.printStackTrace();
        ErrorDetailsResponse response = ErrorDetailsResponse.builder()
                .details(request.getDescription(false))
                .message(ex.getMessage())
                .build();

        return new ResponseEntity<>(response, HttpStatus.CONFLICT);
    }

    @ExceptionHandler(value = EntityNotFoundException.class)
    public ResponseEntity<ErrorDetailsResponse> handleNotFoundException(Exception ex, WebRequest request) {
        log.error(ex.getMessage());
        ex.printStackTrace();
        ErrorDetailsResponse response = ErrorDetailsResponse.builder()
                .details(request.getDescription(false))
                .message(ex.getMessage())
                .build();

        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(value = FeignException.class)
    public ResponseEntity<ErrorDetailsResponse> handleFeignException(FeignException ex,
                                                                     WebRequest request) {
        log.error(ex.getMessage());
        ex.printStackTrace();
        var errorDetails = ErrorDetailsResponse.builder()
                .details(request.getDescription(false))
                .message(ex.getLocalizedMessage())
                .build();
        var status = ex.status() > 0 ? HttpStatus.valueOf(ex.status()) : HttpStatus.INTERNAL_SERVER_ERROR;

        return new ResponseEntity<>(errorDetails, status);
    }

    @NonNull
    @Override
    protected ResponseEntity<Object> handleExceptionInternal(Exception ex,
                                                             Object body,
                                                             @NonNull HttpHeaders headers,
                                                             @NonNull HttpStatus status,
                                                             WebRequest request) {
        log.error(ex.getMessage());
        ex.printStackTrace();
        ErrorDetailsResponse response = ErrorDetailsResponse.builder()
                .details(request.getDescription(false))
                .message(ex.getMessage())
                .build();

        return ResponseEntity.status(status)
                .headers(headers)
                .body(response);
    }
}
