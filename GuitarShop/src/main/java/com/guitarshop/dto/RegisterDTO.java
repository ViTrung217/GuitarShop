package com.guitarshop.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

public class RegisterDTO {

    @NotBlank
    private String username;

    @NotBlank
    private String password;

    @NotBlank
    private String fullName;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }
}

