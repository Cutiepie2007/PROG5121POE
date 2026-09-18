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
    private String loginUsername;
    private String loginPassword;

public Login(String firstName, String lastName, String username, String password, String cellPhoneNumber) {
    this.firstName = firstName;
    this.lastName = lastName;
    this.password = password;
    this.username = username;
    this.cellPhoneNumber = cellPhoneNumber;
}

//Getters (Which allows reading the values)
public String getFirstname(){
    return firstName;
}

public String getLastName(){
    return lastName;
}

public String getUserName(){
    return username;
}

public String getPasword(){
    return password;
}

public String getCellPhoneNumber(){
    return cellPhoneNumber;
}

//Stetters

public void setFirstname(String firstName) {
    this.firstName = firstName;
}

public void setLastName(String lastName) {
    this.lastName = lastName;
}

public void setUsername(String username) {
    this.username = username;
}

public void setPassword(String password) {
    this.password = password;
}

public void setCellPhoneNumber(String cellPhoneNumber) {
    this.cellPhoneNumber = cellPhoneNumber;
}

public void setLoginUsername(String loginUsername) {
    this.loginUsername = loginUsername;
}

public void setLoginPassword(String loginPassword){
    this.loginPassword = loginPassword;
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

public boolean checkCellPhoneNumber() {
    return cellPhoneNumber.matches("^\\+27\\d{9}$");
}
public String registerUser() {
  
    if(!checkUserName()){
        return "Username is not correctly formatted; please ensure that your username contains an underscore and is no  more than five characters in length";
    }
    
    if(!checkPasswordComplexity()) {
        return "Password is not correctly formatted; please ensure that the password contains at least eight characters, a capital letter,a number, and a special character.";
    }
     
    if(!checkCellPhoneNumber()) {
        return "Cell phone number is not correctly; please insure it starts with +27 and is followed by 9 digits.";
    }
    return "Registration successful.";
    }

public void setLoginname(String loginUserName) {
    this.loginUsername = loginUserName;
}

public void setLoginpassword(String loginPassword) {
    this.loginPassword = loginPassword;
}
public boolean loginUser(String username, String Password) {
        return username.equals(loginUsername)
                && password.equals(loginPassword);
}

public String returnLoginStatus() {
    if (loginUser(this.loginUsername,this.loginPassword)) {
        return "Welcome" + firstName + "," + lastName + "it is great to see you again.";
    }else {
        return "Username or password incorrect, please try again.";
    }
}
}