package com.dilip.patientservice.controller;

import com.dilip.patientservice.dto.PatientRequestDTO;
import com.dilip.patientservice.dto.PatientResponseDTO;
import com.dilip.patientservice.dto.validators.CreatePatientValidationGroup;
import com.dilip.patientservice.service.PatientService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.groups.Default;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/patients")
@RequiredArgsConstructor
@Tag(name = "Patient Controller", description = "Endpoints for managing patients")
@Validated
public class PatientController {
    private final PatientService patientService;

    @GetMapping
    @Operation(summary = "Get all patients with pagination")
    @ApiResponse(responseCode = "200", description = "Successfully retrieved patients")
    public ResponseEntity<Page<PatientResponseDTO>> getPatients(
            @Parameter(description = "Pagination information") 
            @PageableDefault(size = 10, sort = "name") Pageable pageable
    ) {
        return ResponseEntity.ok(patientService.getPatients(pageable));
    }

    @GetMapping("/all")
    @Operation(summary = "Get all patients without pagination")
    @ApiResponse(responseCode
            = "200", description = "Successfully retrieved patients")
    public ResponseEntity<List<PatientResponseDTO>> getAllPatients() {
        return ResponseEntity.ok(patientService.getPatients());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get patient by ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved patient"),
            @ApiResponse(responseCode = "404", description = "Patient not found", 
                    content = @Content(schema = @Schema(implementation = Object.class)))
    })
    public ResponseEntity<PatientResponseDTO> getPatientById(
            @Parameter(description = "Patient ID", required = true)
            @PathVariable UUID id
    ) {
        return ResponseEntity.ok(patientService.getPatientById(id));
    }

    @PostMapping
    @Operation(summary = "Create a new patient")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Patient created successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input"),
            @ApiResponse(responseCode = "409", description = "Email already exists")
    })
    public ResponseEntity<PatientResponseDTO> createPatient(
            @Parameter(description = "Patient data", required = true)
            @Validated({Default.class, CreatePatientValidationGroup.class})
            @RequestBody PatientRequestDTO patientRequestDTO
    ) {
        return new ResponseEntity<>(patientService.createPatient(patientRequestDTO), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update an existing patient")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Patient updated successfully"),
            @ApiResponse(responseCode = "404", description = "Patient not found"),
            @ApiResponse(responseCode = "400", description = "Invalid input"),
            @ApiResponse(responseCode = "409", description = "Email already exists")
    })
    public ResponseEntity<PatientResponseDTO> updatePatient(
            @Parameter(description = "Patient ID", required = true)
            @PathVariable UUID id,
            
            @Parameter(description = "Updated patient data", required = true)
            @Validated @RequestBody PatientRequestDTO patientRequestDTO
    ) {
        return ResponseEntity.ok(patientService.updatePatient(id, patientRequestDTO));
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Delete a patient")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Patient deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Patient not found")
    })
    public ResponseEntity<Void> deletePatient(
            @Parameter(description = "Patient ID", required = true)
            @PathVariable UUID id
    ) {
        patientService.deletePatient(id);
        return ResponseEntity.noContent().build();
    }
}
