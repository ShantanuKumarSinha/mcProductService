package dev.shann.mcproductservice.adapters.mail;

import dev.shann.mcproductservice.model.Mail;

public interface MailAdapter {

    // To send a simple email
    String sendSimpleMail(Mail mail);

    // To send an email with attachment
    String sendMailWithAttachment(Mail mail);
}