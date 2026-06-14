package com.nelumbo.dental_api.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.nelumbo.dental_api.entity.Dentist;

public interface DentistRepository extends JpaRepository<Dentist, Long>{
}
