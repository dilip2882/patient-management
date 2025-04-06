package com.dilip.patientservice.controller;

import com.dilip.patientservice.dto.PatientRequestDTO;
import com.dilip.patientservice.dto.PatientResponseDTO;
import com.dilip.patientservice.dto.validators.CreatePatientValidationGroup;
import com.dilip.patientservice.service.PatientService;
import jakarta.validation.groups.Default;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/patients")
public class PatientController {
    private final PatientService patientService;

    public PatientController(PatientService patientService) {
        this.patientService = patientService;
    }

    @GetMapping
    public ResponseEntity<List<PatientResponseDTO>> getPatients() {
        List<PatientResponseDTO> patients = patientService.getPatients();

        return ResponseEntity.ok().body(patients);
    }

    @PostMapping
    public ResponseEntity<PatientResponseDTO> createPatient(
            @Validated({Default.class, CreatePatientValidationGroup.class})
            @RequestBody PatientRequestDTO patientRequestDTO
    ) {
        PatientResponseDTO patientResponseDTO = patientService.createPatient(
                patientRequestDTO);

        return ResponseEntity.ok().body(patientResponseDTO);
    }

}
