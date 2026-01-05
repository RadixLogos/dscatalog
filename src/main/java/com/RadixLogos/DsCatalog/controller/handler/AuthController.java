package com.RadixLogos.DsCatalog.controller.handler;

import com.RadixLogos.DsCatalog.dto.EmailDTO;
import com.RadixLogos.DsCatalog.service.PasswordRecoverService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthController {
    @Autowired
    PasswordRecoverService passwordRecoverService;

    @PostMapping("/recover-token")
    public ResponseEntity<Void> sendPasswordRedefinitionToken(@Valid @RequestBody EmailDTO emailDTO){
        passwordRecoverService.sendRecoverToken(emailDTO);
        return ResponseEntity.noContent().build();
    }
}
