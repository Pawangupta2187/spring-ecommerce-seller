package com.seller.seller.controllers;

import com.seller.seller.entities.users.DTO.UpdatePasswordDTO;
import com.seller.seller.exception.SuccessResponse;
import com.seller.seller.service.ForgetPasswordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
public class forgetpasswordController {


    @Autowired
    ForgetPasswordService forgetPasswordService;


    @PostMapping("/forgetpassword")
    public ResponseEntity<SuccessResponse> forgetPasswordLinkCreation(@RequestPart("emailId") String email) {
        return forgetPasswordService.forgetPasswordLinkCreation(email);
    }

    @GetMapping("/changepassword")
    public ResponseEntity<SuccessResponse> changePaasword(@RequestParam("token")String confirmationToken, @RequestBody @Valid UpdatePasswordDTO updatePasswordDTO) {
        return forgetPasswordService.changePassword(confirmationToken,updatePasswordDTO);
    }

}
