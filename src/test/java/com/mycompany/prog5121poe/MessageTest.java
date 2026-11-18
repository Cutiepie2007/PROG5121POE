package com.mycompany.prog5121poe;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class MessageTest {

    // Test 1: Check message ID
    @Test
    public void testCheckMessageID() {

        Message message = new Message(
                0, "+27831234567", "Hello there");

        assertTrue(message.checkMessageID());
        assertEquals(10, message.getMessageID().length());
    }

    // Test 2: Check valid recipient cellphone number
    @Test
    public void testCheckRecipientCell() {

        Message message = new Message(
                0, "+27831234567", "Hello there");

        assertEquals(
                "Cell phone number successfully captured.",
                message.checkRecipientCell());
    }

    // Test 3: Check invalid recipient cellphone number
    @Test
    public void testInvalidRecipientCell() {

        Message message = new Message(
                0, "0831234567", "Hello there");

        assertTrue(message.checkRecipientCell()
                .contains("incorrectly formatted"));
    }

    // Test 4: Check message length
    @Test
    public void testMessageLength() {

        Message message = new Message(
                0, "+27831234567", "Hello there");

        assertEquals(
                "Message ready to send.",
                message.checkMessageLength());
    }

    // Test 5: Check message exceeding 250 characters
    @Test
    public void testMessageExceeds250Characters() {

        String longMessage = "";

        for (int i = 0; i < 251; i++) {
            longMessage = longMessage + "A";
        }

        Message message = new Message(
                0, "+27831234567", longMessage);

        assertTrue(message.checkMessageLength()
                .contains("exceeds 250 characters"));
    }

    // Test 6: Check message hash
    @Test
    public void testCreateMessageHash() {

        Message message = new Message(
                0, "+27831234567", "Hello there");

        String hash = message.createMessageHash();

        assertEquals(":0:HELLOTHERE", hash.substring(2));

        assertTrue(hash.charAt(0) >= '0'
                && hash.charAt(0) <= '9');

        assertTrue(hash.charAt(1) >= '0'
                && hash.charAt(1) <= '9');
    }

    // Test 7: Test sending a message
    @Test
    public void testSendMessage() {

        Message message = new Message(
                0, "+27831234567", "Hello there");

        assertEquals(
                "Message successfully sent.",
                message.SentMessage(1));

        assertEquals("Sent", message.getStatus());
        assertEquals(1, message.returnTotalMessagess());
    }

    // Test 8: Test disregarding a message
    @Test
    public void testDisregardMessage() {

        Message message = new Message(
                0, "+27831234567", "Hello there");

        assertEquals(
                "Press 0 to delete the message.",
                message.SentMessage(2));

        assertEquals("Disregarded", message.getStatus());
        assertEquals(0, message.returnTotalMessagess());
    }

    // Test 9: Test storing a message
    @Test
    public void testStoreMessage() {

        Message message = new Message(
                0, "+27831234567", "Hello there");

        assertEquals(
                "Message successfully stored.",
                message.SentMessage(3));

        assertEquals("Stored", message.getStatus());
        assertEquals(0, message.returnTotalMessagess());
    }

    // Test 10: Test printing message details
    @Test
    public void testPrintMessages() {

        Message message = new Message(
                0, "+27831234567", "Hello there");

        String output = message.printMessages();

        assertTrue(output.contains("Message ID:"));
        assertTrue(output.contains("Message Hash:"));
        assertTrue(output.contains("Recipient:"));
        assertTrue(output.contains("Message:"));
    }
}