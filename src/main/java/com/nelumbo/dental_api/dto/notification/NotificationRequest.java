package com.nelumbo.dental_api.dto.notification;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class NotificationRequest {
    private String email;
    private String documento;
    private String mensaje;
    private Long clinicaId;
}