package com.sqli.gdmr.Models;

import lombok.Data;

@Data
public class LoginRequest {
    private String username;
    private String password;
}
