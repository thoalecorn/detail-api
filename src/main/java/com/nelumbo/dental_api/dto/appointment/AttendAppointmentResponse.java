package com.nelumbo.dental_api.dto.appointment;

import lombok.AllArgsConstructor;
import lombok.Getter;
import java.math.BigDecimal;

@Getter @AllArgsConstructor
public class AttendAppointmentResponse {
    private String message;
    private BigDecimal totalCharged;
}