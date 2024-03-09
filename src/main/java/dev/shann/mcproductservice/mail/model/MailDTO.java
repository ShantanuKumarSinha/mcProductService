package dev.shann.mcproductservice.mail.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder

public class MailDTO {

    // Class data members
    private String recipient;
    private String msgBody;
    private String subject;
    private String attachment;
}