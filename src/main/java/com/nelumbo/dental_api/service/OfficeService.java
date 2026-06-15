package com.nelumbo.dental_api.service;

import com.nelumbo.dental_api.dto.office.OfficeRequest;
import com.nelumbo.dental_api.dto.office.OfficeResponse;
import com.nelumbo.dental_api.entity.Clinic;
import com.nelumbo.dental_api.entity.Office;
import com.nelumbo.dental_api.repository.ClinicRepository;
import com.nelumbo.dental_api.repository.OfficeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class OfficeService {

    private final OfficeRepository officeRepository;
    private final ClinicRepository clinicRepository;

    @Transactional(readOnly = true)
    public OfficeResponse create(OfficeRequest request) {
        Clinic clinic = clinicRepository.findById(request.getClinicId())
                .orElseThrow(() -> new RuntimeException("Clínica no encontrada"));

        Office office = Office.builder()
                .name(request.getName())
                .capacity(request.getCapacity())
                .clinic(clinic)
                .build();

        return toResponse(officeRepository.save(office));
    }

    @Transactional(readOnly = true)
    public List<OfficeResponse> findAll() {
        return officeRepository.findAll()
                .stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public OfficeResponse findById(Long id) {
        Office office = officeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Consultorio no encontrado"));
        return toResponse(office);
    }

    @Transactional(readOnly = true)
    public List<OfficeResponse> findByClinic(Long clinicId) {
        if (!clinicRepository.existsById(clinicId)) {
            throw new RuntimeException("Clínica no encontrada");
        }
        return officeRepository.findByClinicId(clinicId)
                .stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public OfficeResponse update(Long id, OfficeRequest request) {
        Office office = officeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Consultorio no encontrado"));

        Clinic clinic = clinicRepository.findById(request.getClinicId())
                .orElseThrow(() -> new RuntimeException("Clínica no encontrada"));

        office.setName(request.getName());
        office.setCapacity(request.getCapacity());
        office.setClinic(clinic);

        return toResponse(officeRepository.save(office));
    }

    @Transactional(readOnly = true)
    public void delete(Long id) {
        if (!officeRepository.existsById(id)) {
            throw new RuntimeException("Consultorio no encontrado");
        }
        officeRepository.deleteById(id);
    }

    @Transactional(readOnly = true)
    private OfficeResponse toResponse(Office office) {
        return new OfficeResponse(
                office.getId(),
                office.getName(),
                office.getCapacity(),
                office.getClinic().getId(),
                office.getClinic().getName()
        );
    }
}