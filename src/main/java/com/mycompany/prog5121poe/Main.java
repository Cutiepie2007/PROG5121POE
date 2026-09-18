/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

package com.mycompany.prog5121poe;

import java.util.Scanner;

public class Main {
    public static void main(String[] args){
        Scanner scanner = new Scanner(System.in);
        
        System.out.println("================================");
        System.out.println("QUICKCHAT REGISTRATION");
        System.out.println("================================");
        
        System.out.print("Enter your first name; ");
        String firstName =scanner.nextLine();
        
        System.out.print("Enter your last name; ");
        String lastName =scanner.nextLine();
        
        System.out.print("Enter your username; ");
        String username =scanner.nextLine();
        
        System.out.print("Enter your password; ");
        String password =scanner.nextLine();
        
        System.out.print("Enter your cell phone number; ");
        String cellPhoneNumber =scanner.nextLine();
        
        Login login = new Login(firstName, lastName, username, password, cellPhoneNumber);
        System.out.println(login.registerUser());
        
        System.out.println("================================");
        System.out.println("REGISTRATION RESULTS");
        System.out.println("================================");

        if(login.checkUserName()){
            System.out.println("Username successfully captured");
        }
        else{
            System.out.println("Username is incorrcetly formattted");
        }
        
        if(login.checkPasswordComplexity()) {
            System.out.println("Password successfully captured.");
        }
        
        else {
        System.out.println("Password is incorrcetly formattted");
         }
     
        if(login.checkCellPhoneNumber()){
            System.out.println("Cell phone number successfully captured");
        }
        
        else {
         System.out.println("Cell phone number is incorrectly formatted");
        }
          
        System.out.println("================================");
        System.out.println("LOGIN");
        System.out.println("================================");

        if(login.checkUserName() && login.checkPasswordComplexity() && login.checkCellPhoneNumber()) {
            System.out.println("Registration successful.");
           
            System.out.print("Enter username:");
            String loginUsername = scanner.nextLine();
            
            System.out.print("Enter password:");
            String loginPassword = scanner.nextLine();
            
    if (login.loginUser(loginUsername, loginPassword)) {
        System.out.println("Welcome" + firstName + "," + lastName + "it is great to see you again.");
    }else {
        System.out.println("Username or Password incorrect, please try agin");
        }
    } else {
            System.out.println("Registration failed. Please check your details");
        }
    scanner.close();
    }
    }
