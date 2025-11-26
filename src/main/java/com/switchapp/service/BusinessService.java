package com.switchapp.service;

import com.switchapp.dto.BusinessDto;
import com.switchapp.model.Business;

import java.util.List;

public interface BusinessService {
    List<Business> getAllBusinessesForLoggedUser();

    Business createBusiness(BusinessDto business);

    Business getBusinessById(Long id);

    Business updateBusiness(Long id, Business business);

    void deleteBusiness(Long id);
}
