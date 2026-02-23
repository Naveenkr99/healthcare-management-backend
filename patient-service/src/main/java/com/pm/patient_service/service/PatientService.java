package com.pm.patient_service.service;

import com.pm.patient_service.dto.PatientRequestDTO;
import com.pm.patient_service.dto.PatientResponseDTO;
import com.pm.patient_service.exception.DuplicateEmailException;
import com.pm.patient_service.mapper.PatientMapper;
import com.pm.patient_service.model.Patient;
import com.pm.patient_service.repository.PatientRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class PatientService {
   private final PatientRepository patientRepository;

     public PatientService(PatientRepository patientRepository) {
        this.patientRepository = patientRepository;
     }

     public List<PatientResponseDTO> getAllPatients() {
         List<Patient> patients = patientRepository.findAll();

         return patients.stream().map(PatientMapper::toPatientResponseDTO).toList();
     }

    public PatientResponseDTO createPatient(PatientRequestDTO patientRequestDTO) {
        // Check if email already exists
        if (patientRepository.existsByEmail(patientRequestDTO.getEmail())) {
            throw new DuplicateEmailException("Email '" + patientRequestDTO.getEmail() + "' already exists. Please use a different email address.");
        }

        Patient newPatient = patientRepository.save(PatientMapper.toModel(patientRequestDTO));
        return PatientMapper.toPatientResponseDTO(newPatient);
    }

    public void deletePatient(UUID id) {
        patientRepository.deleteById(id);
    }
}
