package com.nelumbo.dental_api.controller;

import com.nelumbo.dental_api.dto.clinic.ClinicRequest;
import com.nelumbo.dental_api.dto.clinic.ClinicResponse;
import com.nelumbo.dental_api.service.ClinicService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/clinics")
@RequiredArgsConstructor
public class ClinicController {

    private final ClinicService clinicService;

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ClinicResponse> create(@Valid @RequestBody ClinicRequest request) {
        return ResponseEntity.status(201).body(clinicService.create(request));
    }
    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'RECEPCIONISTA')")
    public ResponseEntity<List<ClinicResponse>> findAll() {
        return ResponseEntity.ok(clinicService.findAll());
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'RECEPCIONISTA')")
    public ResponseEntity<ClinicResponse> findById(@PathVariable Long id) {
        return ResponseEntity.ok(clinicService.findById(id));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ClinicResponse> update(@PathVariable Long id,
                                                @Valid @RequestBody ClinicRequest request) {
        return ResponseEntity.ok(clinicService.update(id, request));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        clinicService.delete(id);
        return ResponseEntity.noContent().build();
    }
}