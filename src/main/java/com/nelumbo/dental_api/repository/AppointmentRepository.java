package com.nelumbo.dental_api.repository;

import com.nelumbo.dental_api.entity.Appointment;
import com.nelumbo.dental_api.enums.AppointmentStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface AppointmentRepository extends JpaRepository<Appointment, Long> {

    @Query("""
        SELECT COUNT(a) > 0 FROM Appointment a
        WHERE a.patient.document = :document
        AND CAST(a.appointmentDatetime AS date) = CAST(:date AS date)
        AND a.status IN ('AGENDADA', 'EN_CURSO', 'PENDIENTE_APROBACION')
    """)
    boolean existsActiveAppointmentForPatientOnDate(
            @Param("document") String document,
            @Param("date") LocalDateTime date);

    @Query("""
        SELECT a FROM Appointment a
        JOIN FETCH a.procedure p
        WHERE a.dentist.id = :dentistId
        AND a.status IN ('AGENDADA', 'EN_CURSO', 'PENDIENTE_APROBACION')
        AND a.appointmentDatetime < :endTime
    """)
    List<Appointment> findPotentialDentistOverlaps(
            @Param("dentistId") Long dentistId,
            @Param("endTime") LocalDateTime endTime);

    @Query("""
        SELECT a FROM Appointment a
        JOIN FETCH a.procedure p
        WHERE a.office.id = :officeId
        AND a.status IN ('AGENDADA', 'EN_CURSO', 'PENDIENTE_APROBACION')
        AND a.appointmentDatetime < :endTime
    """)
    List<Appointment> findPotentialOfficeOverlaps(
            @Param("officeId") Long officeId,
            @Param("endTime") LocalDateTime endTime);

    @Query("""
        SELECT a FROM Appointment a
        WHERE (:clinicId IS NULL OR a.clinic.id = :clinicId)
        AND (:officeId IS NULL OR a.office.id = :officeId)
        AND CAST(a.appointmentDatetime AS date) = CAST(:date AS date)
        AND a.status NOT IN ('CANCELADA', 'RECHAZADA')
    """)
    List<Appointment> findDailyAppointments(
            @Param("clinicId") Long clinicId,
            @Param("officeId") Long officeId,
            @Param("date") LocalDateTime date);

    @Query("""
        SELECT a FROM Appointment a
        WHERE a.patient.document LIKE %:document%
    """)
    List<Appointment> searchByDocumentPartial(@Param("document") String document);

    @Query("""
        SELECT COUNT(a) FROM Appointment a
        WHERE a.patient.document = :document
        AND a.status = 'INASISTENCIA'
        AND a.appointmentDatetime >= :since
    """)
    long countNoShowsSince(
            @Param("document") String document,
            @Param("since") LocalDateTime since);

    List<Appointment> findByClinicIdAndStatus(Long clinicId, AppointmentStatus status);

    List<Appointment> findByClinicId(Long clinicId);
}