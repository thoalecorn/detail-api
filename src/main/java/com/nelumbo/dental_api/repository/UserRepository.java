package com.nelumbo.dental_api.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.nelumbo.dental_api.entity.User;

public interface UserRepository extends JpaRepository <User, Long> {
    
}
