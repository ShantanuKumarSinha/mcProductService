package dev.shann.mcproductservice;

import dev.shann.mcproductservice.adapters.mail.impl.EmailServiceAdapter;
import dev.shann.mcproductservice.config.TestConfig;
import dev.shann.mcproductservice.model.Mail;
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
import java.util.Objects;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@ContextConfiguration(classes = TestConfig.class)
class EmailServiceTest {

    @InjectMocks
    private EmailServiceAdapter emailServiceAdapter;

    @Mock
    private JavaMailSender javaMailSender;

    @Test
    void shouldSendEmail() {
        var mailDTO = Mail.builder().recipient("test@test.com").subject("test mail").msgBody("test mail").build();
        doNothing().when(javaMailSender).send(any(SimpleMailMessage.class));
        var response = emailServiceAdapter.sendSimpleMail(mailDTO);
        assertThat(response).isNotNull().isEqualTo("Mail Sent Successfully...");
    }

    @Test
    void shouldNotSendEmail() {
        var mailDTO = Mail.builder().recipient("test@test.com").subject("test mail").msgBody("test mail").build();
        doThrow(new MailSendException("Test message")).when(javaMailSender).send(any(SimpleMailMessage.class));
        var response = emailServiceAdapter.sendSimpleMail(mailDTO);
        assertThat(response).isNotNull().isEqualTo("Error while Sending Mail");
        //assertThrows(MailSendException.class, () -> emailServiceAdapter.sendSimpleMail(mailDTO));
    }


    @Test
    void shouldVerifySentEmail() {
        var mailDTO = Mail.builder().recipient("test@test.com").subject("test subject").msgBody("test mail").build();
        ArgumentCaptor<SimpleMailMessage> simpleMailMessageArgumentCaptor = ArgumentCaptor.forClass(SimpleMailMessage.class);
        emailServiceAdapter.sendSimpleMail(mailDTO);

        verify(javaMailSender).send(simpleMailMessageArgumentCaptor.capture());
        var simpleMailMessageArgumentCaptorValue = simpleMailMessageArgumentCaptor.getValue();

        assert Arrays.asList(Objects.requireNonNull(simpleMailMessageArgumentCaptorValue.getTo())).contains("test@test.com");
        assert Arrays.stream(simpleMailMessageArgumentCaptorValue.getTo()).count() == 1;

        assert Objects.equals(simpleMailMessageArgumentCaptorValue.getSubject(), "test subject");
        assert Objects.equals(simpleMailMessageArgumentCaptorValue.getText(), "test mail");
    }
}
