package com.switchapp.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "customers")
@Setter
@Getter
public class Customer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "business_id", nullable = false)  // foreign key column
    private Business business;
    @Column(nullable = false)
    private String name;
    private String phone;
    private String email;
    private String address;
    @Column(name = "created_dt", columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    private LocalDateTime createdDt;
    @OneToMany(mappedBy = "customer", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Invoice> invoices;

    @PrePersist
    protected void onCreate() {
        if (createdDt == null) {
            createdDt = LocalDateTime.now();
        }
    }

}

/*
| Column      | Type                        | Description       |
| ----------- | --------------------------- | ----------------- |
| id          | BIGINT (PK)                 | Customer ID       |
| business_id | BIGINT (FK → businesses.id) | Linked business   |
| name        | VARCHAR(100)                | Customer name     |
| phone       | VARCHAR(20)                 | Customer phone    |
| email       | VARCHAR(100)                | Customer email    |
| address     | VARCHAR(255)                | Customer address  |
| created_dt  | TIMESTAMP                   | Created timestamp |

Relation: businesses (1) → (M) customers
*/
