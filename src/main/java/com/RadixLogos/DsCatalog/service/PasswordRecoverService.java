package com.RadixLogos.DsCatalog.service;

import com.RadixLogos.DsCatalog.dto.EmailDTO;
import com.RadixLogos.DsCatalog.dto.NewPasswordDTO;
import com.RadixLogos.DsCatalog.entities.PasswordRecover;
import com.RadixLogos.DsCatalog.entities.User;
import com.RadixLogos.DsCatalog.repositories.PasswordRecoverRepository;
import com.RadixLogos.DsCatalog.repositories.UserRepository;
import com.RadixLogos.DsCatalog.service.exceptions.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.UUID;

@Service
public class PasswordRecoverService {
    @Value("${email.password-recover.token.minutes}")
    private Long tokenDuration;
    @Value("${email.password-recover.uri}")
    private String recoverLink;
    @Autowired
    private PasswordRecoverRepository passwordRecoverRepository;
    @Autowired
    private EmailService emailService;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PasswordEncoder encoder;
    public void sendRecoverToken(EmailDTO email){
        User user = userRepository.findUserByEmail(email.email());
        if(user == null){
            throw new NotFoundException("Email não encontrado");
        }
        String token = UUID.randomUUID().toString();
        PasswordRecover passwordRecover = new PasswordRecover(email.email(),token, Instant.now().plusSeconds(tokenDuration * 60L));
        passwordRecoverRepository.save(passwordRecover);
        emailService.sendEmail(email.email(),"support","Recuperação de senha","Acesse o link para recuperar a senha\n\n" + recoverLink + token + " com duração de " + tokenDuration + " minutos.");

    }

    @Transactional
    public void savePassword(NewPasswordDTO newPasswordDTO) {
       var response = passwordRecoverRepository.findValidToken(newPasswordDTO.token(),Instant.now());
        if(response.isEmpty()){
            throw new NotFoundException("Token inválido");
        }

        User user = userRepository.findUserByEmail(response.getFirst().getEmail());
        user.setPassword(encoder.encode(newPasswordDTO.password()));
        userRepository.save(user);
    }
}
