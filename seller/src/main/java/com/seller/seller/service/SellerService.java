package com.seller.seller.service;

import com.seller.seller.controllers.RegisterController;
import com.seller.seller.entities.users.DTO.AddressDTO;
import com.seller.seller.entities.users.DTO.SellerProfileDTO;
import com.seller.seller.entities.users.DTO.UpdatePasswordDTO;
import com.seller.seller.entities.users.Seller;
import com.seller.seller.entities.users.UpdateSellerProfileDTO;
import com.seller.seller.exception.BadRequestException;
import com.seller.seller.exception.NotFoundException;
import com.seller.seller.exception.SuccessResponse;
import com.seller.seller.repository.RegisterSellerRepository;
import org.modelmapper.Conditions;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.security.Principal;

@Service
public class SellerService {

    @Autowired
    RegisterSellerRepository registerSellerRepository;

    @Autowired
    RegisterService registerService;

    @Autowired
    ModelMapper mm;

    @Autowired
    RegisterController registerController;


    public SellerProfileDTO sellerProfile(HttpServletRequest request )
    {
        Principal principal = request.getUserPrincipal();
        String emailId=principal.getName();
        SellerProfileDTO seller=registerSellerRepository.getSellerProfileByEmailId(emailId);
        if(seller==null)
                throw new NotFoundException("seller Not Found");
            return seller;
    }
    public ResponseEntity<SuccessResponse> updatepassword(HttpServletRequest request, UpdatePasswordDTO updatePasswordDTO) {
       Principal principal = request.getUserPrincipal();
        String emailId=principal.getName();
            if (!updatePasswordDTO.getPassword().equals(updatePasswordDTO.getConfirmPassword()))
                throw new BadRequestException("Password and ConfirmPassword field not match");

            Seller seller = registerSellerRepository.findSellerByEmailId(emailId);
            if(seller==null)
                throw new NotFoundException("Not found");

            registerService.changePassword(seller.getId(), updatePasswordDTO.getPassword());
        registerController.logout(request);
        return ResponseEntity.status(HttpStatus.OK).body(new SuccessResponse("Password Changed"));
    }

    public ResponseEntity<SuccessResponse> updateAddress(HttpServletRequest request, AddressDTO address)
    {
        Principal principal = request.getUserPrincipal();
        String emailId=principal.getName();
        Seller seller = registerSellerRepository.findSellerByEmailId(emailId);
        if(seller==null)
            throw new NotFoundException("Not found");
        mm.getConfiguration().setPropertyCondition(Conditions.isNotNull());
        mm.map(address, seller);
             try {
                 registerSellerRepository.save(seller);
             }catch (Exception ex) {
                throw new BadRequestException(ex.getMessage());
             }
        return ResponseEntity.status(HttpStatus.OK).body(new SuccessResponse("Address updated"));
    }

    public ResponseEntity<SuccessResponse> updateProfile(HttpServletRequest request,UpdateSellerProfileDTO updateSellerProfileDTO)
    {
        Principal principal = request.getUserPrincipal();
        String emailId=principal.getName();
            Seller seller = registerSellerRepository.findSellerByEmailId(emailId);
            if(seller==null)
                throw new NotFoundException("Seller Not found");
            System.out.println(seller.getGst()+">>>>>");
//           mm.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
        mm.getConfiguration().setPropertyCondition(Conditions.isNotNull());


        mm.map(updateSellerProfileDTO, seller);
        System.out.println(seller.getGst()+">>>>>");

        try{
        registerSellerRepository.save(seller);
        }catch (Exception ex) {
            ex.printStackTrace();
           throw new BadRequestException(ex.getMessage());
        }
             return ResponseEntity.status(HttpStatus.OK).body(new SuccessResponse("Profile updated"));

    }

}
