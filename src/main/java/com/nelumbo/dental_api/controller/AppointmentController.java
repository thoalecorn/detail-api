package com.nelumbo.dental_api.controller;

import com.nelumbo.dental_api.dto.appointment.*;
import com.nelumbo.dental_api.enums.AppointmentStatus;
import com.nelumbo.dental_api.service.AppointmentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/appointments")
@RequiredArgsConstructor
public class AppointmentController {

    private final AppointmentService appointmentService;

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'RECEPCIONISTA')")
    public ResponseEntity<CreateAppointmentResponse> schedule(
            @Valid @RequestBody CreateAppointmentRequest request) {
        return ResponseEntity.status(201).body(appointmentService.schedule(request));
    }

    @PostMapping("/{id}/checkin")
    @PreAuthorize("hasAnyRole('ADMIN', 'RECEPCIONISTA')")
    public ResponseEntity<Void> checkin(@PathVariable Long id) {
        appointmentService.checkin(id);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/{id}/attend")
    @PreAuthorize("hasAnyRole('ADMIN', 'RECEPCIONISTA')")
    public ResponseEntity<AttendAppointmentResponse> attend(@PathVariable Long id) {
        return ResponseEntity.ok(appointmentService.attend(id));
    }

    @PostMapping("/{id}/cancel")
    @PreAuthorize("hasAnyRole('ADMIN', 'RECEPCIONISTA')")
    public ResponseEntity<CancelAppointmentResponse> cancel(@PathVariable Long id) {
        return ResponseEntity.ok(appointmentService.cancel(id));
    }

    @PostMapping("/{id}/no-show")
    @PreAuthorize("hasAnyRole('ADMIN', 'RECEPCIONISTA')")
    public ResponseEntity<Void> noShow(@PathVariable Long id) {
        appointmentService.noShow(id);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/daily")
    @PreAuthorize("hasAnyRole('ADMIN', 'RECEPCIONISTA')")
    public ResponseEntity<List<AppointmentResponse>> daily(
            @RequestParam(required = false) Long clinicId,
            @RequestParam(required = false) Long officeId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
            LocalDateTime date) {
        return ResponseEntity.ok(
                appointmentService.getDailyAppointments(clinicId, officeId, date));
    }

    @GetMapping("/search")
    @PreAuthorize("hasAnyRole('ADMIN', 'RECEPCIONISTA')")
    public ResponseEntity<List<AppointmentResponse>> search(
            @RequestParam String document) {
        return ResponseEntity.ok(appointmentService.searchByDocument(document));
    }

    @GetMapping("/{id}/history")
    @PreAuthorize("hasAnyRole('ADMIN', 'RECEPCIONISTA')")
    public ResponseEntity<List<AppointmentHistoryResponse>> getHistory(
            @PathVariable Long id) {
        return ResponseEntity.ok(appointmentService.getHistory(id));
    }
    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'RECEPCIONISTA')")
    public ResponseEntity<List<AppointmentResponse>> findAll(
            @RequestParam(required = false) Long clinicId,
            @RequestParam(required = false) AppointmentStatus status) {
        return ResponseEntity.ok(appointmentService.findAll(clinicId, status));
    }
}