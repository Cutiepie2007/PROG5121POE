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
                        }
                    }
                }
            }
        }
    }
}
