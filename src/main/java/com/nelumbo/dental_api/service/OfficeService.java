package com.nelumbo.dental_api.service;

import com.nelumbo.dental_api.dto.office.OfficeRequest;
import com.nelumbo.dental_api.dto.office.OfficeResponse;
import com.nelumbo.dental_api.entity.Clinic;
import com.nelumbo.dental_api.entity.Office;
import com.nelumbo.dental_api.repository.ClinicRepository;
import com.nelumbo.dental_api.repository.OfficeRepository;
import com.nelumbo.dental_api.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OfficeService {

    private final OfficeRepository officeRepository;
    private final ClinicRepository clinicRepository;

    @Transactional
    public OfficeResponse create(OfficeRequest request) {
        Clinic clinic = clinicRepository.findById(request.getClinicId())
                .orElseThrow(() -> new ResourceNotFoundException("Clínica", request.getClinicId()));

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
                .orElseThrow(() -> new ResourceNotFoundException("Consultorio", id));
        return toResponse(office);
    }

    @Transactional(readOnly = true)
    public List<OfficeResponse> findByClinic(Long clinicId) {
        if (!clinicRepository.existsById(clinicId)) {
            throw new ResourceNotFoundException("Clínica", clinicId);
        }
        return officeRepository.findByClinicId(clinicId)
                .stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    @Transactional
    public OfficeResponse update(Long id, OfficeRequest request) {
        Office office = officeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Consultorio", id));

        Clinic clinic = clinicRepository.findById(request.getClinicId())
                .orElseThrow(() -> new ResourceNotFoundException("Clínica", request.getClinicId()));

        office.setName(request.getName());
        office.setCapacity(request.getCapacity());
        office.setClinic(clinic);

        return toResponse(officeRepository.save(office));
    }

    @Transactional
    public void delete(Long id) {
        if (!officeRepository.existsById(id)) {
            throw new ResourceNotFoundException("Consultorio", id);
        }
        officeRepository.deleteById(id);
    }

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