package com.jecfalo.palermus_api.modules.inventory.models;

import jakarta.persistence.*;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@Entity
@Table(name = "orders")
public class Inventory {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne(fetch = FetchType.LAZY)
    private Product product;
    private Integer physQuantity;
    private Integer amountReserved;
    private Boolean invActive;

}
