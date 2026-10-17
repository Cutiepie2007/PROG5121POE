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

    @Test
    public void testMessageLength() {
        Message message = new Message(
                0, "+27831234567", "Hello there"
        );

        assertEquals(
                "Message ready to send.",
                message.checkMessageLength()
        );
    }
    
    @Test
    public void testMessageExceeds250Characters() {
        String longMessage = "A".repeat(251);

        Message message = new Message(
                0, "+27831234567", longMessage
        );

        assertTrue(
                message.checkMessageLength()
                        .contains("exceeds 250 characters")
        );
    }

    @Test
    public void testCreateMessageHash() {
        Message message = new Message(
                0, "+27831234567", "Hello there"
        );

        String hash = message.createMessageHash();

        assertTrue(hash.matches("\\d{2}:0:HELLOTHERE"));
    }

    @Test
    public void testSendMessage() {
        Message message = new Message(
                0, "+27831234567", "Hello there"
        );

        assertEquals(
                "Message successfully sent.",
                message.SentMessage(1)
        );

        assertEquals("Sent", message.getStatus());
        assertEquals(1, message.returnTotalMessagess());
    }

    @Test
    public void testDisregardMessage() {
        Message message = new Message(
                0, "+27831234567", "Hello there"
        );

        assertEquals(
                "Press 0 to delete the message.",
                message.SentMessage(2)
        );

        assertEquals("Disregarded", message.getStatus());
        assertEquals(0, message.returnTotalMessagess());
    }

    @Test
    public void testStoreMessage() {
        Message message = new Message(
                0, "+27831234567", "Hello there"
        );

        assertEquals(
                "Message successfully stored.",
                message.SentMessage(3)
        );

        assertEquals("Stored", message.getStatus());
        assertEquals(0, message.returnTotalMessagess());
    }

    @Test
    public void testPrintMessages() {
        Message message = new Message(
                0, "+27831234567", "Hello there"
        );

        String result = message.printMessages();

        assertTrue(result.contains("Message ID:"));
        assertTrue(result.contains("Message Hash:"));
        assertTrue(result.contains("Recipient: +27831234567"));
        assertTrue(result.contains("Message: Hello there"));
    }
}