package school.journal.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.stereotype.Service;

import java.text.MessageFormat;

@Service("MailService")
public class MailService {

//    @Autowired
    private MailSender mailSender;

    public void sendMail(String to, String password)
    {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject("School Journal app");
        message.setText(MessageFormat.format("Hello, your password for School Journal app is {0}", password));
        mailSender.send(message);
    }

}
