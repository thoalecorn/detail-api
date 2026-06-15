package com.nelumbo.dental_api.controller;

import com.nelumbo.dental_api.dto.procedure.ProcedureRequest;
import com.nelumbo.dental_api.dto.procedure.ProcedureResponse;
import com.nelumbo.dental_api.service.ProcedureService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/procedures")
@RequiredArgsConstructor
public class ProcedureController {

    private final ProcedureService procedureService;

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ProcedureResponse> create(@RequestBody ProcedureRequest request) {
        return ResponseEntity.status(201).body(procedureService.create(request));
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'RECEPCIONISTA')")
    public ResponseEntity<List<ProcedureResponse>> findAll() {
        return ResponseEntity.ok(procedureService.findAll());
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'RECEPCIONISTA')")
    public ResponseEntity<ProcedureResponse> findById(@PathVariable Long id) {
        return ResponseEntity.ok(procedureService.findById(id));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ProcedureResponse> update(@PathVariable Long id,
                                                        @RequestBody ProcedureRequest request) {
        return ResponseEntity.ok(procedureService.update(id, request));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        procedureService.delete(id);
        return ResponseEntity.noContent().build();
    }
}