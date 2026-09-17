package com.mycompany.prog5121poe;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;
public class LoginTest {
    
    @Test
    public void testCheckUserName(){
    Login user = new Login(
                "John",
                "Smith",
                "kyl_1",
                "Ch&&sec@ke99!",
                "+27838668976"
        );
    assertTrue(user.checkUserName());
    }
    @Test
    public void testCheckPasswordComplexity(){
        Login user = new Login(
                "John",
                "Smith",
                "kyl_1",
                "Ch&&sec@ke99!",
                "+27838668976"
        );
        
        assertTrue(user.checkPasswordComplexity());
    }
    
    @Test
    public void testCheckCellPhoneNumber(){
        Login user = new Login(
                "John",
                "Smith",
                "kyl_1",
                "Ch&&sec@ke99!",
                "+27838668976"
        );
        
        assertTrue(user.checkCellPhoneNumber());
    }
    
    @Test
    public void testRegistrationResult(){
        Login user = new Login(
                "John",
                "Smith",
                "kyl_1",
                "Ch&&sec@ke99!",
                "+27838668976"
        );
        
        assertTrue(user.checkUserName());
        assertTrue(user.checkPasswordComplexity());
        assertTrue(user.checkCellPhoneNumber());
    }
    
    @Test
    public void testInvalidUserName(){
        Login user = new Login(
                "John",
                "Smith",
                "kyl1",
                "Ch&&sec@ke99!",
                "+27838968976"
        );
        
        assertFalse(user.checkUserName());
    }
    
    @Test
    public void testInvalidPassword(){
        Login user = new Login(
                "John",
                "Smith",
                "kyl_1",
                "password",
                "+2783896879"
        );
        
        assertFalse(user.checkPasswordComplexity());
    }
}
