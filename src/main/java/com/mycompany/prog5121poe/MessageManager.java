package com.mycompany.prog5121poe;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonIOException;
import com.google.gson.JsonSyntaxException;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

/** 
* Holds various arrays of messages needed for part 3, and allows searching through these.
* These will all be filled with user input as well as information from the supplied JSON.
*/
public final class MessageManager {

    private File storageFile;
    private Gson gson;

    private Message[] sentMessages;
    private Message[] disregardedMessages;
    private Message[] storedMessages;
    private Message[] allMessages;
    private String[] messageHashes;
    private String[] messageIDs;

    public MessageManager() {
        this(new File("stored_messages.json"));
    }

    public MessageManager(File storageFile) {
        this.storageFile = storageFile;
        gson = new GsonBuilder().setPrettyPrinting().create();
        sentMessages = new Message[0];
        disregardedMessages = new Message[0];
        storedMessages = new Message[0];
        allMessages = new Message[0];
        messageHashes = new String[0];
        messageIDs = new String[0];
        loadStoredMessages();
    }

    public void addMessage(Message message) {
        if (message == null) {
            return;
        }

        allMessages = addMessageToArray(allMessages, message);
        messageHashes = addStringToArray(messageHashes, message.getMessageHash());
        messageIDs = addStringToArray(messageIDs, message.getMessageID());

        switch (message.getStatus()) {
            case "Sent" -> sentMessages = addMessageToArray(sentMessages, message);
            case "Disregarded" -> disregardedMessages = addMessageToArray(disregardedMessages, message);
            case "Stored" -> {
                storedMessages = addMessageToArray(storedMessages, message);
                saveStoredMessages();
            }
            default -> {
            }
        }
    }

    private Message[] addMessageToArray(Message[] oldArray, Message message) {
        Message[] newArray = new Message[oldArray.length + 1];
        System.arraycopy(oldArray, 0, newArray, 0, oldArray.length);
        newArray[oldArray.length] = message;
        return newArray;
    }

    private String[] addStringToArray(String[] oldArray, String value) {
        String[] newArray = new String[oldArray.length + 1];
        System.arraycopy(oldArray, 0, newArray, 0, oldArray.length);
        newArray[oldArray.length] = value;
        return newArray;
    }

    public void loadStoredMessages() {
        storedMessages = new Message[0];

        if (!storageFile.exists() || storageFile.length() == 0) {
            return;
        }

        try {
            Message[] loadedMessages;
            try (FileReader reader = new FileReader(storageFile)) {
                loadedMessages = gson.fromJson(reader, Message[].class);
            }

            if (loadedMessages != null) {
                for (Message message : loadedMessages) {
                    if (message != null && "Stored".equals(message.getStatus())) {
                        storedMessages = addMessageToArray(storedMessages, message);
                        if (!containsMessageID(message.getMessageID())) {
                            allMessages = addMessageToArray(allMessages, message);
                            messageHashes = addStringToArray(messageHashes, message.getMessageHash());
                            messageIDs = addStringToArray(messageIDs, message.getMessageID());
                        }
                    }
                }
            }
        } catch (JsonIOException | JsonSyntaxException | IOException e) {
            System.out.println("Could not read stored_messages.json: " + e.getMessage());
        }
    }

    private boolean containsMessageID(String messageID) {
        for (String messageID1 : messageIDs) {
            if (messageID1.equals(messageID)) {
                return true;
            }
        }
        return false;
    }

    public Message[] getSentMessages() {
        return sentMessages;
    }

    public Message[] getDisregardedMessages() {
        return disregardedMessages;
    }

    public Message[] getStoredMessages() {
        loadStoredMessages();
        return storedMessages;
    }

    public String[] getMessageHashes() {
        return messageHashes;
    }

    public String[] getMessageIDs() {
        return messageIDs;
    }

    public String displayStoredSendersAndRecipients() {
        Message[] messages = getStoredMessages();
        if (messages.length == 0) {
            return "There are no stored messages.";
        }

        String output = "";
        for (Message message : messages) {
            output = output + "Sender: " + message.getSender() + "\nRecipient: " + message.getRecipient() + "\n------------------------------\n";
        }
        return output;
    }

    public String getLongestStoredMessage() {
        Message[] messages = getStoredMessages();
        if (messages.length == 0) {
            return "There are no stored messages.";
        }

        int longestIndex = 0;
        for (int i = 1; i < messages.length; i++) {
            if (messages[i].getMessageText().length()
                    > messages[longestIndex].getMessageText().length()) {
                longestIndex = i;
            }
        }
        return messages[longestIndex].getMessageText();
    }

    public String searchMessageByID(String messageID) {
        for (Message allMessage : allMessages) {
            if (allMessage.getMessageID().equals(messageID)) {
                return "Recipient: " + allMessage.getRecipient() + "\nMessage: " + allMessage.getMessageText();
            }
        }
        return "Message ID not found.";
    }

    public String searchMessagesForRecipient(String recipient) {
        String output = "";

        // Search sent messages as well as stored messages
        
        for (Message sentMessage : sentMessages) {
            if (sentMessage.getRecipient().equals(recipient)) {
                output = output + sentMessage.getMessageText() + "\n";
            }
        }

        Message[] messages = getStoredMessages();
        for (Message message : messages) {
            if (message.getRecipient().equals(recipient)) {
                output = output + message.getMessageText() + "\n";
            }
        }

        if (output.equals("")) {
            return "No sent or stored messages found for this recipient.";
        }
        return output;
    }

    public String deleteStoredMessageByHash(String messageHash) {
        Message[] messages = getStoredMessages();
        int deleteIndex = -1;

        for (int i = 0; i < messages.length; i++) {
            if (messages[i].getMessageHash().equalsIgnoreCase(messageHash)) {
                deleteIndex = i;
            }
        }

        if (deleteIndex == -1) {
            return "Message hash not found.";
        }

        Message deletedMessage = messages[deleteIndex];
        Message[] newStoredMessages = new Message[messages.length - 1];
        int newIndex = 0;
        for (int i = 0; i < messages.length; i++) {
            if (i != deleteIndex) {
                newStoredMessages[newIndex] = messages[i];
                newIndex++;
            }
        }

        storedMessages = newStoredMessages;
        saveStoredMessages();
        removeFromAllMessages(deletedMessage);

        return "Message: \"" + deletedMessage.getMessageText()
                + "\" successfully deleted.";
    }

    private void removeFromAllMessages(Message deletedMessage) {
        Message[] newAllMessages = new Message[allMessages.length - 1];
        int newIndex = 0;
        for (Message allMessage : allMessages) {
            if (!allMessage.getMessageID().equals(deletedMessage.getMessageID())) {
                newAllMessages[newIndex] = allMessage;
                newIndex++;
            }
        }
        allMessages = newAllMessages;

        String[] newHashes = new String[messageHashes.length - 1];
        String[] newIDs = new String[messageIDs.length - 1];
        newIndex = 0;
        for (int i = 0; i < messageIDs.length; i++) {
            if (!messageIDs[i].equals(deletedMessage.getMessageID())) {
                newIDs[newIndex] = messageIDs[i];
                newHashes[newIndex] = messageHashes[i];
                newIndex++;
            }
        }
        messageIDs = newIDs;
        messageHashes = newHashes;
    }

    public String displayStoredMessageReport() {
        Message[] messages = getStoredMessages();
        if (messages.length == 0) {
            return "There are no stored messages.";
        }

        String report = "STORED MESSAGES REPORT\n";
        for (Message message : messages) {
            report = report + "\nMessage ID: " + message.getMessageID() + "\nMessage Hash: " + message.getMessageHash() + "\nSender: " + message.getSender() + "\nRecipient: " + message.getRecipient() + "\nMessage: " + message.getMessageText() + "\nStatus: " + message.getStatus() + "\n----------------------------------------\n";
        }
        return report;
    }

    public String displaySentMessageReport() {
        if (sentMessages.length == 0) {
            return "There are no sent messages.";
        }

        String report = "SENT MESSAGES REPORT\n";
        for (Message sentMessage : sentMessages) {
            report = report + "\nMessage Hash: " + sentMessage.getMessageHash() + "\nRecipient: " + sentMessage.getRecipient() + "\nMessage: " + sentMessage.getMessageText() + "\n----------------------------------------\n";
        }
        return report;
    }

    private void saveStoredMessages() {
        try {
            try (FileWriter writer = new FileWriter(storageFile)) {
                gson.toJson(storedMessages, writer);
            }
        } catch (IOException e) {
            System.out.println("Could not update stored_messages.json: " + e.getMessage());
        }
    }
}