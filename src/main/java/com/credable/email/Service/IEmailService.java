package com.credable.email.Service;

import com.credable.email.Response.Response;
import com.credable.email.dto.UserDetails;

import javax.mail.MessagingException;
import java.io.IOException;

public interface IEmailService {
    Response saveEmail(UserDetails userDetails) throws MessagingException, IOException;
}
