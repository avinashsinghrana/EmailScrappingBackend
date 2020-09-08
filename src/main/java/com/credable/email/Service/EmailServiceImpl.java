package com.credable.email.Service;

import com.credable.email.Response.Response;
import com.credable.email.dto.UserDetails;
import com.credable.email.model.EmailRef;
import com.credable.email.utility.ReadMessage;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EmailServiceImpl implements IEmailService {

    @Override
    public Response saveEmail(UserDetails userDetails){
        System.out.println("request enter to service implementation");
        ReadMessage readMessage = new ReadMessage(userDetails);
        List<EmailRef> emails = readMessage.getLastHoursMails(userDetails.getHours(), userDetails.isType());
        if (!emails.isEmpty())
            return new Response(HttpStatus.OK.value(), "emails are ready", emails);
        return new Response(HttpStatus.NOT_FOUND.value(), "emails not Found");
    }
}
