package com.seller.seller.entities.cart.DTO;

import com.seller.seller.entities.products.DTO.ViewVariationDTO;
import lombok.Data;

@Data
public class ViewCartDTO {
    private Long quantity;
    private ViewVariationDTO variation;
    }
