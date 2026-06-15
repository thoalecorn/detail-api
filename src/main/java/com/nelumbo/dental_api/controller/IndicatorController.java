package com.nelumbo.dental_api.controller;

import com.nelumbo.dental_api.dto.indicator.*;
import com.nelumbo.dental_api.service.IndicatorService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/indicators")
@RequiredArgsConstructor
public class IndicatorController {

    private final IndicatorService indicatorService;

    @GetMapping("/top-patients")
    @PreAuthorize("hasAnyRole('ADMIN', 'RECEPCIONISTA')")
    public ResponseEntity<List<TopPatientDTO>> getTop10PatientsNetwork() {
        return ResponseEntity.ok(indicatorService.getTop10PatientsNetwork());
    }

    @GetMapping("/top-patients/clinic/{clinicId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'RECEPCIONISTA')")
    public ResponseEntity<List<TopPatientDTO>> getTop10PatientsByClinic(
            @PathVariable Long clinicId) {
        return ResponseEntity.ok(
                indicatorService.getTop10PatientsByClinic(clinicId));
    }

    @GetMapping("/first-time-patients")
    @PreAuthorize("hasAnyRole('ADMIN', 'RECEPCIONISTA')")
    public ResponseEntity<List<FirstTimePatientDTO>> getFirstTimePatients(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
            LocalDateTime date,
            @RequestParam(required = false) Long clinicId) {
        return ResponseEntity.ok(
                indicatorService.getFirstTimePatients(date, clinicId));
    }

    @GetMapping("/revenue/{clinicId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'RECEPCIONISTA')")
    public ResponseEntity<RevenueDTO> getRevenue(@PathVariable Long clinicId) {
        return ResponseEntity.ok(indicatorService.getRevenue(clinicId));
    }

    @GetMapping("/top-dentists")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<TopDentistDTO>> getTop3Dentists() {
        return ResponseEntity.ok(indicatorService.getTop3DentistsCurrentMonth());
    }

    @GetMapping("/top-procedures")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<TopProcedureDTO>> getTop3Procedures() {
        return ResponseEntity.ok(indicatorService.getTop3ProceduresCurrentMonth());
    }
}