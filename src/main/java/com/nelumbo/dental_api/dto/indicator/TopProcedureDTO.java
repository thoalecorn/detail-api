package com.nelumbo.dental_api.dto.indicator;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter @AllArgsConstructor
public class TopProcedureDTO {
    private String procedureName;
    private Long totalAppointments;
}