package com.seller.seller.controllers;

import com.seller.seller.entities.users.DTO.SellerProfileDTO;
import com.seller.seller.entities.users.DTO.AddressDTO;

import com.seller.seller.entities.users.DTO.UpdatePasswordDTO;
import com.seller.seller.entities.users.UpdateSellerProfileDTO;
import com.seller.seller.exception.SuccessResponse;
import com.seller.seller.service.SellerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

@RequestMapping("/seller")
@RestController
public class SellerController {

    @Autowired
    SellerService sellerService;


    @GetMapping("/profile")
    public SellerProfileDTO sellerProfile(HttpServletRequest request ) {
        return sellerService.sellerProfile(request);
    }

    @PutMapping("/password")
    public ResponseEntity<SuccessResponse> updatepassword(HttpServletRequest request, @RequestBody @Valid UpdatePasswordDTO updatePasswordDTO) {
      return sellerService.updatepassword(request,updatePasswordDTO);
    }

    @PatchMapping("/address")
    public ResponseEntity<SuccessResponse> updateAddress(HttpServletRequest request,@RequestBody @Valid AddressDTO address) {
        return sellerService.updateAddress(request,address);
    }

    @PatchMapping("/profile")
    public ResponseEntity<SuccessResponse> updateProfile(HttpServletRequest request, @RequestBody @Valid UpdateSellerProfileDTO updateSellerProfileDTO) {
        return sellerService.updateProfile(request,updateSellerProfileDTO);
    }
}