package com.dilip.patientservice.dto;

import com.dilip.patientservice.dto.validators.CreatePatientValidationGroup;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Builder;

@Builder
public record PatientRequestDTO(
    @NotBlank(message = "Name is required")
    @Size(max = 100, message = "Name cannot exceed 100 characters")
    String name,

    @NotBlank(message = "Email is required")
    @Email(message = "Email should be valid")
    String email,

    @NotBlank(message = "Address is required")
    String address,

    @NotBlank(message = "Date of birth is required")
    @Pattern(regexp = "\\d{4}-\\d{2}-\\d{2}", message = "Date of birth must be in format yyyy-MM-dd")
    String dateOfBirth,

    @NotBlank(groups = CreatePatientValidationGroup.class, message = "Registered date is required")
    @Pattern(regexp = "\\d{4}-\\d{2}-\\d{2}", message = "Registered date must be in format yyyy-MM-dd")
    String registeredDate
) {}
