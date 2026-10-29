package com.mycompany.prog5121poe;

import com.google.gson.JsonArray;
import com.google.gson.JsonParser;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class MessageTest {

    private Message message;
    private final Path jsonFile = Path.of("stored_messages.json");
    private String originalJson;
    private boolean fileExisted;

    @BeforeEach
    void setUp() throws IOException {
        fileExisted = Files.exists(jsonFile);

        if (fileExisted) {
            originalJson = Files.readString(
                    jsonFile, StandardCharsets.UTF_8);
        }

        Files.deleteIfExists(jsonFile);

        message = new Message(
                0,
                "0012345678",
                "+27831234567",
                "Hi Mike, can you join us for dinner tonight?",
                "",
                "Pending"
        );
    }

    @AfterEach
    void tearDown() throws IOException {
        Files.deleteIfExists(jsonFile);

        if (fileExisted) {
            Files.writeString(
                    jsonFile, originalJson, StandardCharsets.UTF_8);
        }
    }

    @Test
    void testValidMessageID() {
        assertTrue(message.checkMessageID());
    }

    @Test
    void testInvalidMessageID() {
        Message invalid = new Message(
                0, "12345", "+27831234567",
                "Hello", "", "Pending");

        assertFalse(invalid.checkMessageID());
    }

    @Test
    void testValidRecipient() {
        assertEquals(
                "Cell phone number successfully captured.",
                message.checkRecipientCell());
    }

    @Test
    void testInvalidRecipient() {
        Message invalid = new Message(
                0, "0012345678", "0831234567",
                "Hello", "", "Pending");

        assertTrue(invalid.checkRecipientCell().contains(
                "incorrectly formatted"));
    }

    @Test
    void testMessageWithinLimit() {
        assertEquals(
                "Message ready to send.",
                message.checkMessageLength());
    }

    @Test
    void testMessageExceedsLimit() {
        Message longMessage = new Message(
                0, "0012345678", "+27831234567",
                "A".repeat(251), "", "Pending");

        assertTrue(longMessage.checkMessageLength().contains(
                "exceeds 250 characters"));
    }

    @Test
    void testExactMessageHash() {
        assertEquals(
                "00:0:HITONIGHT",
                message.createMessageHash());
    }

    @Test
    void testSendMessage() {
        assertEquals(
                "Message successfully sent.",
                message.SentMessage(1));

        assertEquals("Sent", message.getStatus());
        assertEquals(1, message.returnTotalMessagess());
    }

    @Test
    void testDisregardMessage() {
        assertEquals(
                "Press 0 to delete the message.",
                message.SentMessage(2));

        assertEquals("Disregarded", message.getStatus());
        assertEquals(0, message.returnTotalMessagess());
    }

    @Test
    void testStoreMessageWritesCompleteJSON() throws IOException {
        assertEquals(
                "Message successfully stored.",
                message.SentMessage(3));

        assertTrue(Files.exists(jsonFile));

        String json = Files.readString(
                jsonFile, StandardCharsets.UTF_8);

        JsonArray messages = JsonParser.parseString(json)
                .getAsJsonArray();

        assertEquals(1, messages.size());

        var stored = messages.get(0).getAsJsonObject();

        assertEquals(message.getMessageID(),
                stored.get("messageID").getAsString());
        assertEquals(message.getMessageNumber(),
                stored.get("messageNumber").getAsInt());
        assertEquals(message.getRecipient(),
                stored.get("recipient").getAsString());
        assertEquals(message.getMessageText(),
                stored.get("messageText").getAsString());
        assertEquals(message.getMessageHash(),
                stored.get("messageHash").getAsString());
        assertEquals("Stored",
                stored.get("status").getAsString());
    }

    @Test
    void testInvalidAction() {
        assertEquals("Invalid option.", message.SentMessage(9));
        assertEquals("Pending", message.getStatus());
    }
}