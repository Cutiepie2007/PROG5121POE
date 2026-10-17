package com.mycompany.prog5121poe;

import java.io.FileWriter;
import java.io.IOException;
import java.util.Locale;
import java.util.Random;

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

    private String generateMessageID() {
        Random random = new Random();
        long number = 1_000_000_000L
                + (long) (random.nextDouble() * 9_000_000_000L);

        return String.valueOf(number);
    }

    public boolean checkMessageID() {
        return messageID != null
                && messageID.matches("\\d{10}");
    }

    public String checkRecipientCell() {

        if (recipient != null
                && recipient.matches("^\\+27\\d{9}$")) {
            return "Cell phone number successfully captured.";
        }
        return "Cell phone number is incorrectly formatted or does not contain an international code. Please correct the number and try again.";
    }

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
                return "Invalid option. Message remains pending.";
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
        return "Sent".equals(status) ? 1 : 0;
    }

  public void storeMessage() {
    String json = """
            {
              "messageID": "%s",
              "messageNumber": %d,
              "messageHash": "%s",
              "recipient": "%s",
              "message": "%s",
              "status": "%s"
            },
            """.formatted(
            escapeJson(messageID),
            messageNumber,
            escapeJson(messageHash),
            escapeJson(recipient),
            escapeJson(messageText),
            escapeJson(status)
    );

    try (FileWriter writer =
                 new FileWriter("stored_messages.json", true)) {

        writer.write(json);

    } catch (IOException e) {

        System.out.println(
                "Could not store the message: " + e.getMessage()
        );
    }
}

    private String escapeJson(String value) {
        return value.replace("\\", "\\\\")
                .replace("\"", "\\\"")
                .replace("\n", "\\n")
                .replace("\r", "\\r")
                .replace("\t", "\\t");
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