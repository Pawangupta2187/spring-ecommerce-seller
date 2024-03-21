package com.seller.seller.repository;

import com.seller.seller.entities.users.ConfirmationToken;
import org.springframework.data.repository.CrudRepository;

public interface ConfirmationTokenRepository extends CrudRepository<ConfirmationToken, String> {
    ConfirmationToken findByConfirmationToken(String confirmationToken);
    ConfirmationToken findByUserId(Long id);
}
