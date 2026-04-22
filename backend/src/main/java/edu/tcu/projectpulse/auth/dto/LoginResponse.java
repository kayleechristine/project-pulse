package edu.tcu.projectpulse.auth.dto;

import edu.tcu.projectpulse.user.User;
import edu.tcu.projectpulse.user.UserRole;

import java.util.Set;

public class LoginResponse {

    private String token;
    private Integer userId;
    private String email;
    private String firstName;
    private String lastName;
    private Set<UserRole> roles;

    public LoginResponse(String token, User user) {
        this.token = token;
        this.userId = user.getId();
        this.email = user.getEmail();
        this.firstName = user.getFirstName();
        this.lastName = user.getLastName();
        this.roles = user.getRoles();
    }

    public String getToken() {
        return token;
    }

    public Integer getUserId() {
        return userId;
    }

    public String getEmail() {
        return email;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public Set<UserRole> getRoles() {
        return roles;
    }
}
