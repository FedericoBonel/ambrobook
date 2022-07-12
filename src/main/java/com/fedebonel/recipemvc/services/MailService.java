package com.fedebonel.recipemvc.services;

import com.fedebonel.recipemvc.model.User;

import javax.mail.MessagingException;
import java.io.UnsupportedEncodingException;

public interface MailService {
    void sendVerificationEmail(User user, String siteUrl) throws MessagingException, UnsupportedEncodingException;
}
