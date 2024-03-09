package dev.shann.mcproductservice.mail.producer;

import dev.shann.mcproductservice.mail.model.MailDTO;

public interface EmailClient {

    // To send a simple email
    String sendSimpleMail(MailDTO mailDTO);

    // To send an email with attachment
    String sendMailWithAttachment(MailDTO mailDTO);
}