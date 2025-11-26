package com.switchapp.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "invoices")
@Setter
@Getter
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "business_id", nullable = false)
    private Business business;
    @Column(nullable = false)
    private String name;
    private String category;
    @Column(name = "unit_price", precision = 10, scale = 2)
    private BigDecimal unitPrice;
    @Column(name = "cost_price", precision = 10, scale = 2)
    private BigDecimal costPrice;
    @Column(name = "stock_qty")
    private Long stockQty = 0L;
    private String unit;
    private String barCode;
    @Column(name = "created_dt", columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    private LocalDateTime createdDt;
    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<InvoiceItem> invoiceItems;

    @PrePersist
    protected void onCreate() {
        if (createdDt == null) {
            createdDt = LocalDateTime.now();
        }
    }
}

/*
| Column      | Type                        | Description                         |
| ----------- | --------------------------- | ----------------------------------- |
| id          | BIGINT (PK)                 | Product ID                          |
| business_id | BIGINT (FK → businesses.id) | Linked business                     |
| name        | VARCHAR(100)                | Product name                        |
| category    | VARCHAR(50)                 | Category/type                       |
| unit_price  | DECIMAL(10,2)               | Selling price                       |
| cost_price  | DECIMAL(10,2)               | Purchase price                      |
| stock_qty   | INT                         | Available quantity                  |
| unit        | VARCHAR(20)                 | Unit of measurement (e.g., pcs, kg) |
| barcode     | VARCHAR(50)                 | Optional barcode                    |
| created_at  | TIMESTAMP                   | Created timestamp                   |

Relation: businesses (1) → (M) products
*/

