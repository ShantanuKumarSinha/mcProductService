package dev.shann.mcproductservice.adapters.mail;

import dev.shann.mcproductservice.model.Mail;

/**
 * MailAdapter interface to define methods for sending emails.
 * This interface can be implemented by different mail service providers.
 *
 * @author shann
 * @version 1.0
 */
public interface MailAdapter {

    /** To send a simple email
     *
     * @param mail
     * @return
     */
    String sendSimpleMail(Mail mail);

    /** To send an email with an attachment
     *
     * @param mail
     * @return
     */
    String sendMailWithAttachment(Mail mail);
}