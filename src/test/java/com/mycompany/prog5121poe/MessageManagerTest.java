package com.mycompany.prog5121poe;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.io.File;
import java.io.FileWriter;

public class MessageManagerTest {

    private File jsonFile;
    private MessageManager manager;
    private Message message1;
    private Message message2;
    private Message message3;
    private Message message4;
    private Message message5;

    @BeforeEach
    public void setUp() throws Exception {
        jsonFile = new File("stored_messages_test.json");
        try (FileWriter writer = new FileWriter(jsonFile)) {
            writer.write("[]");
        }
        manager = new MessageManager(jsonFile);

        // Test data
        message1 = new Message(0, "0838884567",
                "+27834557896", "Did you get the cake?");
        message1.SentMessage(1);

        message2 = new Message(1, "0838884567",
                "+27838884567",
                "Where are you? You are late! I have asked you to be on time.");
        message2.SentMessage(3);

        message3 = new Message(2, "0838884567",
                "+27834484567", "Yohoooo, I am at your gate.");
        message3.SentMessage(2);
        
        message4 = new Message(3, "0838884567",
                "Not specified", "It is dinner time !");
        
        message4.setMessageID("0838884567");
        message4.SentMessage(1);

        message5 = new Message(4, "0838884567",
                "+27838884567", "Ok, I am leaving without you.");
        message5.SentMessage(3);

        manager.addMessage(message1);
        manager.addMessage(message2);
        manager.addMessage(message3);
        manager.addMessage(message4);
        manager.addMessage(message5);
    }

    @AfterEach
    public void cleanUp() {
        if (jsonFile.exists()) {
            jsonFile.delete();
        }
    }

    @Test
    public void testSentMessagesArrayIsCorrectlyPopulated() {
        Message[] sent = manager.getSentMessages();

        assertEquals(2, sent.length);
        assertEquals("Did you get the cake?", sent[0].getMessageText());
        assertEquals("It is dinner time !", sent[1].getMessageText());
    }

    @Test
    public void testDisregardedMessagesArrayIsCorrectlyPopulated() {
        Message[] disregarded = manager.getDisregardedMessages();

        assertEquals(1, disregarded.length);
        assertEquals("Yohoooo, I am at your gate.",
                disregarded[0].getMessageText());
    }

    @Test
    public void testStoredMessagesArrayIsCorrectlyPopulated() {
        Message[] stored = manager.getStoredMessages();

        assertEquals(2, stored.length);
        assertEquals("Where are you? You are late! I have asked you to be on time.",
                stored[0].getMessageText());
        assertEquals("Ok, I am leaving without you.",
                stored[1].getMessageText());
    }

    @Test
    public void testMessageHashAndIDArraysAreCorrectlyPopulated() {
        assertEquals(5, manager.getMessageHashes().length);
        assertEquals(5, manager.getMessageIDs().length);
        assertEquals(message1.getMessageHash(), manager.getMessageHashes()[0]);
        assertEquals(message4.getMessageID(), manager.getMessageIDs()[3]);
    }

    @Test
    public void testDisplaySenderAndRecipientOfStoredMessages() {
        String result = manager.displayStoredSendersAndRecipients();

        assertTrue(result.contains("Sender: 0838884567"));
        assertTrue(result.contains("Recipient: +27838884567"));
    }

    @Test
    public void testDisplayLongestStoredMessage() {
        assertEquals(
                "Where are you? You are late! I have asked you to be on time.",
                manager.getLongestStoredMessage());
    }

    @Test
    public void testSearchForMessageID() {
        String result = manager.searchMessageByID(message4.getMessageID());

        assertEquals("Recipient: Not specified\nMessage: It is dinner time !",
                result);
    }

    @Test
    public void testSearchStoredMessagesForRecipient() {
        String result = manager.searchMessagesForRecipient("+27838884567");

        assertEquals("""
                     Where are you? You are late! I have asked you to be on time.
                     Ok, I am leaving without you.
                     """, result);
    }

    @Test
    public void testSearchIncludesSentMessagesForRecipient() {
        String result = manager.searchMessagesForRecipient("+27834557896");
        assertEquals("Did you get the cake?\n", result);
    }

    @Test
    public void testDeleteMessageUsingMessageHash() {
        String result = manager.deleteStoredMessageByHash(
                message2.getMessageHash());

        assertEquals("Message: \"Where are you? You are late! I have asked you to be on time.\" successfully deleted.",
                result);
        assertFalse(manager.searchMessagesForRecipient("+27838884567")
                .contains("Where are you?"));
        assertTrue(manager.searchMessagesForRecipient("+27838884567")
                .contains("Ok, I am leaving without you."));
    }
@Test
public void testDisplayReportListsAllSentMessageDetails() {
    String report = manager.displaySentMessageReport();

    String expectedReport = "SENT MESSAGES REPORT\n"
            + "\nMessage Hash: " + message1.getMessageHash()
            + "\nRecipient: +27834557896"
            + "\nMessage: Did you get the cake?"
            + "\n----------------------------------------\n"
            + "\nMessage Hash: " + message4.getMessageHash()
            + "\nRecipient: Not specified"
            + "\nMessage: It is dinner time !"
            + "\n----------------------------------------\n";

    assertEquals(expectedReport, report);
}

    @Test
    public void testStoredMessageReportListsAllStoredMessageDetails() {
        String report = manager.displayStoredMessageReport();

        assertTrue(report.contains(message2.getMessageID()));
        assertTrue(report.contains(message2.getMessageHash()));
        assertTrue(report.contains("0838884567"));
        assertTrue(report.contains("+27838884567"));
        assertTrue(report.contains(
                "Where are you? You are late! I have asked you to be on time."));
        assertTrue(report.contains("Ok, I am leaving without you."));
        assertFalse(report.contains("Did you get the cake?"));
    }
}