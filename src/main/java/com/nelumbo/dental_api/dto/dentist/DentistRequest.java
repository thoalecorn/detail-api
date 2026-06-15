package com.nelumbo.dental_api.dto.dentist;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import java.util.List;

@Getter
@Setter
public class DentistRequest {

    @NotBlank(message = "El nombre es obligatorio")
    @Size(max = 127, message = "El nombre no puede superar 127 caracteres")
    private String name;

    @NotBlank(message = "El documento es obligatorio")
    @Size(max = 20, message = "El documento no puede superar 20 caracteres")
    private String document;

    @NotBlank(message = "La especialidad es obligatoria")
    @Size(max = 100, message = "La especialidad no puede superar 100 caracteres")
    private String speciality;

    @NotEmpty(message = "Debe asociar al menos una clínica")
    private List<Long> clinicIds;
}