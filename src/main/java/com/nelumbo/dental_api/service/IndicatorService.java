package com.nelumbo.dental_api.service;

import com.nelumbo.dental_api.dto.indicator.*;
import com.nelumbo.dental_api.repository.AppointmentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.temporal.TemporalAdjusters;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class IndicatorService {

    private final AppointmentRepository appointmentRepository;

    public List<TopPatientDTO> getTop10PatientsNetwork() {
        return appointmentRepository.findTop10PatientsNetwork()
                .stream()
                .map(row -> new TopPatientDTO(
                        (String) row[0],
                        (String) row[1],
                        (Long) row[2]))
                .collect(Collectors.toList());
    }

    public List<TopPatientDTO> getTop10PatientsByClinic(Long clinicId) {
        return appointmentRepository.findTop10PatientsByClinic(clinicId)
                .stream()
                .map(row -> new TopPatientDTO(
                        (String) row[0],
                        (String) row[1],
                        (Long) row[2]))
                .collect(Collectors.toList());
    }

    public List<FirstTimePatientDTO> getFirstTimePatients(
            LocalDateTime date, Long clinicId) {
        return appointmentRepository.findFirstTimePatients(date, clinicId)
                .stream()
                .map(row -> new FirstTimePatientDTO(
                        (String) row[0],
                        (String) row[1]))
                .collect(Collectors.toList());
    }

    public RevenueDTO getRevenue(Long clinicId) {
        LocalDateTime now = LocalDateTime.now();

        // Hoy
        LocalDateTime startOfDay = now.toLocalDate().atStartOfDay();
        LocalDateTime endOfDay = now.toLocalDate().atTime(23, 59, 59);

        // Esta semana
        LocalDateTime startOfWeek = now.toLocalDate()
                .with(java.time.DayOfWeek.MONDAY).atStartOfDay();

        // Este mes
        LocalDateTime startOfMonth = now.toLocalDate()
                .with(TemporalAdjusters.firstDayOfMonth()).atStartOfDay();

        // Este año
        LocalDateTime startOfYear = now.toLocalDate()
                .with(TemporalAdjusters.firstDayOfYear()).atStartOfDay();

        return new RevenueDTO(
                appointmentRepository.findRevenueBetween(
                        clinicId, startOfDay, endOfDay),
                appointmentRepository.findRevenueBetween(
                        clinicId, startOfWeek, endOfDay),
                appointmentRepository.findRevenueBetween(
                        clinicId, startOfMonth, endOfDay),
                appointmentRepository.findRevenueBetween(
                        clinicId, startOfYear, endOfDay)
        );
    }

    public List<TopDentistDTO> getTop3DentistsCurrentMonth() {
        return appointmentRepository.findTop3DentistsCurrentMonth()
                .stream()
                .map(row -> new TopDentistDTO(
                        (String) row[0],
                        (Long) row[1]))
                .collect(Collectors.toList());
    }

    public List<TopProcedureDTO> getTop3ProceduresCurrentMonth() {
        return appointmentRepository.findTop3ProceduresCurrentMonth()
                .stream()
                .map(row -> new TopProcedureDTO(
                        (String) row[0],
                        (Long) row[1]))
                .collect(Collectors.toList());
    }
}