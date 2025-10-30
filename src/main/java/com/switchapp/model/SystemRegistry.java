package com.switchapp.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "system_registry")
@Setter
@Getter
public class SystemRegistry {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String description;

    @Column(name = "system_key", unique = true, nullable = false)
    private String systemKey;

    @Column(name = "system_value", length = 6000)
    private String systemValue;
}

