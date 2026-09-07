package com.jecfalo.palermus_api.modules.inventory.repositories;

import com.jecfalo.palermus_api.modules.inventory.models.Inventory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface InventoryRepository extends JpaRepository<Inventory, Long> {
}
