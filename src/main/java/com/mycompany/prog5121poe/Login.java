package com.mycompany.prog5121poe;

public class Login {

    private String firstName;
    private String lastName;
    private String username;
    private String password;
    private String cellPhoneNumber;

    public Login(String firstName, String lastName, String username,
            String password, String cellPhoneNumber) {

        this.firstName = firstName;
        this.lastName = lastName;
        this.username = username;
        this.password = password;
        this.cellPhoneNumber = cellPhoneNumber;
    }

    // Getters
    public String getFirstname() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getUserName() {
        return username;
    }

    public String getPasword() {
        return password;
    }

    public String getCellPhoneNumber() {
        return cellPhoneNumber;
    }

    // Setters
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

    // Check username
    public boolean checkUserName() {

        return username != null
                && username.contains("_")
                && username.length() <= 5;
    }

    // Check password
    public boolean checkPasswordComplexity() {

        if (password == null || password.length() < 8) {
            return false;
        }

        boolean hasCapitalLetter = false;
        boolean hasNumber = false;
        boolean hasSpecialCharacter = false;

        for (int i = 0; i < password.length(); i++) {

            char character = password.charAt(i);

            if (character >= 'A' && character <= 'Z') {
                hasCapitalLetter = true;
            } else if (character >= '0' && character <= '9') {
                hasNumber = true;
            } else if (!(character >= 'a' && character <= 'z')
                    && character != ' ' && character != '\t') {
                hasSpecialCharacter = true;
            }
        }

        return hasCapitalLetter && hasNumber
                && hasSpecialCharacter;
    }

    // Check cellphone number
    public boolean checkCellPhoneNumber() {

        if (cellPhoneNumber == null
                || cellPhoneNumber.length() != 12
                || !cellPhoneNumber.startsWith("+27")) {
            return false;
        }

        for (int i = 3; i < cellPhoneNumber.length(); i++) {

            char digit = cellPhoneNumber.charAt(i);

            if (digit < '0' || digit > '9') {
                return false;
            }
        }

        return true;
    }

    // Register user
    public String registerUser() {

        if (!checkUserName()) {
            return "Username is not correctly formatted; please ensure that your username contains an underscore and is no more than five characters in length.";
        }

        if (!checkPasswordComplexity()) {
            return "Password is not correctly formatted; please ensure that the password contains at least eight characters, a capital letter, a number, and a special character.";
        }

        if (!checkCellPhoneNumber()) {
            return "Cell phone number is incorrectly formatted; please ensure it starts with +27 and is followed by 9 digits.";
        }

        return "Registration successful.";
    }

    // Check login details
    public boolean loginUser(String username, String password) {

        return username != null
                && password != null
                && username.equals(this.username)
                && password.equals(this.password);
    }

    // Return login status
    public String returnLoginStatus(String loginUserName,
            String loginPassword) {

        if (loginUser(loginUserName, loginPassword)) {

            return "Welcome " + firstName + " " + lastName
                    + " it is great to see you again.";

        } else {

            return "Username or password is incorrect, please try again.";
        }
    }
}