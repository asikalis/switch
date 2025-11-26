package com.switchapp.service;

import com.switchapp.exception.ApiException;
import com.switchapp.model.Product;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class ProductServiceImpl implements ProductService {


    private final Map<Long, Product> productStore = new HashMap<>();
    private long idCounter = 1;

    @Override
    public List<Product> getAllProducts() {
        return new ArrayList<>(productStore.values());
    }

    @Override
    public Product getProductById(Long id) {
        Product product = productStore.get(id);
        if (product == null) {
            throw new ApiException("Product not found");
        }
        return product;
    }

    @Override
    public Product addProduct(Product product) {
        product.setId(idCounter++);
        productStore.put(product.getId(), product);
        return product;
    }

    @Override
    public Product updateProduct(Long id, Product product) {
        if (!productStore.containsKey(id)) {
            throw new ApiException("Product not found");
        }
        product.setId(id);
        productStore.put(id, product);
        return product;
    }

    @Override
    public void deleteProduct(Long id) {
        if (!productStore.containsKey(id)) {
            throw new ApiException("Product not found");
        }
        productStore.remove(id);
    }

    @Override
    public List<Product> searchProductsByName(String name) {
        return productStore.values().stream()
                .filter(p -> p.getName() != null && p.getName().toLowerCase().contains(name.toLowerCase()))
                .collect(Collectors.toList());
    }
}
