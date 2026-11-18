package com.mycompany.prog5121poe;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Random;

public class Message {

    private String messageID;
    private int messageNumber;
    private String recipient;
    private String messageText;
    private String messageHash;
    private String status;

    // Constructor
    public Message(int messageNumber, String recipient,
            String messageText) {

        this.messageNumber = messageNumber;
        this.recipient = recipient;
        this.messageText = messageText;

        this.messageID = generateMessageID();
        this.status = "Pending";
        this.messageHash = createMessageHash();
    }

    // Generate a random 10-digit message ID
    private String generateMessageID() {

        Random random = new Random();

        long number = 1000000000L
                + (long) (random.nextDouble() * 9000000000L);

        return String.valueOf(number);
    }

    // Check message ID
    public boolean checkMessageID() {

        if (messageID == null || messageID.length() != 10) {
            return false;
        }

        for (int i = 0; i < messageID.length(); i++) {

            char digit = messageID.charAt(i);

            if (digit < '0' || digit > '9') {
                return false;
            }
        }

        return true;
    }

    // Check recipient cellphone number
    public String checkRecipientCell() {

        if (recipient == null
                || recipient.length() != 12
                || !recipient.startsWith("+27")) {

            return "Cell phone number is incorrectly formatted or does not contain an international code. Please correct the number and try again.";
        }

        for (int i = 3; i < recipient.length(); i++) {

            char digit = recipient.charAt(i);

            if (digit < '0' || digit > '9') {

                return "Cell phone number is incorrectly formatted or does not contain an international code. Please correct the number and try again.";
            }
        }

        return "Cell phone number successfully captured.";
    }

    // Check message length
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

    // Create message hash
    public String createMessageHash() {

        if (messageID == null) {
            return "";
        }

        if (messageText == null || messageText.trim().isEmpty()) {

            messageHash = messageID.substring(0, 2)
                    + ":" + messageNumber + ":";

            return messageHash;
        }

        String[] words = messageText.trim().split("\\s+");

        String firstWord = cleanWord(words[0]);
        String lastWord = cleanWord(words[words.length - 1]);

        messageHash = (messageID.substring(0, 2) + ":"
                + messageNumber + ":"
                + firstWord + lastWord).toUpperCase();

        return messageHash;
    }

    // Remove special characters from words
    private String cleanWord(String word) {

        String cleanedWord = "";

        for (int i = 0; i < word.length(); i++) {

            char character = word.charAt(i);

            if ((character >= 'A' && character <= 'Z')
                    || (character >= 'a' && character <= 'z')
                    || (character >= '0' && character <= '9')) {

                cleanedWord = cleanedWord + character;
            }
        }

        return cleanedWord;
    }

    // Send, disregard or store message
    public String SentMessage(int choice) {

        switch (choice) {

            case 1:
                status = "Sent";
                return "Message successfully sent.";

            case 2:
                status = "Disregarded";
                return "Press 0 to delete the message.";

            case 3:
                status = "Stored";

                if (storeMessage()) {
                    return "Message successfully stored.";
                }

                return "Error storing message.";

            default:
                return "Invalid option. Message remains pending.";
        }
    }

    // Display message information
    public String printMessages() {

        return "Message ID: " + messageID
                + "\nMessage Hash: " + messageHash
                + "\nRecipient: " + recipient
                + "\nMessage: " + messageText;
    }

    // Count sent messages
    public int returnTotalMessagess() {

        if (status.equals("Sent")) {
            return 1;
        }

        return 0;
    }

    // Store messages in a JSON file
    public boolean storeMessage() {

        Gson gson = new GsonBuilder()
                .setPrettyPrinting()
                .create();

        File file = new File("stored_messages.json");

        JsonArray messages = new JsonArray();

        // Read messages already saved
        if (file.exists() && file.length() > 0) {

            try {

                FileReader reader = new FileReader(file);

                JsonArray savedMessages =
                        gson.fromJson(reader, JsonArray.class);

                reader.close();

                if (savedMessages != null) {
                    messages = savedMessages;
                }

            } catch (Exception e) {

                System.out.println("Error reading stored messages.");
                return false;
            }
        }

        // Create a JSON object for this message
        JsonObject messageObject = new JsonObject();

        messageObject.addProperty("messageID", messageID);
        messageObject.addProperty("messageNumber", messageNumber);
        messageObject.addProperty("recipient", recipient);
        messageObject.addProperty("messageText", messageText);
        messageObject.addProperty("messageHash", messageHash);
        messageObject.addProperty("status", status);

        messages.add(messageObject);

        // Save messages to the JSON file
        try {

            FileWriter writer = new FileWriter(file);

            gson.toJson(messages, writer);

            writer.close();

            return true;

        } catch (IOException e) {

            System.out.println("Error storing message: "
                    + e.getMessage());

            return false;
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