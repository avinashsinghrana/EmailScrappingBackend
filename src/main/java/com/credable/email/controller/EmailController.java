package com.credable.email.controller;

import com.credable.email.Response.Response;
import com.credable.email.Service.IEmailService;
import com.credable.email.dto.UserDetails;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.mail.MessagingException;
import java.io.IOException;

@RestController
@RequestMapping("/e")
public class EmailController {
    @Autowired
    private IEmailService emailService;

    @PutMapping("/req_id")
    public ResponseEntity<Response> saveEmail(@RequestBody UserDetails userDetails) throws IOException, MessagingException {
        Response response = emailService.saveEmail(userDetails);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

//    @ApiOperation("find mails")
//    @GetMapping("/{timeLimit}/fetch")
//    public ResponseEntity<Response> fetchEmail(@PathVariable String timeLimit){
//        Response response = emailService.fetchEmail(timeLimit);
//        return ResponseEntity.status(HttpStatus.OK).body(response);
//    }

}



