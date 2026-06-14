package com.nelumbo.dental_api.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.nelumbo.dental_api.entity.DentistClinic;

public interface DentistClinicRepository extends JpaRepository<DentistClinic, Long>{
}
