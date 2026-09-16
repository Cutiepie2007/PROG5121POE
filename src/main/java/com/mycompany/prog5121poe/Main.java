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
            System.out.println("Registration successful");
        }
        
        else {
         System.out.println("Registration failed. Please check your details");
        }
            
    scanner.close();
    }
    }
