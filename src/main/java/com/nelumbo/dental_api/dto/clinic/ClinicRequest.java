package com.nelumbo.dental_api.dto.clinic;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ClinicRequest {

    @NotBlank(message = "El nombre es obligatorio")
    @Size(max = 127, message = "El nombre no puede superar 127 caracteres")
    private String name;

    @NotBlank(message = "La dirección es obligatoria")
    @Size(max = 255, message = "La dirección no puede superar 255 caracteres")
    private String address;

    @NotBlank(message = "La ciudad es obligatoria")
    @Size(max = 255, message = "La ciudad no puede superar 255 caracteres")
    private String city;

    @NotBlank(message = "El teléfono es obligatorio")
    @Size(max = 20, message = "El teléfono no puede superar 20 caracteres")
    @Pattern(regexp = "^[+0-9\\s\\-()]+$", message = "El teléfono solo puede contener números, espacios, guiones y paréntesis")
    private String phone;
}