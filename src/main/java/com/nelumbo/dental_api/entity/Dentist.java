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
@Table(name = "dentists")

public class Dentist {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_dentist")
    private Long id;

    @Column(name = "name", nullable = false, length = 127)
    private String name;

    @Column(name = "document", nullable = false, length = 20)
    private String document;

    @Column(name = "speciality", nullable = false, length = 100)
    private String speciality;

    @Column(name = "creadet_at", nullable = false)
    private LocalDateTime creadetAt;

}   


