package com.switchapp.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "statistics")
@Setter
@Getter
public class Statistics {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "business_id", nullable = false)
    private Business business;
    @Column(nullable = false)
    private LocalDateTime dateTime;
    @Column(name = "total_sales", precision = 10, scale = 2)
    private BigDecimal totalSales = BigDecimal.ZERO;
    @Column(name = "total_invoices")
    private Integer totalInvoices = 0;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "top_product_id")
    private Product topProduct;
}
/*
| Column         | Type                        | Description          |
| -------------- | --------------------------- | -------------------- |
| id             | BIGINT (PK)                 | Record ID            |
| business_id    | BIGINT (FK → businesses.id) | Linked business      |
| date           | DATE                        | Day of statistics    |
| total_sales    | DECIMAL(10,2)               | Total daily sales    |
| total_invoices | INT                         | Number of invoices   |
| top_product_id | BIGINT (FK → products.id)   | Best selling product |
*/
