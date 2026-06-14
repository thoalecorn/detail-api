package com.nelumbo.dental_api.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.nelumbo.dental_api.entity.Patient;

public interface PatientRepository extends JpaRepository<Patient, Long>{
    
}
