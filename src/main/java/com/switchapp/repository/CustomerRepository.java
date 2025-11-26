package com.switchapp.repository;

import com.switchapp.model.Business;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CustomerRepository extends JpaRepository<Business, Long> {
}
