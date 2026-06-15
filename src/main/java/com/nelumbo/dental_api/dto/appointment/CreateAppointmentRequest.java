package com.nelumbo.dental_api.dto.appointment;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import java.time.LocalDateTime;

@Getter
@Setter
public class CreateAppointmentRequest {

    @NotBlank(message = "El documento es obligatorio")
    @Size(max = 12, message = "El documento no puede superar 12 caracteres")
    private String document;

    @NotBlank(message = "El nombre del paciente es obligatorio")
    @Size(max = 127, message = "El nombre no puede superar 127 caracteres")
    private String patientName;

    @Email(message = "El email no tiene un formato válido")
    @Size(max = 255, message = "El email no puede superar 255 caracteres")
    private String email;

    @NotNull(message = "El consultorio es obligatorio")
    private Long officeId;

    @NotNull(message = "El dentista es obligatorio")
    private Long dentistId;

    @NotNull(message = "El procedimiento es obligatorio")
    private Long procedureId;

    @NotNull(message = "La fecha y hora de la cita es obligatoria")
    @Future(message = "La fecha de la cita debe ser en el futuro")
    private LocalDateTime appointmentDatetime;
}