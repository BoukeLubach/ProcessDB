package com.wes.processdb.business.exception;
import org.springframework.http.HttpStatus;

import lombok.*;
import java.util.List;
import java.util.ArrayList;
import org.springframework.validation.FieldError;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class ErrorResponse {
    private String errorMessage;
    private HttpStatus statusCode;
    private long timestamp;
    private List<String> errors;

    public ErrorResponse(HttpStatus status, String errorMessage) {
        this.errorMessage = errorMessage;
        this.statusCode = status;
        this.timestamp = System.currentTimeMillis();
        this.errors = new ArrayList<>();
    }

    public ErrorResponse(String errorMessage) {
        this.errorMessage = errorMessage;
        this.timestamp = System.currentTimeMillis();
    }

    public void addValidationErrors(List<FieldError> fieldErrors) {
        fieldErrors.forEach(error -> {
            String errorMessage = String.format("%s: %s", error.getField(), error.getDefaultMessage());
            errors.add(errorMessage);
        });
    }

    public void addValidationError(String field, String message) {
        String errorMessage = String.format("%s: %s", field, message);
        errors.add(errorMessage);
    }

}

