package com.nelumbo.dental_api.dto.indicator;

import lombok.AllArgsConstructor;
import lombok.Getter;
import java.math.BigDecimal;

@Getter @AllArgsConstructor
public class RevenueDTO {
    private BigDecimal today;
    private BigDecimal week;
    private BigDecimal month;
    private BigDecimal year;
}