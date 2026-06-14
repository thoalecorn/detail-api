package com.nelumbo.dental_api.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.nelumbo.dental_api.entity.UserClinic;

public interface UserClinicRepository extends JpaRepository<UserClinic, Long>{
    
}
