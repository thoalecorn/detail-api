package com.nelumbo.dental_api.entity;


import java.time.LocalDateTime;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "patients")
public class Patient {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_patients")
    private Long id;

    @Column(name = "name", nullable = false, length = 127)
    private String name;

    @Column(name = "document", nullable = false, length = 12)
    private String document;

    @Column(name = "email", nullable = false, unique = true, length = 255)
    private String email;

    @Column(name = "blocked_until")
    private LocalDateTime blockedUntil;

    @Column(name = "creadet_at", nullable = false)
    private LocalDateTime createdAt;
    
    @Column(name = "update_at")
    private LocalDateTime updateAt;
}
