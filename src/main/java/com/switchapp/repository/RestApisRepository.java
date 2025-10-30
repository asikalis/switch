package com.switchapp.repository;

import com.switchapp.model.RestApis;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RestApisRepository extends JpaRepository<RestApis, Long> {
    List<RestApis> findByUrlPatternsPathAndHttpMethodsName(String urlPatternsPath, String httpMethodsName);

    List<RestApis> findByUrlPatternsPathAndHttpMethodsNameAndReturnType(String urlPatternsPath, String httpMethodsName, String returnType);
}
