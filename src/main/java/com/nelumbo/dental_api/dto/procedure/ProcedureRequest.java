package com.nelumbo.dental_api.dto.procedure;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import java.math.BigDecimal;

@Getter
@Setter
public class ProcedureRequest {

    @NotBlank(message = "El nombre es obligatorio")
    @Size(max = 127, message = "El nombre no puede superar 127 caracteres")
    private String name;

    @NotBlank(message = "La descripción es obligatoria")
    @Size(max = 255, message = "La descripción no puede superar 255 caracteres")
    private String description;

    @NotNull(message = "El costo es obligatorio")
    @DecimalMin(value = "0.01", message = "El costo debe ser mayor a 0")
    private BigDecimal cost;

    @NotNull(message = "La duración estimada es obligatoria")
    @Min(value = 1, message = "La duración debe ser al menos 1 minuto")
    private Integer estimatedDuration;
}