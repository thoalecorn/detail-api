package com.nelumbo.dental_api.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.nelumbo.dental_api.entity.Office;
import java.util.List;

public interface OfficeRepository extends JpaRepository<Office, Long> {
    List<Office> findByClinicId(Long clinicId);
}
