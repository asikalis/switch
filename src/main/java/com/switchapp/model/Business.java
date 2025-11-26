package com.switchapp.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "businesses")
@Setter
@Getter
public class Business {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)  // Foreign key
    @JsonBackReference
    private User user;
    @Column(nullable = false)
    private String name;
    private String type;
    private String address;
    private String phone;
    private String email;
    @Column(name = "gst_number")
    private String gstNumber;
    @Column(name = "logo_url")
    private String logourl;
    @Column(name = "created_dt", columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    private LocalDateTime createdDt;
    @Column(name = "updated_dt")
    private LocalDateTime updatedDt;
    @OneToMany(mappedBy = "business", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Customer> customers;
    @OneToMany(mappedBy = "business", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Product> products;
    @OneToMany(mappedBy = "business", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Invoice> invoices;

    @PrePersist
    protected void onCreate() {
        if (createdDt == null) {
            createdDt = LocalDateTime.now();
        }
    }
}

/*
| Column     | Type                   | Description                            |
| ---------- | ---------------------- | -------------------------------------- |
| id         | BIGINT (PK)            | Business ID                            |
| user_id    | BIGINT (FK → users.id) | Owner of the business                  |
| name       | VARCHAR(100)           | Business name                          |
| type       | VARCHAR(50)            | Type (e.g., Grocery, Textiles, Fruits) |
| address    | VARCHAR(255)           | Business address                       |
| phone      | VARCHAR(20)            | Contact number                         |
| email      | VARCHAR(100)           | Business email                         |
| gst_number | VARCHAR(50)            | Tax registration (optional)            |
| logo_url   | VARCHAR(255)           | Logo path (optional)                   |
| created_dt | TIMESTAMP              | Creation date                          |
| updated_dt | TIMESTAMP              | Last update                            |

Relation: users (1) → (M) businesses
*/
