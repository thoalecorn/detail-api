package com.nelumbo.dental_api.dto.procedure;

import lombok.AllArgsConstructor;
import lombok.Getter;
import java.math.BigDecimal;

@Getter
@AllArgsConstructor
public class ProcedureResponse {
    private Long id;
    private String name;
    private String description;
    private BigDecimal cost;
    private Integer estimatedDuration;
}