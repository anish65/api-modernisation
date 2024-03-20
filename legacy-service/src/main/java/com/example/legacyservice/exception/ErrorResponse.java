package com.example.legacyservice.exception;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Schema(description = "Error response Output")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ErrorResponse {

    @Schema(description = "Returned status", required = true)
    private int statusCode;
    @Schema(description = "Returned Message", required = true)
    private String message;
}
