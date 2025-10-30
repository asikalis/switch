package com.switchapp.repository;

import com.switchapp.model.SystemRegistry;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SystemRegistryRepository extends JpaRepository<SystemRegistry, Long> {

    SystemRegistry findBySystemKey(String systemKey);
}
