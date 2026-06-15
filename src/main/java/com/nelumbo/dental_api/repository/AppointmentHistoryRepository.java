package com.nelumbo.dental_api.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.nelumbo.dental_api.entity.AppointmentHistory;
import java.util.List;

public interface AppointmentHistoryRepository 
        extends JpaRepository<AppointmentHistory, Long> {
    List<AppointmentHistory> findByAppointmentId(Long appointmentId);
}
