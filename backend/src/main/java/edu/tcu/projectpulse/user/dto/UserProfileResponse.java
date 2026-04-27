package edu.tcu.projectpulse.user.dto;

import edu.tcu.projectpulse.user.User;
import edu.tcu.projectpulse.user.UserRole;

import java.util.Set;

public class UserProfileResponse {

    private Integer id;
    private String firstName;
    private String lastName;
    private String email;
    private Set<UserRole> roles;

    public UserProfileResponse(User user) {
        this.id = user.getId();
        this.firstName = user.getFirstName();
        this.lastName = user.getLastName();
        this.email = user.getEmail();
        this.roles = user.getRoles();
    }

    public Integer getId() {
        return id;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getEmail() {
        return email;
    }

    public Set<UserRole> getRoles() {
        return roles;
    }
}
