package com.nelumbo.dental_api.dto.appointment;

import lombok.AllArgsConstructor;
import lombok.Getter;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class AppointmentHistoryResponse {
    private Long id;
    private String event;
    private LocalDateTime eventDatetime;
    private BigDecimal amountCharged;
}