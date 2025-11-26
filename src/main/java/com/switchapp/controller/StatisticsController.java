package com.switchapp.controller;

import com.switchapp.service.StatisticsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/statistics")
public class StatisticsController {

    @Autowired
    private StatisticsService statisticsService;

    @GetMapping("/sales")
    public Object getSalesReport(@RequestParam String from, @RequestParam String to) {
        return statisticsService.getSalesReport(from, to);
    }

    @GetMapping("/top-products")
    public Object getTopProducts() {
        return statisticsService.getTopProducts();
    }

    @GetMapping("/customers")
    public Object getCustomerAnalytics() {
        return statisticsService.getCustomerAnalytics();
    }

    @GetMapping("/summary")
    public Object getSummary() {
        return statisticsService.getSummary();
    }
}
