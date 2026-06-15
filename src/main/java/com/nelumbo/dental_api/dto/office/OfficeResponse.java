package com.nelumbo.dental_api.dto.office;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class OfficeResponse {
    private Long id;
    private String name;
    private Integer capacity;
    private Long clinicId;
    private String clinicName;
}