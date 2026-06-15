package com.nelumbo.dental_api.service;

import com.nelumbo.dental_api.dto.clinic.ClinicRequest;
import com.nelumbo.dental_api.dto.clinic.ClinicResponse;
import com.nelumbo.dental_api.entity.Clinic;
import com.nelumbo.dental_api.repository.ClinicRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class ClinicService {

    private final ClinicRepository clinicRepository;

    @Transactional(readOnly = true)
    public ClinicResponse create(ClinicRequest request) {
        Clinic clinic = Clinic.builder()
                .name(request.getName())
                .address(request.getAddress())
                .city(request.getCity())
                .phone(request.getPhone())
                .build();
        return toResponse(clinicRepository.save(clinic));
    }

    @Transactional(readOnly = true)
    public List<ClinicResponse> findAll() {
        return clinicRepository.findAll()
                .stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    public ClinicResponse findById(Long id) {
        Clinic clinic = clinicRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Clínica no encontrada"));
        return toResponse(clinic);
    }

    @Transactional(readOnly = true)
    public ClinicResponse update(Long id, ClinicRequest request) {
        Clinic clinic = clinicRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Clínica no encontrada"));
        clinic.setName(request.getName());
        clinic.setAddress(request.getAddress());
        clinic.setCity(request.getCity());
        clinic.setPhone(request.getPhone());
        return toResponse(clinicRepository.save(clinic));
    }

    @Transactional(readOnly = true)
    public void delete(Long id) {
        if (!clinicRepository.existsById(id)) {
            throw new RuntimeException("Clínica no encontrada");
        }
        clinicRepository.deleteById(id);
    }

    @Transactional(readOnly = true)
    private ClinicResponse toResponse(Clinic clinic) {
        return new ClinicResponse(
                clinic.getId(),
                clinic.getName(),
                clinic.getAddress(),
                clinic.getCity(),
                clinic.getPhone()
        );
    }
}