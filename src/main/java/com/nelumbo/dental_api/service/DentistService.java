package com.nelumbo.dental_api.service;

import com.nelumbo.dental_api.dto.dentist.DentistRequest;
import com.nelumbo.dental_api.dto.dentist.DentistResponse;
import com.nelumbo.dental_api.entity.Clinic;
import com.nelumbo.dental_api.entity.Dentist;
import com.nelumbo.dental_api.entity.DentistClinic;
import com.nelumbo.dental_api.repository.ClinicRepository;
import com.nelumbo.dental_api.repository.DentistClinicRepository;
import com.nelumbo.dental_api.repository.DentistRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class DentistService {

    private final DentistRepository dentistRepository;
    private final DentistClinicRepository dentistClinicRepository;
    private final ClinicRepository clinicRepository;

    public DentistResponse create(DentistRequest request) {
        Dentist dentist = Dentist.builder()
                .name(request.getName())
                .document(request.getDocument())
                .speciality(request.getSpeciality())
                .createdAt(LocalDateTime.now())
                .build();

        dentistRepository.save(dentist);
        associateClinics(dentist, request.getClinicIds());
        return toResponse(dentist);
    }

    @Transactional(readOnly = true)
    public List<DentistResponse> findAll() {
        return dentistRepository.findAll()
                .stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public DentistResponse findById(Long id) {
        Dentist dentist = dentistRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Odontólogo no encontrado"));
        return toResponse(dentist);
    }

    public DentistResponse update(Long id, DentistRequest request) {
        Dentist dentist = dentistRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Odontólogo no encontrado"));

        dentist.setName(request.getName());
        dentist.setDocument(request.getDocument());
        dentist.setSpeciality(request.getSpeciality());
        dentistRepository.save(dentist);

        dentistClinicRepository.deleteByDentistId(id);
        associateClinics(dentist, request.getClinicIds());

        return toResponse(dentist);
    }

    public void delete(Long id) {
        if (!dentistRepository.existsById(id)) {
            throw new RuntimeException("Odontólogo no encontrado");
        }
        dentistClinicRepository.deleteByDentistId(id);
        dentistRepository.deleteById(id);
    }

    public DentistResponse associateClinics(Long dentistId, List<Long> clinicIds) {
        Dentist dentist = dentistRepository.findById(dentistId)
                .orElseThrow(() -> new RuntimeException("Odontólogo no encontrado"));
        dentistClinicRepository.deleteByDentistId(dentistId);
        associateClinics(dentist, clinicIds);
        return toResponse(dentist);
    }

    private void associateClinics(Dentist dentist, List<Long> clinicIds) {
        if (clinicIds == null || clinicIds.isEmpty()) return;
        for (Long clinicId : clinicIds) {
            Clinic clinic = clinicRepository.findById(clinicId)
                    .orElseThrow(() -> new RuntimeException(
                            "Clínica no encontrada: " + clinicId));
            DentistClinic dentistClinic = DentistClinic.builder()
                    .dentist(dentist)
                    .clinic(clinic)
                    .build();
            dentistClinicRepository.save(dentistClinic);
        }
    }

    private DentistResponse toResponse(Dentist dentist) {
        List<String> clinics = dentistClinicRepository.findByDentistId(dentist.getId())
                .stream()
                .map(dc -> dc.getClinic().getName())
                .collect(Collectors.toList());

        return new DentistResponse(
                dentist.getId(),
                dentist.getName(),
                dentist.getDocument(),
                dentist.getSpeciality(),
                clinics
        );
    }
}