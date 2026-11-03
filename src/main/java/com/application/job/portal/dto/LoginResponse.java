
package com.application.job.portal.dto;

public class LoginResponse {

    private String token;
    private String tokenType;
    private String message;

    public LoginResponse(String token, String tokenType, String message) {
        this.token = token;
        this.tokenType = tokenType;
        this.message = message;
    }

    public String getToken() {
        return token;
    }

    public String getTokenType() {
        return tokenType;
    }

    public String getMessage() {
        return message;
    }
}
