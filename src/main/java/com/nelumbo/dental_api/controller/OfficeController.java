package com.nelumbo.dental_api.controller;

import com.nelumbo.dental_api.dto.office.OfficeRequest;
import com.nelumbo.dental_api.dto.office.OfficeResponse;
import com.nelumbo.dental_api.service.OfficeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/offices")
@RequiredArgsConstructor
public class OfficeController {

    private final OfficeService officeService;

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<OfficeResponse> create(@Valid @RequestBody OfficeRequest request) {
        return ResponseEntity.status(201).body(officeService.create(request));
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'RECEPCIONISTA')")
    public ResponseEntity<List<OfficeResponse>> findAll() {
        return ResponseEntity.ok(officeService.findAll());
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'RECEPCIONISTA')")
    public ResponseEntity<OfficeResponse> findById(@PathVariable Long id) {
        return ResponseEntity.ok(officeService.findById(id));
    }

    @GetMapping("/clinic/{clinicId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'RECEPCIONISTA')")
    public ResponseEntity<List<OfficeResponse>> findByClinic(@PathVariable Long clinicId) {
        return ResponseEntity.ok(officeService.findByClinic(clinicId));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<OfficeResponse> update(@PathVariable Long id,
                                                @Valid @RequestBody OfficeRequest request) {
        return ResponseEntity.ok(officeService.update(id, request));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        officeService.delete(id);
        return ResponseEntity.noContent().build();
    }
}