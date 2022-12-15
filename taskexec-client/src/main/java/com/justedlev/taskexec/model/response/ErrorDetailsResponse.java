package com.justedlev.taskexec.model.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ErrorDetailsResponse {
    private String message;
    private String details;
    @Builder.Default
    private Date timestamp = new Date();
}
