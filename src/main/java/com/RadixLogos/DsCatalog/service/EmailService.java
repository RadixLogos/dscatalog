package com.RadixLogos.DsCatalog.service;

import com.RadixLogos.DsCatalog.service.exceptions.EmailException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {
    @Value("${spring.mail.username}")
    public String emailFrom;

    @Autowired
    public JavaMailSender mailSender;

    public void sendEmail(String to, String from, String subject, String text){
        try{
            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(to);
            message.setFrom(from);
            message.setSubject(subject);
            message.setText(text);
            mailSender.send(message);
        }catch (EmailException e){
           throw new EmailException("Error sending email");
        }
    }

}
