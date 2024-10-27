package com.sqli.gdmr.Models;

import lombok.Data;

@Data
public class LoginResponse {
    private String accessToken;
    private Long userId;
    private String username;
    private boolean firstLogin;
    private boolean changePasswordRequired;

    public LoginResponse(String accessToken, Long userId, String username, boolean firstLogin, boolean changePasswordRequired) {
        this.accessToken = accessToken;
        this.userId = userId;
        this.username = username;
        this.firstLogin = firstLogin;
        this.changePasswordRequired = changePasswordRequired;
    }
}
