package com.jecfalo.palermus_api.modules.sales.repositories;

import com.jecfalo.palermus_api.modules.inventory.models.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, Long> {
}
