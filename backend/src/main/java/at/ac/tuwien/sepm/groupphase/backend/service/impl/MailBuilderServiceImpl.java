package at.ac.tuwien.sepm.groupphase.backend.service.impl;

import at.ac.tuwien.sepm.groupphase.backend.service.MailBuilderService;
import java.lang.invoke.MethodHandles;
import java.net.URI;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.stereotype.Service;

@Service
public class MailBuilderServiceImpl implements MailBuilderService {

    private static final String SUBJECT_RESET_PASSWORD_MAIL = "Ticketline 4.0: Reset Password";
    private static final String CONTENT_RESET_PASSWORD_MAIL =
        "A password reset was requested for your account on ticketline, "
            + "please click this link to reset your password: ";
    private static final String CONTENT_ACCOUNT_LOCKED_MAIL =
        "A password reset was requested for your account on ticketline, "
            + "unfortunately it seems your account has been locked. \n "
            + "To unlock it please contact an administrator,"
            + " who will then send you a password reset mail again";

    private static final String MAIL_FROM = "ticketline.2022@gmail.com";


    private static final Logger LOGGER = LoggerFactory.getLogger(
        MethodHandles.lookup().lookupClass());


    @Override
    public SimpleMailMessage buildPasswordResetMail(
        String recipientMail, URI resetUri) {
        LOGGER.debug("Build resetmail for {} wih url {}", recipientMail, resetUri);
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(recipientMail);
        message.setSubject(SUBJECT_RESET_PASSWORD_MAIL);
        message.setText(CONTENT_RESET_PASSWORD_MAIL + resetUri);
        message.setFrom(MAIL_FROM);
        return message;
    }

    @Override
    public SimpleMailMessage buildAccountLockedMail(String email) {
        LOGGER.debug("Build account locked mail for {}", email);
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(email);
        message.setSubject(SUBJECT_RESET_PASSWORD_MAIL);
        message.setText(CONTENT_ACCOUNT_LOCKED_MAIL);
        message.setFrom(MAIL_FROM);
        return message;
    }
}
