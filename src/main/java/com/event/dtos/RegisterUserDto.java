package com.event.dtos;

import com.event.Model.Role;

public class RegisterUserDto {
    private String email;
    private String password;
    private String username;
    private Role role;

    public Role getRole() {
        return role;
    }


    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }


}
