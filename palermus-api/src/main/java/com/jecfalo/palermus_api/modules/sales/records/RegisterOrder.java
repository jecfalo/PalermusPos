package com.jecfalo.palermus_api.modules.sales.records;

import java.util.List;

public record RegisterOrder(
        Long clientId,
        Long sellerId,
        List<RegisterDetailOrder> details
) {
}
