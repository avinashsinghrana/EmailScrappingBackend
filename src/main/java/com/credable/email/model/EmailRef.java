package com.credable.email.model;

import lombok.Data;

@Data
public class EmailRef {
    private String subject;
    private String time;
    private String sender;
    private String text;
}
