package com.mycompany.prog5121poe;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
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

        Path jsonPath = Path.of("stored_messages.json");

        jsonFileExists = Files.exists(jsonPath);

        if (jsonFileExists) {
            originalJson = Files.readAllBytes(jsonPath);
        }

        // Test Data Message 1
        message1 = new Message(
                1,
                "1000000001",
                "+27834557896",
                "Did you get the cake?",
                "10:1:DID:CAKE",
                "Sent"
        );

        // Test Data Message 2
        message2 = new Message(
                2,
                "1000000002",
                "+27838884567",
                "Where are you? You are late! I have asked you to be on time.",
                "10:2:WHERE:TIME",
                "Stored"
        );

        // Test Data Message 3
        message3 = new Message(
                3,
                "1000000003",
                "+27834484567",
                "Yohoooo, I am at your gate.",
                "10:3:YOHOOO:GATE",
                "Disregarded"
        );

        // Test Data Message 4
        message4 = new Message(
                4,
                "0838884567",
                "0838884567",
                "It is dinner time !",
                "08:4:IT:TIME",
                "Sent"
        );

        // Test Data Message 5
        message5 = new Message(
                5,
                "1000000005",
                "+27838884567",
                "Ok, I am leaving without you.",
                "10:5:OK:YOU",
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

        Path jsonPath = Path.of("stored_messages.json");

        if (jsonFileExists) {
            Files.write(jsonPath, originalJson);
        } else {
            Files.deleteIfExists(jsonPath);
        }
    }

    // Test 1: Sent Messages array
    @Test
    public void testSentMessagesArray() {

        assertEquals(2, Main.sentCount);

        assertEquals(
                "Did you get the cake?",
                Main.sentMessages[0].getMessageText()
        );

        assertEquals(
                "It is dinner time !",
                Main.sentMessages[1].getMessageText()
        );
    }

    // Test 2: Longest stored message
    @Test
    public void testLongestStoredMessage() {

        String expected =
                "Where are you? You are late! I have asked you to be on time.";

        assertEquals(
                expected,
                Main.getLongestStoredMessage()
        );
    }

    // Test 3: Search for Message 4 by Message ID
    @Test
    public void testSearchMessageID() {

        try (Scanner scanner = new Scanner("0838884567")) {

            Main.searchMessageID(scanner);

            String result = output.toString();

            assertTrue(result.contains("0838884567"));
            assertTrue(result.contains("It is dinner time !"));
        }
    }

    // Test 4: Search all messages for a recipient
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

    // Test 5: Delete Message 2 using its Message Hash
    @Test
    public void testDeleteMessageByHash() throws Exception {

        Gson gson = new Gson();
        JsonArray jsonArray = new JsonArray();

        jsonArray.add(gson.toJsonTree(message2));
        jsonArray.add(gson.toJsonTree(message5));

        Files.writeString(
                Path.of("stored_messages.json"),
                gson.toJson(jsonArray),
                StandardCharsets.UTF_8
        );

        try (Scanner scanner = new Scanner("10:2:WHERE:TIME")) {

            Main.deleteMessageByHash(scanner);

            String result = output.toString();

            assertTrue(
                    result.contains("successfully deleted")
            );
        }

        // Reload the stored messages after deletion
        Main.loadStoredMessagesFromJson();

        // Only Message 5 should remain
        assertEquals(1, Main.storedCount);

        assertEquals(
                "Ok, I am leaving without you.",
                Main.storedMessages[0].getMessageText()
        );

        // Check the updated JSON file
        String jsonContent = Files.readString(
                Path.of("stored_messages.json")
        );

        // Message 5 must still be present
        assertTrue(
                jsonContent.contains(
                        "Ok, I am leaving without you."
                )
        );

        // Message 2 must have been deleted
        assertTrue(
                !jsonContent.contains(
                        "Where are you? You are late! I have asked you to be on time."
                )
        );
    }

    // Test 6: Display full report
    @Test
    public void testDisplayReport() {

        Main.displayReport();

        String result = output.toString();

        assertTrue(result.contains("Report"));

       // Message Hashes
        assertTrue(result.contains("10:1:DID:CAKE"));
        assertTrue(result.contains("08:4:IT:TIME"));

       // Recipients
        assertTrue(result.contains("+27834557896"));
        assertTrue(result.contains("0838884567"));

        // Messages
        assertTrue(
                result.contains("Did you get the cake?")
        );

        assertTrue(
                result.contains("It is dinner time !")
        );
    }
}