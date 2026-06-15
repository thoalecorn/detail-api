package com.nelumbo.dental_api.dto.indicator;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter @AllArgsConstructor
public class TopPatientDTO {
    private String patientName;
    private String document;
    private Long attendedAppointments;
}