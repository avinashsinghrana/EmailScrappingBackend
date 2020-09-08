package com.credable.email.controller;

import com.credable.email.Response.Response;
import com.credable.email.Service.IEmailService;
import com.credable.email.dto.UserDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.mail.MessagingException;
import java.io.IOException;

@RestController
@RequestMapping("/e")
@CrossOrigin(allowedHeaders = "*", origins = "*")
public class EmailController {
    @Autowired
    private IEmailService emailService;

    @PostMapping("/req_id")
    public ResponseEntity<Response> saveEmail(@RequestBody UserDetails userDetails) throws IOException, MessagingException {
        System.out.println("request enter to controller");
        Response response = emailService.saveEmail(userDetails);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
}



