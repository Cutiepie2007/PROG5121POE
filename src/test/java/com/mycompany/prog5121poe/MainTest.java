package com.mycompany.prog5121poe;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Scanner;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class MainTest {

    private Message message1;
    private Message message2;
    private Message message3;
    private Message message4;
    private Message message5;

    private PrintStream originalOut;
    private ByteArrayOutputStream output;

    private byte[] originalJson;
    private boolean jsonFileExists;

    @BeforeEach
    public void setUp() throws Exception {

        originalOut = System.out;
        output = new ByteArrayOutputStream();
        System.setOut(new PrintStream(output));

        Path jsonPath = Paths.get("stored_messages.json");

        jsonFileExists = Files.exists(jsonPath);

        if (jsonFileExists) {
            originalJson = Files.readAllBytes(jsonPath);
        }

        // Test Data Message 1
        message1 = new Message(
                1,
                "0834557896",
                "+27834557896",
                "Did you get the cake?",
                "AA:1:DIDCAKE",
                "Sent"
        );

        // Test Data Message 2
        message2 = new Message(
                2,
                "0838884567",
                "+27838884567",
                "Where are you? You are late! I have asked you to be on time.",
                "AA:2:WHERE",
                "Stored"
        );

        // Test Data Message 3
        message3 = new Message(
                3,
                "0833444567",
                "+27834484567",
                "Yohoooo, I am at your gate.",
                "AA:3:YOHO",
                "Disregarded"
        );

        // Test Data Message 4
        message4 = new Message(
                4,
                "0838884567",
                "0838884567",
                "It is dinner time!",
                "AA:4:DINNER",
                "Sent"
        );

        // Test Data Message 5
        message5 = new Message(
                5,
                "0838884568",
                "+27838884567",
                "Ok, I am leaving without you.",
                "AA:5:LEAVING",
                "Stored"
        );

        // Reset Main arrays
        Main.sentMessages = new Message[100];
        Main.disregardedMessages = new Message[100];
        Main.storedMessages = new Message[100];

        Main.messageHashes = new String[100];
        Main.messageIDs = new String[100];

        Main.sentCount = 0;
        Main.disregardedCount = 0;
        Main.storedCount = 0;
        Main.hashCount = 0;
        Main.idCount = 0;

        // Populate Sent Messages
        Main.sentMessages[0] = message1;
        Main.sentMessages[1] = message4;
        Main.sentCount = 2;

        // Populate Disregarded Messages
        Main.disregardedMessages[0] = message3;
        Main.disregardedCount = 1;

        // Populate Stored Messages
        Main.storedMessages[0] = message2;
        Main.storedMessages[1] = message5;
        Main.storedCount = 2;
    }

    @AfterEach
    public void tearDown() throws Exception {

        System.setOut(originalOut);

        Path jsonPath = Paths.get("stored_messages.json");

        if (jsonFileExists) {
            Files.write(jsonPath, originalJson);
        } else {
            Files.deleteIfExists(jsonPath);
        }
    }

    // Test 1: Sent Messages array correctly populated
    @Test
    public void testSentMessagesArray() {

        assertEquals(2, Main.sentCount);

        assertEquals(
                "Did you get the cake?",
                Main.sentMessages[0].getMessageText()
        );

        assertEquals(
                "It is dinner time!",
                Main.sentMessages[1].getMessageText()
        );
    }

    // Test 2: Display the longest stored message
    @Test
    public void testLongestStoredMessage() {

        String expected =
                "Where are you? You are late! I have asked you to be on time.";

        assertEquals(
                expected,
                Main.getLongestStoredMessage()
        );
    }

    // Test 3: Search for message ID
    @Test
    public void testSearchMessageID() {

        Main.storedMessages[0] = message4;
        Main.storedCount = 1;

        try (Scanner scanner = new Scanner("0838884567")) {

            Main.searchMessageID(scanner);

            String result = output.toString();

            assertTrue(result.contains("0838884567"));
            assertTrue(result.contains("It is dinner time!"));
        }
    }

    // Test 4: Search all messages for a particular recipient
    @Test
    public void testSearchMessagesByRecipient() {

        try (Scanner scanner = new Scanner("+27838884567")) {

            Main.searchMessagesByRecipient(scanner);

            String result = output.toString();

            assertTrue(
                    result.contains(
                            "Where are you? You are late! I have asked you to be on time."
                    )
            );

            assertTrue(
                    result.contains(
                            "Ok, I am leaving without you."
                    )
            );
        }
    }
}