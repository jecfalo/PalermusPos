package com.jecfalo.palermus_api.modules.sales.services;

import com.jecfalo.palermus_api.modules.inventory.models.Product;
import com.jecfalo.palermus_api.modules.sales.models.DetailOrder;
import com.jecfalo.palermus_api.modules.sales.models.Order;
import com.jecfalo.palermus_api.modules.sales.records.*;
import com.jecfalo.palermus_api.modules.sales.repositories.OrderRepository;
import com.jecfalo.palermus_api.modules.users.models.Profile;
import com.jecfalo.palermus_api.modules.users.repositories.ProfileRepository;
import com.jecfalo.palermus_api.modules.users.services.ProfileService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.awt.color.ProfileDataException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

@Service
public class OrderService implements  IOrderService{
    @Autowired
    private OrderRepository repository;
    @Autowired
    private ProfileService profileService;
    @Autowired
    private ProductService productService;
    @Override
    @Transactional
    public Page<DataListOrder> loadAllActive(Pageable page) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String typeProfile = auth.getAuthorities().iterator().next().getAuthority();

        Page<Order> orders = repository.findAll(page);

        if(typeProfile.equals("ROLE_CLIENT")){
            Page<Order> orderClient = repository.findByClientIdUserUsername(auth.getName(), page);
            return orderClient.map(DataListOrder::new);
        }
        return orders.map(DataListOrder::new);
    }

    @Override
    @Transactional
    public ReferenceOrder createOrder(RegisterOrder register) {
        Order order = new Order(register);
        Profile profile = profileService.findProfileEntity(register.clientId());
        order.setClientId(profile);
        if(register.sellerId() != null){
            Profile seller = profileService.findProfileEntity(register.sellerId());
            order.setSellerId(seller);
        }
        List<DetailOrder>  details = new ArrayList<>();
        BigDecimal subtotalCalculated = BigDecimal.ZERO;
        for(RegisterDetailOrder detail: register.details()){
            Product product = productService.findProduct(detail.productId());
            BigDecimal quantity = BigDecimal.valueOf(detail.quantity());
            BigDecimal price = product.getPrice();
            BigDecimal itemSubtotal = price.multiply(quantity);
            subtotalCalculated = subtotalCalculated.add(itemSubtotal);
            DetailOrder detailOrder = new DetailOrder(detail, order, product);
            details.add(detailOrder);
        }
        BigDecimal ivaPorcentage = new BigDecimal("0.15");
        BigDecimal ivaAmount = subtotalCalculated.multiply(ivaPorcentage).setScale(2, RoundingMode.HALF_UP);
        BigDecimal finalTotal = subtotalCalculated.add(ivaAmount);

        order.setTotalAmount(finalTotal);
        order.setDetailOrder(details);
        Order orderGenerated = repository.save(order);
        return new ReferenceOrder(orderGenerated);
    }

    @Override
    public ReferenceOrder getOrderUnique(Long id) {
        return null;
    }

    @Override
    public ReferenceOrder updateOrder(Long id, UpdateOrder update) {
        return null;
    }

    @Override
    public Boolean deleteOrder(Long id) {
        return null;
    }
}
