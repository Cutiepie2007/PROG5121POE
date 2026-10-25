package com.mycompany.prog5121poe;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Locale;
import java.util.Random;

public final class Message {

    private final String messageID;
    private final int messageNumber;
    private final String recipient;
    private final String messageText;
    private final String messageHash;
    private String status;

    // Constructor for creating a new message
    public Message(int messageNumber, String recipient, String messageText) {
        this.messageNumber = messageNumber;
        this.recipient = recipient;
        this.messageText = messageText;
        this.messageID = generateMessageID();
        this.messageHash = createMessageHash();
        this.status = "Pending";
    }

    // Constructor for loading an existing message from JSON
    public Message(
            int messageNumber,
            String messageID,
            String recipient,
            String messageText,
            String messageHash,
            String status) {

        this.messageNumber = messageNumber;
        this.messageID = messageID;
        this.recipient = recipient;
        this.messageText = messageText;
        this.messageHash = messageHash;
        this.status = status;
    }

    // Generate a random ten-digit message ID
    private String generateMessageID() {
        Random random = new Random();

        long number = 1_000_000_000L
                + (long) (random.nextDouble() * 9_000_000_000L);

        return String.valueOf(number);
    }

    // Check that the message ID contains ten digits
    public boolean checkMessageID() {
        return messageID != null && messageID.matches("\\d{10}");
    }

    // Validate the recipient's South African cellphone number
    public String checkRecipientCell() {
        if (recipient != null && recipient.matches("^\\+27\\d{9}$")) {
            return "Cell phone number successfully captured.";
        }

        return "Cell phone number is incorrectly formatted or does not contain an international code. Please correct the number and try again.";
    }

    // Check that the message is not empty and does not exceed 250 characters
    public String checkMessageLength() {
        if (messageText == null || messageText.trim().isEmpty()) {
            return "Please enter a message.";
        }

        if (messageText.length() <= 250) {
            return "Message ready to send.";
        }

        return "Message exceeds 250 characters by "
                + (messageText.length() - 250)
                + "; please reduce the size.";
    }

    // Create the message hash
    public String createMessageHash() {
        if (messageText == null || messageText.trim().isEmpty()) {
            return messageID.substring(0, 2)
                    + ":" + messageNumber + ":";
        }

        String[] words = messageText.trim().split("\\s+");

        String firstWord = cleanWord(words[0]);
        String lastWord = cleanWord(words[words.length - 1]);

        String firstTwoIDDigits = messageID.substring(0, 2);

        return (firstTwoIDDigits + ":" + messageNumber + ":"
                + firstWord + lastWord).toUpperCase(Locale.ROOT);
    }

    // Remove punctuation from the first and last words
    private String cleanWord(String word) {
        return word.replaceAll("[^A-Za-z0-9]", "");
    }

    // Process the user's choice for a message
    public String SentMessage(int choice) {
        switch (choice) {
            case 1 -> {
                status = "Sent";
                return "Message successfully sent.";
            }

            case 2 -> {
                status = "Disregarded";
                return "Press 0 to delete the message.";
            }

            case 3 -> {
                status = "Stored";
                storeMessage();
                return "Message successfully stored.";
            }

            default -> {
                return "Invalid option. Message remains pending.";
            }
        }
    }

    // Display the message details
    public String printMessages() {
        return "Message ID: " + messageID
                + "\nMessage Hash: " + messageHash
                + "\nRecipient: " + recipient
                + "\nMessage: " + messageText;
    }

    // Return 1 if the message was sent, otherwise return 0
    public int returnTotalMessagess() {
        return "Sent".equals(status) ? 1 : 0;
    }

    // Store the message in a JSON file
    public void storeMessage() {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();

        Path filePath = Path.of("stored_messages.json");
        JsonArray messages = new JsonArray();

        try {
            if (Files.exists(filePath) && Files.size(filePath) > 0) {
                String existingJson = Files.readString(
                        filePath, StandardCharsets.UTF_8
                ).trim();

                if (!existingJson.isEmpty()) {
                    JsonElement jsonElement =
                            JsonParser.parseString(existingJson);

                    if (!jsonElement.isJsonArray()) {
                        System.out.println(
                                "Error: JSON file must contain an array."
                        );
                        return;
                    }

                    messages = jsonElement.getAsJsonArray();
                }
            }

            JsonObject messageObject = new JsonObject();

            messageObject.addProperty("messageID", messageID);
            messageObject.addProperty("messageNumber", messageNumber);
            messageObject.addProperty("recipient", recipient);
            messageObject.addProperty("messageText", messageText);
            messageObject.addProperty("messageHash", messageHash);
            messageObject.addProperty("status", status);

            messages.add(messageObject);

            Files.writeString(
                    filePath,
                    gson.toJson(messages),
                    StandardCharsets.UTF_8
            );

            System.out.println("Message successfully stored.");

        } catch (IOException
                | IllegalStateException
                | com.google.gson.JsonParseException e) {

            System.out.println(
                    "Error storing message: " + e.getMessage()
            );
        }
    }

    // Getters
    public String getMessageID() {
        return messageID;
    }

    public int getMessageNumber() {
        return messageNumber;
    }

    public String getRecipient() {
        return recipient;
    }

    public String getMessageText() {
        return messageText;
    }

    public String getMessageHash() {
        return messageHash;
    }

    public String getStatus() {
        return status;
    }
}

