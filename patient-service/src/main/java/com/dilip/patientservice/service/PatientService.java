package com.dilip.patientservice.service;

import com.dilip.patientservice.dto.PatientRequestDTO;
import com.dilip.patientservice.dto.PatientResponseDTO;
import com.dilip.patientservice.exception.EmailAlreadyExistsException;
import com.dilip.patientservice.exception.PatientNotFoundException;
import com.dilip.patientservice.mapper.PatientMapper;
import com.dilip.patientservice.model.Patient;
import com.dilip.patientservice.repository.PatientRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class PatientService {
    private final PatientRepository patientRepository;

    /**
     * Get all patients with pagination
     *
     * @param pageable pagination information
     * @return Page of PatientResponseDTO objects
     */
    public Page<PatientResponseDTO> getPatients(Pageable pageable) {
        log.debug("Fetching patients page: {}", pageable);
        return patientRepository.findAll(pageable).map(PatientMapper::toDTO);
    }

    /**
     * Get all patients (no pagination)
     *
     * @return List of PatientResponseDTO objects
     */
    public List<PatientResponseDTO> getPatients() {
        log.debug("Fetching all patients");
        List<Patient> patients = patientRepository.findAll();
        return patients.stream().map(PatientMapper::toDTO).toList();
    }

    /**
     * Get patient by ID
     *
     * @param id patient ID
     * @return PatientResponseDTO
     * @throws PatientNotFoundException if patient not found
     */
    public PatientResponseDTO getPatientById(UUID id) {
        log.debug("Fetching patient with id: {}", id);
        return patientRepository.findById(id)
                .map(PatientMapper::toDTO)
                .orElseThrow(() -> new PatientNotFoundException("Patient not found with id: " + id));
    }

    /**
     * Create a new patient
     *
     * @param patientRequestDTO patient data
     * @return PatientResponseDTO
     * @throws EmailAlreadyExistsException if email already exists
     */
    @Transactional
    public PatientResponseDTO createPatient(PatientRequestDTO patientRequestDTO) {
        log.debug("Creating new patient with email: {}", patientRequestDTO.email());
        if (patientRepository.existsByEmail(patientRequestDTO.email())) {
            throw new EmailAlreadyExistsException("Patient already exists with email: " + patientRequestDTO.email());
        }

        Patient newPatient = patientRepository.save(PatientMapper.toModel(patientRequestDTO));
        log.info("Created new patient with id: {}", newPatient.getId());
        return PatientMapper.toDTO(newPatient);
    }

    /**
     * Update an existing patient
     *
     * @param id                patient ID
     * @param patientRequestDTO updated patient data
     * @return PatientResponseDTO
     * @throws PatientNotFoundException    if patient not found
     * @throws EmailAlreadyExistsException if new email already exists
     */
    @Transactional
    public PatientResponseDTO updatePatient(UUID id, PatientRequestDTO patientRequestDTO) {
        log.debug("Updating patient with id: {}", id);
        Patient existingPatient = patientRepository.findById(id)
                .orElseThrow(() -> new PatientNotFoundException("Patient not found with id: " + id));

        if (!existingPatient.getEmail().equals(patientRequestDTO.email()) &&
                patientRepository.existsByEmail(patientRequestDTO.email())) {
            throw new EmailAlreadyExistsException("Patient already exists with email: " + patientRequestDTO.email());
        }

        Patient updatedPatient = PatientMapper.updateModel(existingPatient, patientRequestDTO);
        return PatientMapper.toDTO(patientRepository.save(updatedPatient));
    }

    /**
     * Delete a patient by ID
     *
     * @param id patient ID
     * @throws PatientNotFoundException if patient not found
     */
    @Transactional
    public void deletePatient(UUID id) {
        log.debug("Deleting patient with id: {}", id);
        if (!patientRepository.existsById(id)) {
            throw new PatientNotFoundException("Patient not found with id: " + id);
        }
        patientRepository.deleteById(id);
        log.info("Deleted patient with id: {}", id);
    }
}
