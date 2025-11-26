package com.switchapp.service;

import com.switchapp.model.Invoice;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class InvoiceServiceImpl implements InvoiceService {

    @Override
    public List<Invoice> getAllInvoices() {
        return List.of();
    }

    @Override
    public Invoice getInvoiceById(Long id) {
        return null;
    }

    @Override
    public Invoice createInvoice(Invoice invoice) {
        return null;
    }

    @Override
    public String generatePrintableInvoice(Long id) {
        return "";
    }
}
