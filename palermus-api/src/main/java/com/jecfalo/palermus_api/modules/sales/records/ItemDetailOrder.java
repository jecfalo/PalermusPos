package com.jecfalo.palermus_api.modules.sales.records;

import com.jecfalo.palermus_api.modules.sales.models.DetailOrder;

import java.math.BigDecimal;

public record ItemDetailOrder(
        Long id,
        String productName,
        BigDecimal productPrice,
        Integer quantity
) {
    public ItemDetailOrder(DetailOrder detail){
        this(
                detail.getDetOrderId(),
                detail.getProduct().getName(),
                detail.getProduct().getPrice(),
                detail.getQuantity()
        );
    }
}
