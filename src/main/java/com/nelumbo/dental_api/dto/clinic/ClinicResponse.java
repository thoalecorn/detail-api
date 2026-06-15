package com.nelumbo.dental_api.dto.clinic;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ClinicResponse {
    private Long id;
    private String name;
    private String address;
    private String city;
    private String phone;
}