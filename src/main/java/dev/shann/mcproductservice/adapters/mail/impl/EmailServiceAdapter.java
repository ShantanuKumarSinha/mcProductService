package dev.shann.mcproductservice.adapters.mail.impl;

import dev.shann.mcproductservice.model.Mail;
import dev.shann.mcproductservice.adapters.mail.MailAdapter;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;

import java.io.File;
@Slf4j
public class EmailServiceAdapter implements MailAdapter {

    private  JavaMailSender javaMailSender;

    @Value("${spring.mail.username}") private String sender;

    public EmailServiceAdapter(JavaMailSender javaMailSender){
    this.javaMailSender = javaMailSender;
    }

    @Override
    public String sendSimpleMail(Mail mail) {
        try{
            SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
            simpleMailMessage.setFrom(sender);
            simpleMailMessage.setTo(mail.getRecipient());
            simpleMailMessage.setText(mail.getMsgBody());
            simpleMailMessage.setSubject(mail.getSubject());
            log.info("Sending Mail : {}",simpleMailMessage);

            // Sending the mail
            javaMailSender.send(simpleMailMessage);
            log.info("Mail Sent Successfully...");
            return "Mail Sent Successfully...";
        }
        catch (MailException e) {
            log.error("Error while Sending Mail  : {}",e);
            return "Error while Sending Mail";
        }
    }

    @Override
    public String sendMailWithAttachment(Mail mail) {
        // Creating a mime message
        MimeMessage mimeMessage
                = javaMailSender.createMimeMessage();
        MimeMessageHelper mimeMessageHelper;

        try {
            // Setting multipart as true for attachments to
            // be send
            mimeMessageHelper
                    = new MimeMessageHelper(mimeMessage, true);
            mimeMessageHelper.setFrom(sender);
            mimeMessageHelper.setTo(mail.getRecipient());
            mimeMessageHelper.setText(mail.getMsgBody());
            mimeMessageHelper.setSubject(
                    mail.getSubject());

            // Adding the attachment
            FileSystemResource file
                    = new FileSystemResource(
                    new File(mail.getAttachment()));

            mimeMessageHelper.addAttachment(
                    file.getFilename(), file);

            // Sending the mail
            javaMailSender.send(mimeMessage);
            return "Mail sent Successfully";
        }

        // Catch block to handle MessagingException
        catch (MessagingException e) {

            // Display message when exception occurred
            return "Error while sending mail!!!";
        }
    }
}
