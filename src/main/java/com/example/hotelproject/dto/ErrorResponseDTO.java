package com.example.hotelproject.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Schema(description = "Error response")
public class ErrorResponseDTO {
    
    @Schema(description = "Timestamp when error occurred", example = "2026-02-21T19:30:00")
    private LocalDateTime timestamp;
    
    @Schema(description = "HTTP status code", example = "404")
    private Integer status;
    
    @Schema(description = "Error type", example = "Not Found")
    private String error;
    
    @Schema(description = "Error message", example = "Hotel not found with id: 1")
    private String message;
}
