package com.mycompany.prog5121poe;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class LoginTest {

    @Test
    public void testCheckUserName() {

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
    public void testCheckPasswordComplexity() {

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
    public void testCheckCellPhoneNumber() {

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
    public void testRegistrationResult() {

        Login user = new Login(
                "John",
                "Smith",
                "kyl_1",
                "Ch&&sec@ke99!",
                "+27838668976"
        );

        assertEquals(
                "Registration successful.",
                user.registerUser()
        );
    }

    @Test
    public void testInvalidUserName() {

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
    public void testInvalidPassword() {

        Login user = new Login(
                "John",
                "Smith",
                "kyl_1",
                "password",
                "+27838968976"
        );

        assertFalse(user.checkPasswordComplexity());
    }

    @Test
    public void testInvalidCellPhoneNumber() {

        Login user = new Login(
                "John",
                "Smith",
                "kyl_1",
                "Ch&&sec@ke99!",
                "083968976"
        );

        assertFalse(user.checkCellPhoneNumber());
    }

    @Test
    public void testSuccessfulLogin() {

        Login user = new Login(
                "John",
                "Smith",
                "kyl_1",
                "Ch&&sec@ke99!",
                "+27838668976"
        );

        assertTrue(
                user.loginUser(
                        "kyl_1",
                        "Ch&&sec@ke99!"
                )
        );
    }

    @Test
    public void testFailedLogin() {

        Login user = new Login(
                "John",
                "Smith",
                "kyl_1",
                "Ch&&sec@ke99!",
                "+27838668976"
        );

        assertFalse(
                user.loginUser(
                        "wrong",
                        "wrong"
                )
        );
    }

    @Test
    public void testSuccessfulLoginStatus() {

        Login user = new Login(
                "John",
                "Smith",
                "kyl_1",
                "Ch&&sec@ke99!",
                "+27838668976"
        );

        assertEquals(
                "Welcome John, Smith it is great to see you again.",
                user.returnLoginStatus(
                        "kyl_1",
                        "Ch&&sec@ke99!"
                )
        );
    }

    @Test
    public void testFailedLoginStatus() {

        Login user = new Login(
                "John",
                "Smith",
                "kyl_1",
                "Ch&&sec@ke99!",
                "+27838668976"
        );

        assertEquals(
                "Username or password incorrect, please try again.",
                user.returnLoginStatus(
                        "wrong",
                        "wrong"
                )
        );
    }
}