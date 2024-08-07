package com.myproject.BankingApplication.service;

import com.myproject.BankingApplication.dto.EmailDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.sql.SQLOutput;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender javaMailSender;
    @Value("${spring.mail.username}")
    private String senderEmail;

    public void sendEmailAlert(EmailDetails emailDetails){

        SimpleMailMessage message = new SimpleMailMessage();

        message.setFrom(senderEmail);
        message.setTo(emailDetails.getRecipient());
        message.setText(emailDetails.getMessageBody());
        message.setSubject(emailDetails.getSubject());

        javaMailSender.send(message);
        System.out.println("Mail sent successfully...");
    }



}

