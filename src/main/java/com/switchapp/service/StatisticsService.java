package com.switchapp.service;

public interface StatisticsService {
    Object getSalesReport(String from, String to);
    Object getTopProducts();
    Object getCustomerAnalytics();
    Object getSummary();
}
