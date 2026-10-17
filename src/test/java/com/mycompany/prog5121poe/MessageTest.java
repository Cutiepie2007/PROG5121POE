package com.mycompany.prog5121poe;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class MessageTest {

    @Test
    public void testCheckMessageID() {
        Message message = new Message(
                0, "+27831234567", "Hello there"
        );

        assertTrue(message.checkMessageID());
        assertEquals(10, message.getMessageID().length());
    }