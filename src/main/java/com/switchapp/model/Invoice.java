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
public class Invoice {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "business_id", nullable = false)
    private Business business;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_id", nullable = false)
    private Customer customer;
    @Column(name = "invoice_number", nullable = false, unique = true, length = 50)
    private String invoiceNumber;
    @Column(name = "total_amount", precision = 10, scale = 2)
    private BigDecimal totalAmount = BigDecimal.ZERO;
    @Column(name = "tax_amount", precision = 10, scale = 2)
    private BigDecimal taxAmount = BigDecimal.ZERO;
    @Column(precision = 10, scale = 2)
    private BigDecimal discount = BigDecimal.ZERO;
    @Column(name = "net_amount", precision = 10, scale = 2)
    private BigDecimal netAmount = BigDecimal.ZERO;
    @Column(name = "invoice_date")
    private LocalDateTime invoiceDate;
    @Column(name = "payment_mode", length = 20)
    private String paymentMode = "CASH";
    @Enumerated(EnumType.STRING) // ✅ store enum name as text ("PAID", "UNPAID", "CANCELLED")
    @Column(nullable = false, length = 20)
    private InvoiceStatus status;
    @OneToMany(mappedBy = "invoice", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<InvoiceItem> items;
}

/*
| Column         | Type                              | Description           |
| -------------- | --------------------------------- | --------------------- |
| id             | BIGINT (PK)                       | Invoice ID            |
| business_id    | BIGINT (FK → businesses.id)       | Linked business       |
| customer_id    | BIGINT (FK → customers.id)        | Linked customer       |
| invoice_number | VARCHAR(50)                       | Unique invoice number |
| total_amount   | DECIMAL(10,2)                     | Total invoice amount  |
| tax_amount     | DECIMAL(10,2)                     | GST/VAT               |
| discount       | DECIMAL(10,2)                     | Discount applied      |
| net_amount     | DECIMAL(10,2)                     | Total payable         |
| invoice_date   | TIMESTAMP                         | Invoice date          |
| payment_mode   | VARCHAR(20)                       | e.g., CASH, CARD, UPI |
| status         | ENUM('PAID','UNPAID','CANCELLED') | Invoice status        |

Relation:
businesses (1) → (M) invoices
customers (1) → (M) invoices
*/

