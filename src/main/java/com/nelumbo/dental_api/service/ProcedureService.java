package com.nelumbo.dental_api.service;

import com.nelumbo.dental_api.dto.procedure.ProcedureRequest;
import com.nelumbo.dental_api.dto.procedure.ProcedureResponse;
import com.nelumbo.dental_api.entity.Procedure;
import com.nelumbo.dental_api.repository.ProcedureRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class ProcedureService {

    private final ProcedureRepository procedureRepository;

    public ProcedureResponse create(ProcedureRequest request) {
        Procedure procedure = Procedure.builder()
                .name(request.getName())
                .description(request.getDescription())
                .cost(request.getCost())
                .estimatedDuration(request.getEstimatedDuration())
                .build();
        return toResponse(procedureRepository.save(procedure));
    }

    @Transactional(readOnly = true)
    public List<ProcedureResponse> findAll() {
        return procedureRepository.findAll()
                .stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public ProcedureResponse findById(Long id) {
        Procedure procedure = procedureRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Procedimiento no encontrado"));
        return toResponse(procedure);
    }

    public ProcedureResponse update(Long id, ProcedureRequest request) {
        Procedure procedure = procedureRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Procedimiento no encontrado"));
        procedure.setName(request.getName());
        procedure.setDescription(request.getDescription());
        procedure.setCost(request.getCost());
        procedure.setEstimatedDuration(request.getEstimatedDuration());
        return toResponse(procedureRepository.save(procedure));
    }

    public void delete(Long id) {
        if (!procedureRepository.existsById(id)) {
            throw new RuntimeException("Procedimiento no encontrado");
        }
        procedureRepository.deleteById(id);
    }

    private ProcedureResponse toResponse(Procedure procedure) {
        return new ProcedureResponse(
                procedure.getId(),
                procedure.getName(),
                procedure.getDescription(),
                procedure.getCost(),
                procedure.getEstimatedDuration()
        );
    }
}