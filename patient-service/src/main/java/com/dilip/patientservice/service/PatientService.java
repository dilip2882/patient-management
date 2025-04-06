package com.dilip.patientservice.service;

import com.dilip.patientservice.dto.PatientResponseDTO;
import com.dilip.patientservice.mapper.PatientMapper;
import com.dilip.patientservice.model.Patient;
import com.dilip.patientservice.repository.PatientRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PatientService {
    private final PatientRepository patientRepository;

    public PatientService(PatientRepository patientRepository) {
        this.patientRepository = patientRepository;
    }

    public List<PatientResponseDTO> getPatients() {
        List<Patient> patients = patientRepository.findAll();

        return patients.stream().map(PatientMapper::toDTO).toList();
    }
}
