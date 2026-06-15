package com.nelumbo.dental_api.dto.indicator;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter @AllArgsConstructor
public class FirstTimePatientDTO {
    private String patientName;
    private String document;
}