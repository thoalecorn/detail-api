package com.nelumbo.dental_api.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.nelumbo.dental_api.entity.DentistClinic;
import java.util.List;

public interface DentistClinicRepository extends JpaRepository<DentistClinic, Long> {
    List<DentistClinic> findByDentistId(Long dentistId);
    
    @Modifying
    @Query("DELETE FROM DentistClinic dc WHERE dc.dentist.id = :dentistId")
    void deleteByDentistId(@Param("dentistId") Long dentistId);
}