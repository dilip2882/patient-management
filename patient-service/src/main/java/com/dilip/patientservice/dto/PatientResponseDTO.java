package com.dilip.patientservice.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

@Builder
@Schema(description = "Patient response data transfer object")
public record PatientResponseDTO(
    @Schema(description = "Patient's unique identifier")
    String id,
    
    @Schema(description = "Patient's full name")
    String name,
    
    @Schema(description = "Patient's email address")
    String email,
    
    @Schema(description = "Patient's residential address")
    String address,
    
    @Schema(description = "Patient's date of birth")
    String dateOfBirth,
    
    @Schema(description = "Date when patient was registered")
    String registeredDate
) {}
