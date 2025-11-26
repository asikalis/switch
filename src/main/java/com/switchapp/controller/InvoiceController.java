package com.switchapp.controller;

import com.switchapp.model.Invoice;
import com.switchapp.service.InvoiceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/invoices")
public class InvoiceController {

    @Autowired
    private InvoiceService invoiceService;

    // GET /api/invoices
    @GetMapping
    public List<Invoice> getAllInvoices() {
        return invoiceService.getAllInvoices();
    }

    // GET /api/invoices/{id}
    @GetMapping("/{id}")
    public ResponseEntity<Invoice> getInvoiceById(@PathVariable Long id) {
        Invoice invoice = invoiceService.getInvoiceById(id);
        return ResponseEntity.ok(invoice);
    }

    // POST /api/invoices
    @PostMapping
    public ResponseEntity<Invoice> createInvoice(@RequestBody Invoice invoice) {
        Invoice created = invoiceService.createInvoice(invoice); // Should reduce stock
        return ResponseEntity.ok(created);
    }

    // GET /api/invoices/{id}/print
    @GetMapping("/{id}/print")
    public ResponseEntity<String> printInvoice(@PathVariable Long id) {
        String printable = invoiceService.generatePrintableInvoice(id); // PDF/HTML logic
        return ResponseEntity.ok(printable);
    }
}
