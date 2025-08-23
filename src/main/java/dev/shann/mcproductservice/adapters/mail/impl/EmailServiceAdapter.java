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
/**
 * EmailServiceAdapter is an implementation of the MailAdapter interface
 * that uses JavaMailSender to send simple and attachment emails.
 * It provides methods to send emails with or without attachments.
 *
 * @author shann
 * @version 1.0
 */
public class EmailServiceAdapter implements MailAdapter {

    private  JavaMailSender javaMailSender;

    @Value("${spring.mail.username}") private String sender;

    /**
     * Constructor to initialize the JavaMailSender.
     *
     * @param javaMailSender JavaMailSender instance
     */
    public EmailServiceAdapter(JavaMailSender javaMailSender){
    this.javaMailSender = javaMailSender;
    }

    /**
     * Sends a simple email.
     *
     * @param mail Mail object containing recipient, subject, and message body
     * @return String indicating success or failure of the email sending
     */
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
            log.error("Error while Sending Mail: {}",e);
            return "Error while Sending Mail";
        }
    }

    /**
     * Sends an email with an attachment.
     *
     * @param mail Mail object containing recipient, subject, message body, and attachment path
     * @return String indicating success or failure of the email sending
     */
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
