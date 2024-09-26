package com.L2.dto;

public class UserDTO {
    int id;
    String firstName;
    String lastName;
    String email;
    String sesa;
    String profileLink;

    public UserDTO(int id, String firstName, String lastName, String email, String sesa, String profileLink) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.sesa = sesa;
        this.profileLink = profileLink;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFullName() {
        return firstName + " " + lastName;
    }

    public String getProfileLink() {
        return profileLink;
    }

    public void setProfileLink(String profileLink) {
        this.profileLink = profileLink;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getSesa() {
        return sesa;
    }

    public void setSesa(String sesa) {
        this.sesa = sesa;
    }
}
