package com.jecfalo.palermus_api.modules.sales.models;

import com.jecfalo.palermus_api.modules.sales.records.RegisterOrder;
import com.jecfalo.palermus_api.modules.users.models.Profile;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@Entity
@Table(name = "orders")
@EntityListeners(AuditingEntityListener.class)
public class Order {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long orderId;
    @Enumerated(EnumType.STRING)
    private PaidState paidState;
    private BigDecimal totalAmount;
    private BigDecimal amountPaid;
    private BigDecimal changeDue;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "clientId")
    private Profile clientId;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sellerId")
    private Profile sellerId;
    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    private List<DetailOrder> detailOrder;
    private Boolean orderActive;
    @CreatedDate @Column(updatable = false)
    private LocalDateTime createdOrder;
    @LastModifiedDate @Column(updatable = false)
    private LocalDateTime lastModifiedOrder;
    public Order(RegisterOrder registerOrder){
        this.orderId = null;
        this.paidState = PaidState.PENDING;
        this.amountPaid = BigDecimal.ZERO;
        this.changeDue = BigDecimal.ZERO;
        this.detailOrder = new ArrayList<>();
        this.orderActive = true;
    }
}
