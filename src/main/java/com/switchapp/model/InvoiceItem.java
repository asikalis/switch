package com.switchapp.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Entity
@Table(name = "invoice_items")
@Setter
@Getter
public class InvoiceItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "invoice_id", nullable = false)
    private Invoice invoice;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;
    @Column(nullable = false)
    private Integer quantity;
    @Column(name = "unit_price", precision = 10, scale = 2)
    private BigDecimal unitPrice;
    @Column(name = "total_price", precision = 10, scale = 2)
    private BigDecimal totalPrice;
}

/*
| Column      | Type                      | Description           |
| ----------- | ------------------------- | --------------------- |
| id          | BIGINT (PK)               | Item ID               |
| invoice_id  | BIGINT (FK → invoices.id) | Linked invoice        |
| product_id  | BIGINT (FK → products.id) | Linked product        |
| quantity    | INT                       | Quantity sold         |
| unit_price  | DECIMAL(10,2)             | Price per unit        |
| total_price | DECIMAL(10,2)             | quantity * unit_price |

Relation:
invoices (1) → (M) invoice_items
products (1) → (M) invoice_items
*/
