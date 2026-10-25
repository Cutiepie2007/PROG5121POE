/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

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
import java.util.ArrayList;
import java.util.Scanner;

public class Main {

    // Stores recently sent messages from Part 2
    static ArrayList<String> recentSentMessages = new ArrayList<>();

    // Part 3 arrays
    static Message[] sentMessages = new Message[100];
    static Message[] disregardedMessages = new Message[100];
    static Message[] storedMessages = new Message[100];
    static String[] messageHashes = new String[100];
    static String[] messageIDs = new String[100];

    // Counters for the Part 3 arrays
    static int sentCount = 0;
    static int disregardedCount = 0;
    static int storedCount = 0;
    static int hashCount = 0;
    static int idCount = 0;

    public static void main(String[] args) {

        try (Scanner scanner = new Scanner(System.in)) {

            // ==============================
            // REGISTRATION
            // ==============================

            System.out.println("================================");
            System.out.println("QUICKCHAT REGISTRATION");
            System.out.println("================================");

            System.out.print("Enter your first name: ");
            String firstName = scanner.nextLine();

            System.out.print("Enter your last name: ");
            String lastName = scanner.nextLine();

            System.out.print("Enter your username: ");
            String username = scanner.nextLine();

            System.out.print("Enter your password: ");
            String password = scanner.nextLine();

            System.out.print("Enter your cell phone number: ");
            String cellPhoneNumber = scanner.nextLine();

            Login login = new Login(
                    firstName,
                    lastName,
                    username,
                    password,
                    cellPhoneNumber
            );

            System.out.println(login.registerUser());

            // ==============================
            // REGISTRATION RESULTS
            // ==============================

            System.out.println("================================");
            System.out.println("REGISTRATION RESULTS");
            System.out.println("================================");

            if (login.checkUserName()) {
                System.out.println("Username successfully captured");
            } else {
                System.out.println("Username is incorrectly formatted");
            }

            if (login.checkPasswordComplexity()) {
                System.out.println("Password successfully captured.");
            } else {
                System.out.println("Password is incorrectly formatted");
            }

            if (login.checkCellPhoneNumber()) {
                System.out.println("Cell phone number successfully captured");
            } else {
                System.out.println("Cell phone number is incorrectly formatted");
            }

            if (login.checkUserName()
                    && login.checkPasswordComplexity()
                    && login.checkCellPhoneNumber()) {

                System.out.println("Registration successful.");

            } else {

                System.out.println(
                        "Registration failed. Please correct your details."
                );
                return;
            }

            // ==============================
            // LOGIN
            // ==============================

            System.out.println("================================");
            System.out.println("LOGIN");
            System.out.println("================================");

            System.out.print("Enter username: ");
            String loginUsername = scanner.nextLine();

            System.out.print("Enter password: ");
            String loginPassword = scanner.nextLine();

            if (!login.loginUser(loginUsername, loginPassword)) {

                System.out.println(
                        "Username or password incorrect, please try again."
                );
                return;
            }

            System.out.println(
                    "Welcome " + firstName + ", " + lastName
                    + " it is great to see you again."
            );

            System.out.println("Welcome to QuickChat.");

            // ==============================
            // NUMBER OF MESSAGES
            // ==============================

            System.out.print(
                    "How many messages would you like to enter? "
            );

            int numberOfMessages;

            try {

                numberOfMessages =
                        Integer.parseInt(scanner.nextLine());

                if (numberOfMessages < 0) {

                    System.out.println(
                            "Please enter zero or a positive number."
                    );
                    return;
                }

            } catch (NumberFormatException e) {

                System.out.println(
                        "Please enter a valid whole number."
                );
                return;
            }

            int messageNumber = 0;
            int totalSent = 0;
            boolean quit = false;

            // ==============================
            // QUICKCHAT MENU
            // ==============================

            while (!quit) {

                System.out.println("\n================================");
                System.out.println("QUICKCHAT MENU");
                System.out.println("================================");
                System.out.println("1) Send Messages");
                System.out.println("2) Show recently sent messages");
                System.out.println("3) Quit");
                System.out.println("4) Stored Messages");
                System.out.print("Choose an option: ");

                String option = scanner.nextLine();

                switch (option) {

                    // ==============================
                    // OPTION 1 - SEND MESSAGES
                    // ==============================

                    case "1" -> {

                        if (messageNumber >= numberOfMessages) {

                            System.out.println(
                                    "You have reached the number of messages you selected."
                            );
                            break;
                        }

                        for (; messageNumber < numberOfMessages;
                                messageNumber++) {

                            System.out.println(
                                    "\nMessage " + (messageNumber + 1)
                            );

                            System.out.print(
                                    "Enter recipient number (+27 followed by 9 digits): "
                            );

                            String recipient = scanner.nextLine();

                            System.out.print(
                                    "Enter message (maximum 250 characters): "
                            );

                            String messageText = scanner.nextLine();

                            Message message = new Message(
                                    messageNumber,
                                    recipient,
                                    messageText
                            );

                            // Check message ID
                            if (!message.checkMessageID()) {

                                System.out.println(
                                        "Message ID is incorrectly formatted."
                                );
                                continue;
                            }

                            // Check recipient
                            String recipientResult =
                                    message.checkRecipientCell();

                            System.out.println(recipientResult);

                            if (!recipientResult.startsWith(
                                    "Cell phone number successfully")) {

                                messageNumber--;
                                break;
                            }

                            // Check message length
                            String lengthResult =
                                    message.checkMessageLength();

                            System.out.println(lengthResult);

                            if (!"Message ready to send.".equals(
                                    lengthResult)) {

                                messageNumber--;
                                break;
                            }

                            // Display ID and hash
                            System.out.println(
                                    "Message ID generated: "
                                    + message.getMessageID()
                            );

                            System.out.println(
                                    "Message Hash: "
                                    + message.createMessageHash()
                            );

                            // Message action
                            System.out.println(
                                    "Choose what to do with this message:"
                            );

                            System.out.println("1) Send Message");
                            System.out.println("2) Disregard Message");
                            System.out.println(
                                    "3) Store Message to send later"
                            );

                            System.out.print("Choose an option: ");

                            int action;

                            try {

                                action =
                                        Integer.parseInt(
                                                scanner.nextLine()
                                        );

                            } catch (NumberFormatException e) {

                                action = -1;
                            }

                            String actionResult = message.SentMessage(action);
                            if (action != 3) {
                                System.out.println(actionResult);
}
                            // ==============================
                            // PART 3 ARRAYS
                            // ==============================

                            // Store message ID
                            if (idCount < messageIDs.length) {

                                messageIDs[idCount] =
                                        message.getMessageID();

                                idCount++;
                            }

                            // Store message hash
                            if (hashCount < messageHashes.length) {

                                messageHashes[hashCount] =
                                        message.getMessageHash();

                                hashCount++;
                            }

                            // ==============================
                            // SENT MESSAGE
                            // ==============================

                            if (action == 1) {

                                totalSent++;

                                recentSentMessages.add(
                                        message.printMessages()
                                );

                                if (sentCount < sentMessages.length) {

                                    sentMessages[sentCount] =
                                            message;

                                    sentCount++;
                                }
                            }

                            // ==============================
                            // DISREGARDED MESSAGE
                            // ==============================

                            if (action == 2) {

                                if (disregardedCount
                                        < disregardedMessages.length) {

                                    disregardedMessages[
                                            disregardedCount
                                    ] = message;

                                    disregardedCount++;
                                }
                            }

                            // ==============================
                            // STORED MESSAGE
                            // ==============================

                            if (action == 3) {

                                loadStoredMessagesFromJson();

                                System.out.println(
                                        "Message successfully stored."
                                );
                            }

                            // Display complete message
                            System.out.println(
                                    "\nFull message details:"
                            );

                            System.out.println(
                                    message.printMessages()
                            );
                        }
                    }

                    // ==============================
                    // OPTION 2 - RECENTLY SENT
                    // ==============================

                    case "2" -> {

                        if (recentSentMessages.isEmpty()) {

                            System.out.println(
                                    "No recently sent messages."
                            );

                        } else {

                            System.out.println(
                                    "Recently Sent Messages:"
                            );

                            for (int i =
                                    recentSentMessages.size() - 1;
                                    i >= 0;
                                    i--) {

                                System.out.println(
                                        "------------------------------"
                                );

                                System.out.println(
                                        recentSentMessages.get(i)
                                );
                            }
                        }
                    }

                    // ==============================
                    // OPTION 3 - QUIT
                    // ==============================

                    case "3" -> {

                        quit = true;

                        System.out.println(
                                "Total messages sent: "
                                + totalSent
                        );

                        System.out.println(
                                "Thank you for using QuickChat."
                        );
                    }

                    // ==============================
                    // OPTION 4 - STORED MESSAGES
                    // ==============================

                    case "4" -> {

                        loadStoredMessagesFromJson();

                        storedMessagesMenu(scanner);
                    }

                    default -> System.out.println(
                            "Invalid option. Please choose 1, 2, 3 or 4."
                    );
                }
            }
        }
    }

    // ==========================================
    // LOAD STORED MESSAGES FROM JSON
    // ==========================================

    public static void loadStoredMessagesFromJson() {

        Path filePath =
                Path.of("stored_messages.json");

        storedCount = 0;

        try {

            if (!Files.exists(filePath)
                    || Files.size(filePath) == 0) {

                return;
            }

            String json =
                    Files.readString(
                            filePath,
                            StandardCharsets.UTF_8
                    );

            if (json.trim().isEmpty()) {
                return;
            }

            JsonElement jsonElement =
                    JsonParser.parseString(json);

            if (!jsonElement.isJsonArray()) {
                return;
            }

            JsonArray messages =
                    jsonElement.getAsJsonArray();

            Gson gson = new Gson();

            for (JsonElement element : messages) {

                if (storedCount >= storedMessages.length) {
                    break;
                }

                Message message =
                        gson.fromJson(
                                element,
                                Message.class
                        );

                storedMessages[storedCount] =
                        message;

                storedCount++;
            }

        } catch (IOException |
                IllegalStateException |
                com.google.gson.JsonParseException e) {

            System.out.println(
                    "Error loading stored messages: "
                    + e.getMessage()
            );
        }
    }

    // ==========================================
    // STORED MESSAGES MENU
    // ==========================================

    public static void storedMessagesMenu(
            Scanner scanner) {

        boolean back = false;

        while (!back) {

            System.out.println(
                    "\n================================"
            );

            System.out.println(
                    "STORED MESSAGES"
            );

            System.out.println(
                    "================================"
            );

            System.out.println(
                    "1) Display sender and recipient"
            );

            System.out.println(
                    "2) Display longest stored message"
            );

            System.out.println(
                    "3) Search for message ID"
            );

            System.out.println(
                    "4) Search messages by recipient"
            );

            System.out.println(
                    "5) Delete message using message hash"
            );

            System.out.println(
                    "6) Display full report"
            );

            System.out.println(
                    "7) Back to QuickChat menu"
            );

            System.out.print(
                    "Choose an option: "
            );

            String choice =
                    scanner.nextLine();

            switch (choice) {

                case "1" ->
                        displaySenderAndRecipient();

                case "2" ->
                        displayLongestStoredMessage();

                case "3" ->
                        searchMessageID(scanner);

                case "4" ->
                        searchMessagesByRecipient(scanner);

                case "5" ->
                        deleteMessageByHash(scanner);

                case "6" ->
                        displayReport();

                case "7" ->
                        back = true;

                default ->
                        System.out.println(
                                "Invalid option."
                        );
            }
        }
    }
    // ==========================================
    // DISPLAY SENDER AND RECIPIENT
    // ==========================================

    public static void displaySenderAndRecipient() {

        if (storedCount == 0) {

            System.out.println(
                    "No stored messages."
            );

            return;
        }

        System.out.println(
                "\nSender and Recipient:"
        );

        for (int i = 0;
                i < storedCount;
                i++) {

            Message message =
                    storedMessages[i];

            System.out.println(
                    "Sender: QuickChat User"
            );

            System.out.println(
                    "Recipient: "
                    + message.getRecipient()
            );

            System.out.println(
                    "------------------------------"
            );
        }
    }

    // ==========================================
    // FIND LONGEST STORED MESSAGE
    // ==========================================

    public static String getLongestStoredMessage() {

        if (storedCount == 0) {
            return "";
        }

        Message longest =
                storedMessages[0];

        for (int i = 1;
                i < storedCount;
                i++) {

            if (storedMessages[i]
                    .getMessageText()
                    .length()
                    >
                    longest
                    .getMessageText()
                    .length()) {

                longest =
                        storedMessages[i];
            }
        }

        return longest.getMessageText();
    }

    public static void displayLongestStoredMessage() {

        String longest =
                getLongestStoredMessage();

        if (longest.isEmpty()) {

            System.out.println(
                    "No stored messages."
            );

        } else {

            System.out.println(
                    "\nLongest Stored Message:"
            );

            System.out.println(longest);
        }
    }

    // ==========================================
    // SEARCH MESSAGE ID
    // ==========================================

    public static void searchMessageID(
            Scanner scanner) {

        System.out.print(
                "Enter message ID: "
        );

        String searchID =
                scanner.nextLine();

        boolean found = false;

        for (int i = 0;
                i < storedCount;
                i++) {

            Message message =
                    storedMessages[i];

            if (message.getMessageID()
                    .equals(searchID)) {

                System.out.println(
                        "\nRecipient: "
                        + message.getRecipient()
                );

                System.out.println(
                        "Message: "
                        + message.getMessageText()
                );

                found = true;
                break;
            }
        }

        if (!found) {

            System.out.println(
                    "Message ID not found."
            );
        }
    }

    // ==========================================
    // SEARCH BY RECIPIENT
    // ==========================================

    public static void searchMessagesByRecipient(
            Scanner scanner) {

        System.out.print(
                "Enter recipient number: "
        );

        String searchRecipient =
                scanner.nextLine();

        boolean found = false;

        System.out.println(
                "\nMessages for recipient:"
        );

        for (int i = 0;
                i < storedCount;
                i++) {

            Message message =
                    storedMessages[i];

            if (message.getRecipient()
                    .equals(searchRecipient)) {

                System.out.println(
                        message.getMessageText()
                );

                found = true;
            }
        }

        if (!found) {

            System.out.println(
                    "No messages found for this recipient."
            );
        }
    }

    // ==========================================
    // DELETE MESSAGE USING HASH
    // ==========================================

    public static void deleteMessageByHash(
            Scanner scanner) {

        System.out.print(
                "Enter message hash: "
        );

        String searchHash =
                scanner.nextLine();

        Path filePath =
                Path.of("stored_messages.json");

        try {

            if (!Files.exists(filePath)) {

                System.out.println(
                        "No stored messages found."
                );

                return;
            }

            String json =
                    Files.readString(
                            filePath,
                            StandardCharsets.UTF_8
                    );

            if (json.trim().isEmpty()) {

                System.out.println(
                        "No stored messages found."
                );

                return;
            }

            JsonArray messages =
                    JsonParser
                            .parseString(json)
                            .getAsJsonArray();

            JsonArray remainingMessages =
                    new JsonArray();

            boolean found = false;

            for (JsonElement element :
                    messages) {

                JsonObject object =
                        element.getAsJsonObject();

                String hash =
                        object.get("messageHash")
                                .getAsString();

                if (hash.equals(searchHash)) {

                    found = true;

                } else {

                    remainingMessages.add(object);
                }
            }

            if (found) {

                Gson gson =
                        new GsonBuilder()
                                .setPrettyPrinting()
                                .create();

                Files.writeString(
                        filePath,
                        gson.toJson(remainingMessages),
                        StandardCharsets.UTF_8
                );

                loadStoredMessagesFromJson();

                System.out.println(
                        "Message successfully deleted."
                );

            } else {

                System.out.println(
                        "Message hash not found."
                );
            }

        } catch (IOException |
                IllegalStateException |
                com.google.gson.JsonParseException e) {

            System.out.println(
                    "Error deleting message: "
                    + e.getMessage()
            );
        }
    }

    // ==========================================
    // DISPLAY FULL STORED MESSAGE REPORT
    // ==========================================

    public static void displayReport() {

        if (storedCount == 0) {

            System.out.println(
                    "No stored messages."
            );

            return;
        }

        System.out.println(
                "\n================================"
        );

        System.out.println(
                "STORED MESSAGE REPORT"
        );

        System.out.println(
                "================================"
        );

        for (int i = 0;
                i < storedCount;
                i++) {

            Message message =
                    storedMessages[i];

            System.out.println(
                    "Message ID: "
                    + message.getMessageID()
            );

            System.out.println(
                    "Message Hash: "
                    + message.getMessageHash()
            );

            System.out.println(
                    "Recipient: "
                    + message.getRecipient()
            );

            System.out.println(
                    "Message: "
                    + message.getMessageText()
            );

            System.out.println(
                    "------------------------------"
            );
        }
    }

    // ==========================================
    // GETTERS FOR PART 3 ARRAYS
    // ==========================================

    public static Message[] getSentMessages() {
        return sentMessages;
    }

    public static Message[] getDisregardedMessages() {
        return disregardedMessages;
    }

    public static Message[] getStoredMessages() {
        return storedMessages;
    }

    public static String[] getMessageHashes() {
        return messageHashes;
    }

    public static String[] getMessageIDs() {
        return messageIDs;
    }
}