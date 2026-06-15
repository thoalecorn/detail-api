package com.nelumbo.dental_api.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import com.nelumbo.dental_api.entity.UserClinic;
import java.util.List;

public interface UserClinicRepository extends JpaRepository<UserClinic, Long> {
    List<UserClinic> findByUserId(Long userId);

    @Modifying
    @Query("DELETE FROM UserClinic uc WHERE uc.user.id = :userId")
    void deleteByUserId(Long userId);
}