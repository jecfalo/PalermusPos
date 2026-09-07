package com.jecfalo.palermus_api.modules.sales.models;

import com.jecfalo.palermus_api.modules.inventory.models.Product;
import com.jecfalo.palermus_api.modules.sales.records.RegisterDetailOrder;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@Entity
@Table(name = "orderdetails")
@EntityListeners(AuditingEntityListener.class)
public class DetailOrder {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long detOrderId;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "orderId")
    private Order order;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "productId")
    private Product product;
    private Integer quantity;
    private Boolean detailActive;
    public DetailOrder(RegisterDetailOrder register, Order order, Product product){
        this.detOrderId = null;
        this.order = order;
        this.product = product;
        this.quantity = register.quantity();
        this.detailActive = true;
    }
}
