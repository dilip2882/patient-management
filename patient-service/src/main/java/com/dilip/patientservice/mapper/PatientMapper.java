package com.dilip.patientservice.mapper;

import com.dilip.patientservice.dto.PatientRequestDTO;
import com.dilip.patientservice.dto.PatientResponseDTO;
import com.dilip.patientservice.model.Patient;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

/**
 * Mapper for converting between Patient entity and DTOs
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class PatientMapper {
    /**
     * Converts a Patient entity to PatientResponseDTO
     * 
     * @param patient the Patient entity to convert
     * @return the PatientResponseDTO
     */
    public static PatientResponseDTO toDTO(Patient patient) {
        return PatientResponseDTO.builder()
                .id(patient.getId().toString())
                .name(patient.getName())
                .address(patient.getAddress())
                .email(patient.getEmail())
                .dateOfBirth(patient.getDateOfBirth().toString())
                .registeredDate(patient.getRegisteredDate().toString())
                .build();
    }

    /**
     * Creates a new Patient entity from PatientRequestDTO
     * 
     * @param dto the PatientRequestDTO
     * @return new Patient entity
     */
    public static Patient toModel(PatientRequestDTO dto) {
        Patient patient = new Patient();
        return updateModel(patient, dto);
    }

    /**
     * Updates an existing Patient entity with data from PatientRequestDTO
     * 
     * @param patient the Patient entity to update
     * @param dto the PatientRequestDTO with new data
     * @return updated Patient entity
     */
    public static Patient updateModel(Patient patient, PatientRequestDTO dto) {
        patient.setName(dto.name());
        patient.setAddress(dto.address());
        patient.setEmail(dto.email());

        try {
            patient.setDateOfBirth(LocalDate.parse(dto.dateOfBirth()));
            patient.setRegisteredDate(LocalDate.parse(dto.registeredDate()));
        } catch (Exception e) {
            throw new IllegalArgumentException("Error parsing date: " + e.getMessage(), e);
        }

        return patient;
    }
}
