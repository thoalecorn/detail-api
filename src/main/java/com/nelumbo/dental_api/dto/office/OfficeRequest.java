package com.nelumbo.dental_api.dto.office;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OfficeRequest {

    @NotBlank(message = "El nombre es obligatorio")
    @Size(max = 127, message = "El nombre no puede superar 127 caracteres")
    private String name;

    @NotNull(message = "La capacidad es obligatoria")
    @Min(value = 1, message = "La capacidad debe ser al menos 1")
    private Integer capacity;

    @NotNull(message = "La clínica es obligatoria")
    private Long clinicId;
}