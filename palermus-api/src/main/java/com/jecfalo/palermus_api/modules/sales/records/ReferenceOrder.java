package com.jecfalo.palermus_api.modules.sales.records;

import com.jecfalo.palermus_api.modules.sales.models.Order;
import com.jecfalo.palermus_api.modules.sales.models.PaidState;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

public record ReferenceOrder(
        Long orderId,
        PaidState paidState,
        LocalDateTime createdOrder,
        LocalDateTime lastModifiedOrder,
        BigDecimal totalAmount,
        BigDecimal amountPaid,
        BigDecimal changeDue,
        String clientName,
        String documentName,
        String sellerName,
        List<ItemDetailOrder> itemDetail,
        Boolean orderActive
) {
    public ReferenceOrder(Order order){
        this(
                order.getOrderId(),
                order.getPaidState(),
                order.getCreatedOrder(),
                order.getLastModifiedOrder(),
                order.getTotalAmount(),
                order.getAmountPaid(),
                order.getChangeDue(),
                order.getClientId() !=null ? order.getClientId().getNames() : "Consumidor Final",
                order.getClientId() !=null ? order.getClientId().getDocument() : "999999999",
                order.getSellerId() != null ? order.getSellerId().getNames() : "Compra web",
                order.getDetailOrder() !=null ?
                        order.getDetailOrder().stream()
                                .filter(detailOrder -> detailOrder.getDetailActive())
                                .map(ItemDetailOrder::new)
                                        .collect(Collectors.toList()):List.of(),
                order.getOrderActive()
        );
    }
}
