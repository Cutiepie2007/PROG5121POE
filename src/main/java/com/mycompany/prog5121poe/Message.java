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

    private String messageID;
    private int messageNumber;
    private String recipient;
    private String messageText;
    private String messageHash;
    private String status;

    // Required for loading messages from JSON with Gson
    public Message() {
    }

    public Message(int messageNumber, String recipient, String messageText) {
        this.messageNumber = messageNumber;
        this.recipient = recipient;
        this.messageText = messageText;
        this.messageID = generateMessageID();
        this.messageHash = createMessageHash();
        this.status = "Pending";
    }

    // Constructor for test data and previously stored messages
    public Message(int messageNumber, String messageID, String recipient,
                   String messageText, String messageHash, String status) {
        this.messageNumber = messageNumber;
        this.messageID = messageID;
        this.recipient = recipient;
        this.messageText = messageText;
        this.messageHash = createMessageHash();
        this.status = status;
    }

    private String generateMessageID() {
        Random random = new Random();
        long number = 1_000_000_000L
                + (long) (random.nextDouble() * 9_000_000_000L);

        return String.valueOf(number);
    }

    public boolean checkMessageID() {
        return messageID != null && messageID.matches("\\d{10}");
    }

    public String checkRecipientCell() {
        if (recipient != null && recipient.matches("^\\+27\\d{9}$")) {
            return "Cell phone number successfully captured.";
        }

        return "Cell phone number is incorrectly formatted or does not contain an international code. Please correct the number and try again.";
    }

    public String checkMessageLength() {
        if (messageText == null || messageText.length() > 250) {
            int length = messageText == null ? 0 : messageText.length();

            return "Message exceeds 250 characters by "
                    + Math.max(0, length - 250)
                    + "; please reduce the size.";
        }

        if (messageText.trim().isEmpty()) {
            return "Please enter a message.";
        }

        return "Message ready to send.";
    }

    public String createMessageHash() {
        if (messageID == null || messageID.length() < 2) {
            return "";
        }

        if (messageText == null || messageText.trim().isEmpty()) {
            return messageID.substring(0, 2) + ":" + messageNumber + ":";
        }

        String[] words = messageText.trim().split("\\s+");

        String firstWord = cleanWord(words[0]);
        String lastWord = cleanWord(words[words.length - 1]);

        return (messageID.substring(0, 2) + ":"
                + messageNumber + ":"
                + firstWord + lastWord).toUpperCase(Locale.ROOT);
    }

    private String cleanWord(String word) {
        return word.replaceAll("[^A-Za-z0-9]", "");
    }

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
                return "Invalid option.";
            }
        }
    }

    public String printMessages() {
        return "Message ID: " + messageID
                + "\nMessage Hash: " + messageHash
                + "\nRecipient: " + recipient
                + "\nMessage: " + messageText;
    }

    public int returnTotalMessagess() {
        if ("Sent".equals(status)) {
            return 1;
        }

        return 0;
    }

    public void storeMessage() {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        Path filePath = Path.of("stored_messages.json");
        JsonArray messages = new JsonArray();

        try {
            if (Files.exists(filePath) && Files.size(filePath) > 0) {
                String json = Files.readString(
                        filePath, StandardCharsets.UTF_8);

                if (!json.trim().isEmpty()) {
                    JsonElement element = JsonParser.parseString(json);

                    if (!element.isJsonArray()) {
                        System.out.println(
                                "Stored messages file has an invalid format.");
                        return;
                    }

                    messages = element.getAsJsonArray();
                }
            }

            JsonObject object = new JsonObject();
            object.addProperty("messageID", messageID);
            object.addProperty("messageNumber", messageNumber);
            object.addProperty("recipient", recipient);
            object.addProperty("messageText", messageText);
            object.addProperty("messageHash", messageHash);
            object.addProperty("status", status);

            messages.add(object);

            Files.writeString(
                    filePath,
                    gson.toJson(messages),
                    StandardCharsets.UTF_8);

        } catch (IOException | IllegalStateException
                 | com.google.gson.JsonParseException e) {
            System.out.println("Error storing message: " + e.getMessage());
        }
    }

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