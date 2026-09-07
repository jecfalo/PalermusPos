package com.jecfalo.palermus_api.modules.sales.services;

import com.jecfalo.palermus_api.modules.sales.records.DataListOrder;
import com.jecfalo.palermus_api.modules.sales.records.ReferenceOrder;
import com.jecfalo.palermus_api.modules.sales.records.RegisterOrder;
import com.jecfalo.palermus_api.modules.sales.records.UpdateOrder;
import com.jecfalo.palermus_api.modules.users.models.Profile;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface IOrderService {
    Page<DataListOrder> loadAllActive(Pageable page);
    ReferenceOrder createOrder(RegisterOrder register);
    ReferenceOrder getOrderUnique(Long id);
    ReferenceOrder updateOrder(Long id, UpdateOrder update);
    Boolean deleteOrder(Long id);
}
