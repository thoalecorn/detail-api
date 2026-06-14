package com.nelumbo.dental_api.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.nelumbo.dental_api.entity.Clinic;

public interface ClinicRepository extends JpaRepository<Clinic, Long> {
}
