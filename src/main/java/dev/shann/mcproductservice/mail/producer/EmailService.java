package dev.shann.mcproductservice.mail.producer;

import dev.shann.mcproductservice.mail.model.MailDTO;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;

import java.io.File;

public class EmailService implements EmailClient{

    private  JavaMailSender javaMailSender;

    @Value("${spring.mail.username}") private String sender;

    public EmailService(JavaMailSender javaMailSender){
    this.javaMailSender = javaMailSender;
    }

    @Override
    public String sendSimpleMail(MailDTO mailDTO) {
        try{
            SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
            simpleMailMessage.setFrom(sender);
            simpleMailMessage.setTo(mailDTO.getRecipient());
            simpleMailMessage.setText(mailDTO.getMsgBody());
            simpleMailMessage.setSubject(mailDTO.getSubject());

            // Sending the mail
            javaMailSender.send(simpleMailMessage);
            return "Mail Sent Successfully...";
        }
        catch (Exception e) {
            return "Error while Sending Mail";
        }
    }

    @Override
    public String sendMailWithAttachment(MailDTO mailDTO) {
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
            mimeMessageHelper.setTo(mailDTO.getRecipient());
            mimeMessageHelper.setText(mailDTO.getMsgBody());
            mimeMessageHelper.setSubject(
                    mailDTO.getSubject());

            // Adding the attachment
            FileSystemResource file
                    = new FileSystemResource(
                    new File(mailDTO.getAttachment()));

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
