/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

package com.mycompany.prog5121poe;

/**
 *
 * @author Student
 */
public class Main {

    public static void main(String[] args) {
        
       Login user = new Login (
                "Saaheerah",
                "Majiet",
                "saa_1",
                "Password1!",
                "+27680343190"
        );
       
       System.out.println(user.registerUser());
       System.out.println(user.loginUser());
       System.out.println(user.returnLoginStatus());
    }
}
