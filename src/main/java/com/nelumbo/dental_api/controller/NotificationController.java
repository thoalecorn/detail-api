package com.nelumbo.dental_api.controller;

import com.nelumbo.dental_api.dto.notification.NotificationRequest;
import com.nelumbo.dental_api.dto.notification.NotificationResponse;
import com.nelumbo.dental_api.service.NotificationClientService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/notifications")
@RequiredArgsConstructor
public class NotificationController {

    private final NotificationClientService notificationClientService;

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<NotificationResponse> send(
            @RequestBody NotificationRequest request) {
        return ResponseEntity.ok(
                notificationClientService.sendNotification(request));
    }
}