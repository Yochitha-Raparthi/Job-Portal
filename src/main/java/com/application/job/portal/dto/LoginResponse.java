
package com.application.job.portal.dto;

public class LoginResponse {

    private String token;
    private String tokenType;
    private String message;
    private String role;
    private String name;
    public LoginResponse(
            String token,
            String tokenType,
            String message,
            String role,
            String name) {

        this.token = token;
        this.tokenType = tokenType;
        this.message = message;
        this.role = role;
        this.name = name;
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
    public String getRole() {
        return role;
    }

    public String getName() {
        return name;
    }
}
