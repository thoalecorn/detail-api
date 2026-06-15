package com.nelumbo.dental_api.dto.dentist;

import lombok.AllArgsConstructor;
import lombok.Getter;
import java.util.List;

@Getter
@AllArgsConstructor
public class DentistResponse {
    private Long id;
    private String name;
    private String document;
    private String speciality;
    private List<String> clinics;
}