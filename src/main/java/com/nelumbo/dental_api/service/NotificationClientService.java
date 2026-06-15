package com.nelumbo.dental_api.service;

import com.nelumbo.dental_api.dto.notification.NotificationMicroserviceRequest;
import com.nelumbo.dental_api.dto.notification.NotificationResponse;
import com.nelumbo.dental_api.dto.notification.NotificationRequest;
import com.nelumbo.dental_api.entity.Appointment;
import com.nelumbo.dental_api.enums.AppointmentStatus;
import com.nelumbo.dental_api.exception.BusinessException;
import com.nelumbo.dental_api.repository.AppointmentRepository;
import com.nelumbo.dental_api.repository.ClinicRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class NotificationClientService {

    private final AppointmentRepository appointmentRepository;
    private final ClinicRepository clinicRepository;
    private final RestTemplate restTemplate;

    @Value("${notification.service.url}")
    private String notificationServiceUrl;

    @Transactional
    public NotificationResponse sendNotification(NotificationRequest request) {
        log.info(">>> Buscando citas para clinicaId: {}", request.getClinicaId());
        log.info(">>> Documento: {}", request.getDocumento());

        List<Appointment> appointments = appointmentRepository
                .findByClinicId(request.getClinicaId());

        log.info(">>> Citas encontradas: {}", appointments.size());

        boolean hasAppointment = appointments.stream()
        .anyMatch(a -> a.getPatient().getDocument()
                .equals(request.getDocumento())
                && a.getStatus() == AppointmentStatus.AGENDADA);

        if (!hasAppointment) {
            throw new BusinessException(
                "El paciente no tiene una cita agendada en la clínica indicada");
        }

        String clinicaNombre = clinicRepository.findById(request.getClinicaId())
                .orElseThrow(() -> new BusinessException("Clínica no encontrada"))
                .getName();

        NotificationMicroserviceRequest microserviceRequest =
                new NotificationMicroserviceRequest(
                        request.getEmail(),
                        request.getDocumento(),
                        request.getMensaje(),
                        clinicaNombre
                );

        String url = notificationServiceUrl + "/notifications";
        ResponseEntity<NotificationResponse> response = restTemplate.postForEntity(
                url, microserviceRequest, NotificationResponse.class);

        log.info("Notificación enviada a {} para documento {}",
                request.getEmail(), request.getDocumento());

        return response.getBody();
    }
}