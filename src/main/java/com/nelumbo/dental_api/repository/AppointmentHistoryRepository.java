package com.nelumbo.dental_api.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.nelumbo.dental_api.entity.AppointmentHistory;

public interface AppointmentHistoryRepository extends JpaRepository<AppointmentHistory, Long>{
    
}
