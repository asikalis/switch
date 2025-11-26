package com.switchapp.controller;

import com.switchapp.dto.BusinessDto;
import com.switchapp.model.Business;
import com.switchapp.service.BusinessService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/businesses")
public class BusinessController {

    @Autowired
    private BusinessService businessService;

    @GetMapping
    public List<Business> getAllBusinesses() {
        return businessService.getAllBusinessesForLoggedUser();
    }

    @PostMapping
    public Business createBusiness(@RequestBody BusinessDto business) {
        return businessService.createBusiness(business);
    }

    @GetMapping("/{id}")
    public Business getBusinessById(@PathVariable Long id) {
        return businessService.getBusinessById(id);
    }

    @PutMapping("/{id}")
    public Business updateBusiness(@PathVariable Long id, @RequestBody Business business) {
        return businessService.updateBusiness(id, business);
    }

    @DeleteMapping("/{id}")
    public void deleteBusiness(@PathVariable Long id) {
        businessService.deleteBusiness(id);
    }
}
/*
| Method | Endpoint             | Description                         |
| ------ | -------------------- | ----------------------------------- |
| GET    | `/api/businesses`      | List all businesses for logged user |
| POST   | `/api/businesses`      | Create new business                 |
| GET    | `/api/businesses/{id}` | Get business details                |
| PUT    | `/api/businesses/{id}` | Update business info                |
| DELETE | `/api/businesses/{id}` | Delete business                     |
*/
