package at.ac.tuwien.sepm.groupphase.backend.unittests;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

import at.ac.tuwien.sepm.groupphase.backend.service.MailBuilderService;
import at.ac.tuwien.sepm.groupphase.backend.service.impl.MailBuilderServiceImpl;
import java.net.URI;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mail.SimpleMailMessage;

@ExtendWith(MockitoExtension.class)
class MailBuilderServiceTest {

    private static final String SUBJECT_RESET_PASSWORD_MAIL = "Ticketline 4.0: Reset Password";
    private static final String CONTENT_RESET_PASSWORD_MAIL = "A password reset was requested for your account on ticketline, please click this link to reset your password: ";
    private static final String MAIL_FROM = "ticketline.2000@gmail.com";


    MailBuilderService mailBuilderService;
    String exampleMail = "test@email.com";
    URI exampleUri = URI.create("http://localhost:4200/test");

    @BeforeEach
    void setUp() {
        mailBuilderService = new MailBuilderServiceImpl();

    }

    @Test
    void buildPasswordResetMail_givenInputBuildSimpleMailMessage() {
        SimpleMailMessage msg = mailBuilderService.buildPasswordResetMail(exampleMail, exampleUri);

        assertAll(
            () -> assertEquals(MAIL_FROM, msg.getFrom()),
            () -> assertEquals(exampleMail, msg.getTo()[0]),
            () -> assertEquals(SUBJECT_RESET_PASSWORD_MAIL, msg.getSubject()),
            () -> assertEquals(CONTENT_RESET_PASSWORD_MAIL + exampleUri.toString(), msg.getText())
        );
    }


}
