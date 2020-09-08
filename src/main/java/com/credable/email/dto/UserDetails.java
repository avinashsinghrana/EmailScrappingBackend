package com.credable.email.dto;

import lombok.Data;

@Data
public class UserDetails {
    private String user;
    private String password;
    private int hours;
    private boolean type;
}
