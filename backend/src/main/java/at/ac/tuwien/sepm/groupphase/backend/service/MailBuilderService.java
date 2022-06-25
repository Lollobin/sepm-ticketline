package at.ac.tuwien.sepm.groupphase.backend.service;

import java.net.URI;
import org.springframework.mail.SimpleMailMessage;

public interface MailBuilderService {

    SimpleMailMessage buildPasswordResetMail(String recipientMail, URI resetUri);

    SimpleMailMessage buildAccountLockedMail(String email);
}
