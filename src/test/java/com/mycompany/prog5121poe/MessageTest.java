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
    
 @Test
    public void testCheckRecipientCell() {
        Message message = new Message(
                0, "+27831234567", "Hello there"
        );

        assertEquals(
                "Cell phone number successfully captured.",
                message.checkRecipientCell()
        );
    }

    @Test
    public void testInvalidRecipientCell() {
        Message message = new Message(
                0, "0831234567", "Hello there"
        );

        assertTrue(
                message.checkRecipientCell()
                        .contains("incorrectly formatted")
        );
    }
}