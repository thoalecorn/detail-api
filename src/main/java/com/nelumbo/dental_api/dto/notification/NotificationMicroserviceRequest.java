package com.nelumbo.dental_api.dto.notification;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class NotificationMicroserviceRequest {
    private String email;
    private String documento;
    private String mensaje;
    private String clinicaNombre;
}