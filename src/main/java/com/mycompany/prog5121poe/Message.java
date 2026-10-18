package com.mycompany.prog5121poe;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonElement;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Locale;
import java.util.Random;
/** 
* Will handle quick chat messages and allow for checks to be made
* on them as well as hashing and JSON.
*/ 
public final class Message {

    private final String messageID;
    private final int messageNumber;
    private final String recipient;
    private final String messageText;
    private final String messageHash;

    private String status;

    public Message(
            int messageNumber,
            String recipient,
            String messageText) {

        this.messageNumber = messageNumber;
        this.recipient = recipient;
        this.messageText = messageText;

        this.messageID = generateMessageID();
        this.messageHash = createMessageHash();

        this.status = "Pending";
    }

    // Creates a random message id of ten digits long.
    private String generateMessageID() {
        Random random = new Random();
        long number = 1_000_000_000L
                + (long) (random.nextDouble() * 9_000_000_000L);

        return String.valueOf(number);
    }

    // Tests to make sure there are ten digits in the id.
    public boolean checkMessageID() {
        return messageID != null
                && messageID.matches("\\d{10}");
    }

    // Tests if the number the user wants to send the message to is valid using java regex (Oracle, n.d.). 
    public String checkRecipientCell() {

        if (recipient != null
                && recipient.matches("^\\+27\\d{9}$")) {
            return "Cell phone number successfully captured.";
        }
        return "Cell phone number is incorrectly formatted or does not contain an international code. Please correct the number and try again.";
    }

    // Ensures the message is not blank and that it is under 250 characters.
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

    // Hashes the message with the message id number and first and last word. 
    public String createMessageHash() {
        if(messageText == null || messageText.trim().isEmpty()) {
            return messageID.substring(0, 2)
                    + ":" + messageNumber + ":";
        }
        String[] words = messageText.trim().split("\\s+");

        String firstWord = cleanWord(words[0]);
        String lastWord = cleanWord(words[words.length - 1]);

        String firstTwoIDDigits = messageID.substring(0, 2);
        return (firstTwoIDDigits
                + ":" + messageNumber
                + ":" + firstWord
                + lastWord).toUpperCase(Locale.ROOT);
    }

    // Removes special characters from a word. 
    private String cleanWord(String word) {
        return word.replaceAll("[^A-Za-z0-9]", "");
    }

    // Allows the user to decide if they want to send the message or ignore it or save it. 
    // Changes status of the message depending on users input.
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

    // Will return the information about the message. 
    public String printMessages() {
        return "Message ID: " + messageID
                + "\nMessage Hash: " + messageHash
                + "\nRecipient: " + recipient
                + "\nMessage: " + messageText;
    }

    // Returns 1 if message was sent else it will return 0.
    public int returnTotalMessagess() {
        return "Sent".equals(status) ? 1 : 0;
    }
    
    // Will take the json that already exists and append the message to it and write the new message array to the json.
public void storeMessage() {
    Gson gson = new GsonBuilder()
            .setPrettyPrinting()
            .create();

    Path filePath = Path.of("stored_messages.json");
    JsonArray messages = new JsonArray();

    try {
        // Read existing messages so they are not lost.
        if (Files.exists(filePath)
                && Files.size(filePath) > 0) {

            String existingJson = Files.readString(
                    filePath,
                    StandardCharsets.UTF_8
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

        // Create a JSON object for the current message.
        JsonObject messageObject = new JsonObject();
        messageObject.addProperty("messageID", messageID);
        messageObject.addProperty("messageNumber", messageNumber);
        messageObject.addProperty("recipient", recipient);
        messageObject.addProperty("messageText", messageText);
        messageObject.addProperty("messageHash", messageHash);
        messageObject.addProperty("status", status);

        // Add the current message to the array.
        messages.add(messageObject);

        // Save all messages to the JSON file.
        Files.writeString(
                filePath,
                gson.toJson(messages),
                StandardCharsets.UTF_8
        );

        System.out.println("Message successfully stored.");

    } catch (IOException | IllegalStateException
            | com.google.gson.JsonParseException e) {
        System.out.println(
                "Error storing message: " + e.getMessage()
        );
    }
}

    // Getters for each variable in the class. 
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

