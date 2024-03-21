package com.seller.seller.service;

import com.seller.seller.entities.users.*;
import com.seller.seller.exception.*;
import com.seller.seller.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.*;

@Service
public class RegisterService {



    @Autowired
    RegisterSellerRepository registerSellerRepository;

    @Autowired
    RoleRepository roleRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    ConfirmationTokenRepository confirmationTokenRepository;

    @Autowired
    EmailSenderService emailSenderService;




    public ResponseEntity<SuccessResponse> createSeller(Seller seller) {
        Seller seller1=registerSellerRepository.findSellerByEmailId(seller.getEmailId());
        if(seller1!=null) {
            throw new ConflictException("User All ready Exist");
        }
        if(registerSellerRepository.findSellerByCompanyNameIgnoreCase(seller.getCompanyName()).size()!=0 || registerSellerRepository.findSellerByGstIgnoreCase(seller.getGst()).size()!=0)
           throw new ConflictException("gst or company name is already registered");
        Set<Role> roles = new HashSet<>();
        Role role = roleRepository.findByAuthority("ROLE_SELLER").get(0);


        // role.setAuthority("ROLE_SELLER");
        roles.add(role);
        seller.setRoles(roles);
        seller.addAddress(seller.getAddress());
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        seller.setPassword(passwordEncoder.encode(seller.getPassword()));
        try {
            registerSellerRepository.save(seller);
        }catch (Exception ex) {
            throw new BadRequestException(ex.getMessage());
        }

        SimpleMailMessage mailMessage = emailSenderService.CreateBodyForMail(seller.getEmailId(),"Waiting For Approval","Seller Created wait for activation");
        emailSenderService.sendEmail(mailMessage);
        return ResponseEntity.status(HttpStatus.CREATED).body(new SuccessResponse("Seller Created wait for activation"));


        //   return seller;

    }

    public Seller readSeller(Long id) {
        Optional<Seller> seller = registerSellerRepository.findById(id);
        return seller.get();
    }

    //changepassword
    public void changePassword(Long id, String password) {
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        Optional<Seller> seller=registerSellerRepository.findById(id);
         if(seller.isPresent())
        {
            seller.get().setPassword(passwordEncoder.encode(password));
            System.out.println(seller.get().getGst()+"GST--");
            try {

                registerSellerRepository.save(seller.get());
            }catch (Exception ex) {
                ex.printStackTrace();
               throw new BadRequestException(ex.getMessage());
            }
        }else
        {
            throw new NotFoundException("user not found");
        }


    }

}

