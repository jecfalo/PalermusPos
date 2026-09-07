package com.jecfalo.palermus_api.modules.sales.records;

import com.jecfalo.palermus_api.modules.sales.models.DetailOrder;
import com.jecfalo.palermus_api.modules.sales.models.Order;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record DataListOrder(
        Long id,
        String documentClient,
        BigDecimal totalAmount,
        Integer quantity,
        LocalDateTime purchaseDate
) {
    public DataListOrder(Order order){
        this(
                order.getOrderId(),
                order.getClientId().getDocument(),
                order.getTotalAmount(),
                order.getDetailOrder().stream()
                                .mapToInt(DetailOrder::getQuantity)
                        //operador ternario en caso de error
                                        .sum(),
                order.getLastModifiedOrder()
        );
    }
}
