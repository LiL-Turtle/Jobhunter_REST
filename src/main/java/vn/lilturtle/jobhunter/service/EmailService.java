package vn.lilturtle.jobhunter.service;

import org.springframework.mail.SimpleMailMessage;
import org.springframework.stereotype.Service;
import org.springframework.mail.MailSender;

@Service
public class EmailService {

    private final MailSender mailSender;

    public EmailService(MailSender mailSender) {
        this.mailSender = mailSender;
    }

    public void sendSimleEmail() {
        SimpleMailMessage msg = new SimpleMailMessage();
        msg.setFrom("baothien210704@gmail.com");
        msg.setTo("baothien9900@gmail.com");
        msg.setSubject("Testing from Spring Boot");
        msg.setText("Hello World from Spring Boot Email");
        this.mailSender.send(msg);
    }
}