package com.nelumbo.dental_api.dto.appointment;

import lombok.AllArgsConstructor;
import lombok.Getter;
import java.time.LocalDateTime;

@Getter @AllArgsConstructor
public class AppointmentResponse {
    private Long id;
    private String patientName;
    private String patientDocument;
    private String dentistName;
    private String procedureName;
    private LocalDateTime appointmentDatetime;
    private String status;
}