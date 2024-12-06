package com.event.dtos;

import com.event.Model.Role;

public class LoginUserDto {
    private String username;

    private String password;

    public String getPassword() {
        return password;
    }
    private Role role;

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
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
