package com.seller.seller.entities.orders;

import lombok.Data;

import javax.persistence.Embeddable;

@Data
@Embeddable
public class OrderStatus {
    private Status fromStatus;
    private Status toStatus;
    private String Notes;
}
