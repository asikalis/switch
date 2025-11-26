package com.switchapp.service;

import com.switchapp.model.Invoice;

import java.util.List;

public interface InvoiceService {
    List<Invoice> getAllInvoices();

    Invoice getInvoiceById(Long id);

    Invoice createInvoice(Invoice invoice);

    String generatePrintableInvoice(Long id);
}
