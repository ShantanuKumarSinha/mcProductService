package dev.shann.mcproductservice;

import dev.shann.mcproductservice.config.TestConfig;
import dev.shann.mcproductservice.mail.model.MailDTO;
import dev.shann.mcproductservice.mail.producer.impl.EmailService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mail.MailSendException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.test.context.ContextConfiguration;

import java.util.Arrays;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@ContextConfiguration(classes = TestConfig.class)
class EmailServiceTest {

    @InjectMocks
    private EmailService emailService;

    @Mock
    private JavaMailSender javaMailSender;

    @Test
    void shouldSendEmail() {
        var mailDTO = MailDTO.builder().recipient("test@test.com").subject("test mail").msgBody("test mail").build();
        doNothing().when(javaMailSender).send(any(SimpleMailMessage.class));
        var response = emailService.sendSimpleMail(mailDTO);
        assertThat(response).isNotNull().isEqualTo("Mail Sent Successfully...");
    }

    @Test
    void shouldNotSendEmail() {
        var mailDTO = MailDTO.builder().recipient("test@test.com").subject("test mail").msgBody("test mail").build();
        doThrow(new MailSendException("Test message")).when(javaMailSender).send(any(SimpleMailMessage.class));
        var response = emailService.sendSimpleMail(mailDTO);
        assertThat(response).isNotNull().isEqualTo("Error while Sending Mail");
//          assertThrows(MailSendException.class, () ->emailService.sendSimpleMail(mailDTO));
    }


    @Test
    void shouldVerifySentEmail() {
        var mailDTO = MailDTO.builder().recipient("test@test.com").subject("test mail").msgBody("test mail").build();
        ArgumentCaptor<SimpleMailMessage> simpleMailMessageArgumentCaptor = ArgumentCaptor.forClass(SimpleMailMessage.class);
        emailService.sendSimpleMail(mailDTO);
        verify(javaMailSender).send(simpleMailMessageArgumentCaptor.capture());
        var simpleMailMessageArgumentCaptorValue = simpleMailMessageArgumentCaptor.getValue();
        assert Arrays.stream(simpleMailMessageArgumentCaptorValue.getTo()).anyMatch(to -> to.equals("test@test.com"));
        assert simpleMailMessageArgumentCaptorValue.getTo()[0].equals("test@test.com");
        assert simpleMailMessageArgumentCaptorValue.getSubject().equals("test mail");
        assert simpleMailMessageArgumentCaptorValue.getText().equals("test mail");
    }
}
