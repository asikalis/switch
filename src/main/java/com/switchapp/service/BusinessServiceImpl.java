package com.switchapp.service;

import com.switchapp.dto.BusinessDto;
import com.switchapp.model.Business;
import com.switchapp.model.User;
import com.switchapp.repository.BusinessRepository;
import com.switchapp.repository.UserRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class BusinessServiceImpl implements BusinessService {

    private final Map<Long, Business> businessStore = new HashMap<>();
    private long idCounter = 1;

    @Autowired
    BusinessRepository businessRepository;

    @Autowired
    private UserRepository userRepository;

    public List<Business> getAllBusinessesForLoggedUser() {
        // For demo, return all businesses
        return new ArrayList<>(businessStore.values());
    }

    public Business createBusiness(BusinessDto businessDto) {
//        business.setId(idCounter++);
//        businessStore.put(business.getId(), business);

        // Get username from SecurityContext
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Business business = new Business();
        BeanUtils.copyProperties(businessDto, business);
        business.setUser(user);
        return businessRepository.save(business);
    }

    public Business getBusinessById(Long id) {
        return businessStore.get(id);
    }

    public Business updateBusiness(Long id, Business business) {
        business.setId(id);
        businessStore.put(id, business);
        return business;
    }

    public void deleteBusiness(Long id) {
        businessStore.remove(id);
    }
}
