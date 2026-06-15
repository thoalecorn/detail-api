package com.nelumbo.dental_api.controller;

import com.nelumbo.dental_api.dto.dentist.DentistRequest;
import com.nelumbo.dental_api.dto.dentist.DentistResponse;
import com.nelumbo.dental_api.service.DentistService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/dentists")
@RequiredArgsConstructor
public class DentistController {

    private final DentistService dentistService;

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<DentistResponse> create(@RequestBody DentistRequest request) {
        return ResponseEntity.status(201).body(dentistService.create(request));
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'RECEPCIONISTA')")
    public ResponseEntity<List<DentistResponse>> findAll() {
        return ResponseEntity.ok(dentistService.findAll());
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'RECEPCIONISTA')")
    public ResponseEntity<DentistResponse> findById(@PathVariable Long id) {
        return ResponseEntity.ok(dentistService.findById(id));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<DentistResponse> update(@PathVariable Long id,
                                                    @RequestBody DentistRequest request) {
        return ResponseEntity.ok(dentistService.update(id, request));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        dentistService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{id}/clinics")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<DentistResponse> associateClinics(@PathVariable Long id,
                                                                @RequestBody List<Long> clinicIds) {
        return ResponseEntity.ok(dentistService.associateClinics(id, clinicIds));
    }
}