package com.nelumbo.dental_api.dto.indicator;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter @AllArgsConstructor
public class TopDentistDTO {
    private String dentistName;
    private Long attendedAppointments;
}