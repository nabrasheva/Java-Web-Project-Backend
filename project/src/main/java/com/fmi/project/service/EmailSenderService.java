package com.fmi.project.service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@AllArgsConstructor
public class EmailSenderService {

    private final JavaMailSender javaMailSender;

    public void sendEmail(String to, String subject, String body){
        try{
            MimeMessage mimeMessage = javaMailSender.createMimeMessage();
            MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, "utf-8");

            String token = generateToken();

            String newBody = body.concat(token);

            mimeMessageHelper.setFrom("fn72039@g.fmi.uni-sofia.bg");
            mimeMessageHelper.setTo(to);
            mimeMessageHelper.setSubject(subject);
            mimeMessageHelper.setText(newBody);

            javaMailSender.send(mimeMessage);

        } catch (Exception e){
            throw new RuntimeException(e);
        }
    }

    private String generateToken(){
        //LocalDateTime expiryDate = LocalDateTime.now().plusMinutes(30); //the token will be available for 30 min

        //TODO: save the token and the expiry date in the database for the user table

        return UUID.randomUUID().toString();
    }
}
