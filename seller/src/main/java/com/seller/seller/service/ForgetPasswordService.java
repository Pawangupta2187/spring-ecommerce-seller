package com.seller.seller.service;

import com.seller.seller.entities.users.ConfirmationToken;
import com.seller.seller.entities.users.DTO.UpdatePasswordDTO;
import com.seller.seller.entities.users.User;
import com.seller.seller.exception.*;
import com.seller.seller.repository.ConfirmationTokenRepository;
import com.seller.seller.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ForgetPasswordService {
    @Autowired
    RegisterService registerService;

    @Autowired
    UserRepository userRepository;

    @Autowired
    TokenStore tokenStore;

    @Autowired
    ConfirmationTokenRepository confirmationTokenRepository;

    @Autowired
    EmailSenderService emailSenderService;

    public ResponseEntity<SuccessResponse> forgetPasswordLinkCreation(String email)
    {
        System.out.println(email+">>>>>");
        List<User> users=userRepository.findUserByemailId(email);
        if(users.size()==0)
            throw new NotFoundException("User Not Found");
        User user=users.get(0);
        if(user.getIsActive()==false)
            throw new NotFoundException("Not a Active User");
        ConfirmationToken token=confirmationTokenRepository.findByUserId(user.getId());
        if(token!=null)
            confirmationTokenRepository.delete(token);
        ConfirmationToken confirmationToken = new ConfirmationToken(user);
        try {

            confirmationTokenRepository.save(confirmationToken);
           // confirmationTokenRepository.save(confirmationToken);
        }catch (Exception ex)
        {
            ex.printStackTrace();
            throw new BadRequestException(ex.getMessage());
        }
        SimpleMailMessage mailMessage = emailSenderService.CreateBodyForMail(
                user.getEmailId(),"Change Password!","To Change Password, please click here : "
                        +"http://localhost:8080/changepassword?token="+confirmationToken.getConfirmationToken());
        emailSenderService.sendEmail(mailMessage);
        return ResponseEntity.status(HttpStatus.CREATED).body(new SuccessResponse("Link is sent on your mail"));
    }

    public ResponseEntity<SuccessResponse> changePassword(String confirmationToken, UpdatePasswordDTO updatePasswordDTO)
    {
       // System.out.println(forgetPasswordDTO.getPassword()+" "+forgetPasswordDTO.getConfirmPassword());
        if(!updatePasswordDTO.getPassword().equals(updatePasswordDTO.getConfirmPassword()))
            throw new ConflictException("Password and ConfirmPassword field not match");
        ConfirmationToken token = confirmationTokenRepository.findByConfirmationToken(confirmationToken);
        if(token != null)
        {
//            List<User> user = userRepository.findUserByemailId(token.getUser().getEmailId());

            registerService.changePassword(token.getUser().getId(),updatePasswordDTO.getPassword());
            confirmationTokenRepository.delete(token);
            return ResponseEntity.status(HttpStatus.CREATED).body(new SuccessResponse("Password Changed"));
        }
        else
        {
            throw new TokenFailedException("The link is invalid or broken!");
        }
    }
}
