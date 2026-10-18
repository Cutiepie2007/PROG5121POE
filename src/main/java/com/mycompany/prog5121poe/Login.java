/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.prog5121poe;

/**
 *
 * @author Student
 */
//Holds the users information and allows user to register and login.
public class Login {
    private String firstName;
    private String lastName;
    private String username;
    private String password;
    private String cellPhoneNumber;
    private String loginUsername;
    
// Sets up the users information once the Login class has been instantiated.
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

// Ensures username has an _ in it and does not exceed 5 characters. 
public boolean checkUserName() {
    return username.contains("_") && username.length() <=5;
}

// Makes sure the password meets all criteria.
public boolean checkPasswordComplexity() {
    return password.length()>= 8
            && password.matches(".*[A-Z].*")
            && password.matches(".*\\d.*")
            && password.matches(".*[^a-zA-Z0-9].*");
}
//Regex is a regular expression. You can use regex to test if a string contains what your looking for(W3Schools,n.d).
public boolean checkCellPhoneNumber() {
    return cellPhoneNumber.matches("^\\+27\\d{9}$");
}
// Goes through every requirement to see if user can register.
public String registerUser() {
  
    if(!checkUserName()){
        return "Username is not correctly formatted; please ensure that your username contains an underscore and is no  more than five characters in length";
    }
    
    if(!checkPasswordComplexity()) {
        return "Password is not correctly formatted; please ensure that the password contains at least eight characters, a capital letter,a number, and a special character.";
    }
     
    if(!checkCellPhoneNumber()) {
        return "Cell phone number is incorrectly formatted; please insure it starts with +27 and is followed by 9 digits.";
    }
    return "Registration successful.";
    }

public void setLoginname(String loginUserName) {
    this.loginUsername = loginUserName;
}

// Compares the information entered to what is registered.
public boolean loginUser(String username, String password) {
        return username.equals(this.username)
                && password.equals(this.password);
}

// Tells the user they have successfully logged in or if they have failed. 
public String returnLoginStatus(String loginUserName, String loginPassword) {
    if (loginUser(loginUsername, loginPassword)){
        return "Welcome " +firstName+ " " +lastName+ " it is great to see you again.";
    }else{
        return "Username or password is incorrect, please try again.";
}
}
}