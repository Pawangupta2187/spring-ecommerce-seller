package com.seller.seller.controllers;


import com.seller.seller.entities.users.Customer;
import com.seller.seller.entities.users.Seller;
import com.seller.seller.exception.SuccessResponse;
import com.seller.seller.service.RegisterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

@RequestMapping("/register")
@RestController
public class RegisterController {

    @Autowired
    RegisterService registerService;

    @Autowired
    TokenStore tokenStore;

    @GetMapping("/doLogout")
    public String logout(HttpServletRequest request) {
        String authHeader = request.getHeader("Authorization");
        if (authHeader != null) {
            String tokenValue = authHeader.replace("Bearer", "").trim();
            OAuth2AccessToken accessToken = tokenStore.readAccessToken(tokenValue);
            tokenStore.removeAccessToken(accessToken);
        }
        return "Logged out successfully";
    }

    @PostMapping("/uploadimage")
    public void uploadImage(@RequestPart("image") MultipartFile image) throws IOException {
        System.out.println(image);
        String img=image.getOriginalFilename();
        System.out.println( image.getOriginalFilename());
        System.out.println( image.getName());
        System.out.println( image.getContentType());
        String[] str=image.getOriginalFilename().split("\\.");
        System.out.println(str[str.length-1]);
        String fileName = StringUtils.cleanPath(image.getOriginalFilename());
//        user.setPhotos(fileName);
//
//        User savedUser = repo.save(user);

        String uploadDir = "user-photos/" +img;

      //  FileUploadUtil.saveFile(uploadDir, fileName, multipartFile);
        Path uploadPath = Paths.get(uploadDir);

        if (!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath);
        }

        try (InputStream inputStream = image.getInputStream()) {
            Path filePath = uploadPath.resolve(fileName);
            Files.copy(inputStream, filePath, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException ioe) {
            throw new IOException("Could not save image file: " + fileName, ioe);
        }
    }


    @PostMapping("/seller")
    public ResponseEntity<SuccessResponse> createSeller(@Valid @RequestBody Seller seller) {
        return registerService.createSeller(seller);
    }


    //post api for activate account
//    @PostMapping("/activateaccount")
//    public ResponseEntity<SuccessResponse> confirmUserAccount(@RequestParam("token") String confirmationToken) {
//        return registerService.activateCustomerAccount(confirmationToken);
//    }

    //api for send re-activation link
//    @PostMapping("/resendactivationlink")
//    public ResponseEntity<SuccessResponse> resendActivationLink(@RequestBody String email){
//System.out.println("-----"+email);
//        return registerService.resendActivationLink(email);
//    }


}
