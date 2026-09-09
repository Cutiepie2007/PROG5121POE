/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.prog5121poe;

/**
 *
 * @author Student
 */
public class Login {
    private String firstName;
    private String lastName;
    private String username;
    private String password;
    private String cellPhoneNumber;

public Login(String firstName, String lastName, String username, String password, String cellPhoneNumber) {
    this.firstName = firstName;
    this.lastName = lastName;
    this.password = password;
    this.username = username;
    this.cellPhoneNumber = cellPhoneNumber;
}
public boolean checkUserName() {
    return username.contains("_") && username.length() <=5;
}

public boolean checkPasswordComplexity() {
    return password.length()>= 8
            && password.matches(".*[A-Z].*")
            && password.matches(".*\\d.*")
            && password.matches(".*[^a-zA-Z0-9].*");
}
}
