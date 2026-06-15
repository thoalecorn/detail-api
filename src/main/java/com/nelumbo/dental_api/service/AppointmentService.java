package com.nelumbo.dental_api.service;

import com.nelumbo.dental_api.dto.appointment.*;
import com.nelumbo.dental_api.entity.*;
import com.nelumbo.dental_api.enums.AppointmentStatus;
import com.nelumbo.dental_api.enums.HistoryEvent;
import com.nelumbo.dental_api.exception.BusinessException;
import com.nelumbo.dental_api.exception.ResourceNotFoundException;
import com.nelumbo.dental_api.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class AppointmentService {

    private final AppointmentRepository appointmentRepository;
    private final PatientRepository patientRepository;
    private final OfficeRepository officeRepository;
    private final DentistRepository dentistRepository;
    private final ProcedureRepository procedureRepository;
    private final AppointmentHistoryRepository appointmentHistoryRepository;

    public CreateAppointmentResponse schedule(CreateAppointmentRequest request) {

        if (!request.getDocument().matches("\\d{6,12}")) {
            throw new BusinessException(
                "El documento debe tener entre 6 y 12 dígitos numéricos");
        }

        if (!request.getAppointmentDatetime().isAfter(LocalDateTime.now())) {
            throw new BusinessException(
                "La fecha de la cita debe ser posterior a la fecha actual");
        }

        Patient patient = patientRepository.findByDocument(request.getDocument())
                .orElseGet(() -> {
                    Patient p = Patient.builder()
                            .name(request.getPatientName())
                            .document(request.getDocument())
                            .email(request.getEmail())
                            .createdAt(LocalDateTime.now())
                            .build();
                    return patientRepository.save(p);
                });

        if (patient.getBlockedUntil() != null &&
                patient.getBlockedUntil().isAfter(LocalDateTime.now())) {
            throw new BusinessException(
                "No se puede Agendar Cita, el paciente se encuentra bloqueado " +
                "por inasistencias reiteradas");
        }

        if (appointmentRepository.existsActiveAppointmentForPatientOnDate(
                request.getDocument(), request.getAppointmentDatetime())) {
            throw new BusinessException(
                "No se puede Agendar Cita, ya existe una cita pendiente para " +
                "este paciente en esta u otra clínica el mismo día");
        }

        Office office = officeRepository.findById(request.getOfficeId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Consultorio", request.getOfficeId()));
        Dentist dentist = dentistRepository.findById(request.getDentistId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Odontólogo", request.getDentistId()));
        Procedure procedure = procedureRepository.findById(request.getProcedureId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Procedimiento", request.getProcedureId()));

        LocalDateTime startTime = request.getAppointmentDatetime();
        LocalDateTime endTime = startTime.plusMinutes(procedure.getEstimatedDuration());

        List<Appointment> dentistAppointments = appointmentRepository
                .findPotentialDentistOverlaps(dentist.getId(), endTime);

        boolean dentistOverlap = dentistAppointments.stream().anyMatch(existing -> {
            LocalDateTime existingEnd = existing.getAppointmentDatetime()
                    .plusMinutes(existing.getProcedure().getEstimatedDuration());
            return existing.getAppointmentDatetime().isBefore(endTime)
                    && existingEnd.isAfter(startTime);
        });

        if (dentistOverlap) {
            throw new BusinessException(
                "No se puede Agendar Cita, el odontólogo o el consultorio " +
                "no se encuentran disponibles en el horario solicitado");
        }

        List<Appointment> officeAppointments = appointmentRepository
                .findPotentialOfficeOverlaps(office.getId(), endTime);

        long currentCount = officeAppointments.stream().filter(existing -> {
            LocalDateTime existingEnd = existing.getAppointmentDatetime()
                    .plusMinutes(existing.getProcedure().getEstimatedDuration());
            return existing.getAppointmentDatetime().isBefore(endTime)
                    && existingEnd.isAfter(startTime);
        }).count();

        if (currentCount >= office.getCapacity()) {
            throw new BusinessException(
                "No se puede Agendar Cita, el odontólogo o el consultorio " +
                "no se encuentran disponibles en el horario solicitado");
        }

        Appointment appointment = Appointment.builder()
                .patient(patient)
                .clinic(office.getClinic())
                .office(office)
                .dentist(dentist)
                .procedure(procedure)
                .appointmentDatetime(startTime)
                .status(AppointmentStatus.AGENDADA)
                .procedureCost(procedure.getCost())
                .createdAt(LocalDateTime.now())
                .build();

        return new CreateAppointmentResponse(
                appointmentRepository.save(appointment).getId());
    }

    public void checkin(Long appointmentId) {
        Appointment appointment = appointmentRepository.findById(appointmentId)
                .orElseThrow(() -> new ResourceNotFoundException("Cita", appointmentId));

        if (appointment.getStatus() != AppointmentStatus.AGENDADA) {
            throw new BusinessException("La cita no está en estado AGENDADA");
        }

        appointment.setStatus(AppointmentStatus.EN_CURSO);
        appointment.setCheckIn(LocalDateTime.now());
        appointmentRepository.save(appointment);
    }

    public AttendAppointmentResponse attend(Long appointmentId) {
        Appointment appointment = appointmentRepository.findById(appointmentId)
                .orElseThrow(() -> new ResourceNotFoundException("Cita", appointmentId));

        if (appointment.getStatus() != AppointmentStatus.EN_CURSO &&
                appointment.getStatus() != AppointmentStatus.AGENDADA) {
            throw new BusinessException(
                "No se puede Registrar Atención, no existe una cita activa " +
                "para este paciente");
        }

        if (appointment.getCheckIn() == null) {
            throw new BusinessException(
                "El paciente debe hacer check-in antes de registrar la atención");
        }

        appointment.setStatus(AppointmentStatus.ATENDIDA);
        appointment.setStartedAt(LocalDateTime.now());
        appointment.setFinishedAt(LocalDateTime.now());
        appointmentRepository.save(appointment);

        saveHistory(appointment, HistoryEvent.ATENDIDA, appointment.getProcedureCost());

        return new AttendAppointmentResponse("Atención registrada",
                appointment.getProcedureCost());
    }

    public CancelAppointmentResponse cancel(Long appointmentId) {
        Appointment appointment = appointmentRepository.findById(appointmentId)
                .orElseThrow(() -> new ResourceNotFoundException("Cita", appointmentId));

        if (appointment.getStatus() != AppointmentStatus.AGENDADA &&
                appointment.getStatus() != AppointmentStatus.EN_CURSO) {
            throw new BusinessException(
                "La cita no puede ser cancelada en su estado actual");
        }

        BigDecimal cancellationFee = BigDecimal.ZERO;
        LocalDateTime now = LocalDateTime.now();

        if (appointment.getAppointmentDatetime().minusHours(24).isBefore(now)) {
            cancellationFee = appointment.getProcedureCost()
                    .multiply(BigDecimal.valueOf(0.30));
            saveHistory(appointment, HistoryEvent.CARGO_POR_CANCELACION_TARDIA,
                    cancellationFee);
        }

        appointment.setStatus(AppointmentStatus.CANCELADA);
        appointmentRepository.save(appointment);
        saveHistory(appointment, HistoryEvent.CANCELADA, BigDecimal.ZERO);

        return new CancelAppointmentResponse("Cita cancelada", cancellationFee);
    }

    public void noShow(Long appointmentId) {
        Appointment appointment = appointmentRepository.findById(appointmentId)
                .orElseThrow(() -> new ResourceNotFoundException("Cita", appointmentId));

        if (appointment.getStatus() != AppointmentStatus.AGENDADA) {
            throw new BusinessException("La cita no está en estado AGENDADA");
        }

        appointment.setStatus(AppointmentStatus.INASISTENCIA);
        appointmentRepository.save(appointment);
        saveHistory(appointment, HistoryEvent.INASISTENCIA, BigDecimal.ZERO);

        LocalDateTime since = LocalDateTime.now().minusDays(90);
        long noShows = appointmentRepository.countNoShowsSince(
                appointment.getPatient().getDocument(), since);

        if (noShows >= 3) {
            Patient patient = appointment.getPatient();
            patient.setBlockedUntil(LocalDateTime.now().plusDays(30));
            patientRepository.save(patient);
        }
    }

    @Transactional(readOnly = true)
    public List<AppointmentHistoryResponse> getHistory(Long appointmentId) {
        return appointmentHistoryRepository.findByAppointmentId(appointmentId)
                .stream()
                .map(h -> new AppointmentHistoryResponse(
                        h.getId(),
                        h.getHistoryEvent().name(),
                        h.getEventDatetime(),
                        h.getAmountCharged()
                ))
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<AppointmentResponse> getDailyAppointments(
            Long clinicId, Long officeId, LocalDateTime date) {
        return appointmentRepository.findDailyAppointments(clinicId, officeId, date)
                .stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<AppointmentResponse> searchByDocument(String document) {
        return appointmentRepository.searchByDocumentPartial(document)
                .stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<AppointmentResponse> findAll(Long clinicId, AppointmentStatus status) {
        List<Appointment> appointments;

        if (clinicId != null && status != null) {
                appointments = appointmentRepository.findByClinicIdAndStatus(clinicId, status);
        } else if (clinicId != null) {
                appointments = appointmentRepository.findByClinicId(clinicId);
        } else {
                appointments = appointmentRepository.findAll();
        }

        return appointments.stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    private void saveHistory(Appointment appointment, HistoryEvent event,
                                BigDecimal amount) {
        AppointmentHistory history = AppointmentHistory.builder()
                .appointment(appointment)
                .historyEvent(event)
                .eventDatetime(LocalDateTime.now())
                .amountCharged(amount)
                .build();
        appointmentHistoryRepository.save(history);
    }

    private AppointmentResponse toResponse(Appointment a) {
        return new AppointmentResponse(
                a.getId(),
                a.getPatient().getName(),
                a.getPatient().getDocument(),
                a.getDentist().getName(),
                a.getProcedure().getName(),
                a.getAppointmentDatetime(),
                a.getStatus().name()
        );
    }
}