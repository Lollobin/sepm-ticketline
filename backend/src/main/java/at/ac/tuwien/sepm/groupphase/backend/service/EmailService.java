package at.ac.tuwien.sepm.groupphase.backend.service;

import org.springframework.mail.SimpleMailMessage;

public interface EmailService {

    void sendEmail(SimpleMailMessage message);
}
