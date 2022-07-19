package com.fedebonel.recipemvc.services;

import com.fedebonel.recipemvc.controllers.UserController;
import com.fedebonel.recipemvc.model.User;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.UnsupportedEncodingException;

@Service
public class MailServiceImpl implements MailService {

    public static final String AMBRO_BOOK_USER_SUPPORT = "AmbroBook User Support";
    public static final String VERIFICATION_SUBJECT = "Please verify your registration for AmbroBook";
    private final JavaMailSender mailSender;

    @Value("${spring.mail.username}")
    private String originEmail;

    public MailServiceImpl(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    @Override
    @Async
    public void sendVerificationEmail(User user, String siteURL) throws MessagingException, UnsupportedEncodingException {
        String toAddress = user.getEmail();
        String content = "Hello [[name]],<br> "
                + "\uD83E\uDD73 Thank you for signing up to AmbroBook \uD83E\uDD73 <br>"
                + "Please click the link below to complete your registration and begin your delicious journey:<br>"
                + "<h3><a href=\"[[URL]]\" target=\"_self\">VERIFY</a></h3>"
                + "We can't wait to see what recipes you have in mind,<br><br>"
                + "User Support, AmbroBook.";

        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message);

        helper.setFrom(originEmail, AMBRO_BOOK_USER_SUPPORT);
        helper.setTo(toAddress);
        helper.setSubject(VERIFICATION_SUBJECT);

        content = content.replace("[[name]]", user.getUsername());
        String verifyURL = siteURL + UserController.USER_URI + "/verify?code=" + user.getVerificationCode();

        content = content.replace("[[URL]]", verifyURL);

        helper.setText(content, true);

        mailSender.send(message);
    }
}
