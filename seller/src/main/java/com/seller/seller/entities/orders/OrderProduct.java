package com.seller.seller.entities.orders;

import com.seller.seller.Auditing.Auditable;
import com.seller.seller.entities.JSONObjectConverter;
import com.seller.seller.entities.products.ProductVariation;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.json.JSONObject;

import javax.persistence.*;

@Entity
@Getter
@Setter
@Data
public class OrderProduct  extends Auditable<String> {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;
    private Long quantity;
    private Long price;
    @Column(columnDefinition = "TEXT")
    @Convert(converter= JSONObjectConverter.class)
    private JSONObject metaData;
     @ManyToOne
    @JoinColumn(name="productvariation_id")
    private ProductVariation productVariation;
//    private JSONObject productVariation;


    @ManyToOne
    @JoinColumn(name="order_id")
    private Order order;


    @Embedded
    private OrderStatus orderStatus;



}
