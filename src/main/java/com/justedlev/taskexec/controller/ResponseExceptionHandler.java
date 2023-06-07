package com.justedlev.taskexec.controller;

import com.justedlevhub.api.model.response.ErrorDetailsResponse;
import com.justedlevhub.api.model.response.ValidationErrorResponse;
import com.justedlevhub.api.model.response.ViolationResponse;
import feign.FeignException;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import javax.persistence.EntityNotFoundException;
import javax.validation.ConstraintViolationException;

@Slf4j
@ControllerAdvice
@RequiredArgsConstructor
public class ResponseExceptionHandler extends ResponseEntityExceptionHandler {
    private final ModelMapper defaultMapper;

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

    @ExceptionHandler(value = ConstraintViolationException.class)
    public ResponseEntity<ValidationErrorResponse> handleFeignException(ConstraintViolationException ex,
                                                                        WebRequest request) {
        log.error(ex.getMessage());
        ex.printStackTrace();
        var violations = ex.getConstraintViolations()
                .stream()
                .map(current -> ViolationResponse.builder()
                        .fieldName(current.getPropertyPath().toString())
                        .message(current.getMessage())
                        .build())
                .toList();
        var response = ValidationErrorResponse.builder()
                .details(request.getDescription(false))
                .violations(violations)
                .build();

        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @NonNull
    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
                                                                  @NonNull HttpHeaders headers,
                                                                  @NonNull HttpStatus status,
                                                                  @NonNull WebRequest request) {
        log.error(ex.getMessage());
        ex.printStackTrace();
        var violations = ex.getBindingResult().getFieldErrors()
                .stream()
                .map(current -> ViolationResponse.builder()
                        .fieldName(current.getField())
                        .message(current.getDefaultMessage())
                        .build())
                .toList();
        var error = ValidationErrorResponse.builder()
                .details(request.getDescription(false))
                .violations(violations)
                .build();

        return new ResponseEntity<>(error, status);
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
