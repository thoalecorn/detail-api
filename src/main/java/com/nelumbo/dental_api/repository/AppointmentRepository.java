package com.nelumbo.dental_api.repository;

import com.nelumbo.dental_api.entity.Appointment;
import com.nelumbo.dental_api.enums.AppointmentStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
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

    @Query("""
        SELECT a.patient.name, a.patient.document, COUNT(a) as total
        FROM Appointment a
        WHERE a.status = 'ATENDIDA'
        GROUP BY a.patient.name, a.patient.document
        ORDER BY total DESC
        LIMIT 10
    """)
    List<Object[]> findTop10PatientsNetwork();

    @Query("""
        SELECT a.patient.name, a.patient.document, COUNT(a) as total
        FROM Appointment a
        WHERE a.status = 'ATENDIDA'
        AND a.clinic.id = :clinicId
        GROUP BY a.patient.name, a.patient.document
        ORDER BY total DESC
        LIMIT 10
    """)
    List<Object[]> findTop10PatientsByClinic(@Param("clinicId") Long clinicId);

    @Query("""
        SELECT a.patient.name, a.patient.document
        FROM Appointment a
        WHERE CAST(a.appointmentDatetime AS date) = CAST(:date AS date)
        AND a.status NOT IN ('CANCELADA', 'RECHAZADA', 'INASISTENCIA')
        AND a.patient.id NOT IN (
            SELECT a2.patient.id FROM Appointment a2
            WHERE a2.clinic.id = a.clinic.id
            AND a2.status = 'ATENDIDA'
            AND a2.appointmentDatetime < a.appointmentDatetime
        )
        AND (:clinicId IS NULL OR a.clinic.id = :clinicId)
    """)
    List<Object[]> findFirstTimePatients(
            @Param("date") LocalDateTime date,
            @Param("clinicId") Long clinicId);

    @Query("""
        SELECT COALESCE(SUM(h.amountCharged), 0)
        FROM AppointmentHistory h
        WHERE h.historyEvent = 'ATENDIDA'
        AND h.appointment.clinic.id = :clinicId
        AND h.eventDatetime >= :from
        AND h.eventDatetime <= :to
    """)
    BigDecimal findRevenueBetween(
            @Param("clinicId") Long clinicId,
            @Param("from") LocalDateTime from,
            @Param("to") LocalDateTime to);

    @Query("""
        SELECT a.dentist.name, COUNT(a) as total
        FROM Appointment a
        WHERE a.status = 'ATENDIDA'
        AND MONTH(a.appointmentDatetime) = MONTH(CURRENT_DATE)
        AND YEAR(a.appointmentDatetime) = YEAR(CURRENT_DATE)
        GROUP BY a.dentist.name
        ORDER BY total DESC
        LIMIT 3
    """)
    List<Object[]> findTop3DentistsCurrentMonth();

    @Query("""
        SELECT a.procedure.name, COUNT(a) as total
        FROM Appointment a
        WHERE a.status = 'ATENDIDA'
        AND MONTH(a.appointmentDatetime) = MONTH(CURRENT_DATE)
        AND YEAR(a.appointmentDatetime) = YEAR(CURRENT_DATE)
        GROUP BY a.procedure.name
        ORDER BY total DESC
        LIMIT 3
    """)
    List<Object[]> findTop3ProceduresCurrentMonth();
}