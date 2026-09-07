package com.jecfalo.palermus_api.modules.sales.services;

import com.jecfalo.palermus_api.modules.inventory.models.Product;
import com.jecfalo.palermus_api.modules.sales.repositories.ProductRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ProductService {
    @Autowired
    private ProductRepository repository;
    public Product findProduct(Long productId){
        return repository.findById(productId)
                .orElseThrow(()-> new EntityNotFoundException("No existe un producto asociado"));
    }
}
