package com.L2.dto;

public class UserDTO {
    String firstName;
    String lastName;
    String email;
    String sesa;

    public UserDTO(String firstName, String lastName, String email, String sesa) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.sesa = sesa;
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
