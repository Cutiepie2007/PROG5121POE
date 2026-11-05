package com.mycompany.prog5121poe;

import java.util.Scanner;
import java.util.ArrayList;

public class Main {

    public static void main(String[] args) {

        try (Scanner scanner = new Scanner(System.in)) {

            // Gather information from the user and create a Login object.
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

            // Validate the user's registration.
            System.out.println("================================");
            System.out.println("REGISTRATION RESULTS");
            System.out.println("================================");

            if (login.checkUserName()) {
                System.out.println("Username successfully captured.");
            } else {
                System.out.println("Username is incorrectly formatted.");
            }

            if (login.checkPasswordComplexity()) {
                System.out.println("Password successfully captured.");
            } else {
                System.out.println("Password is incorrectly formatted.");
            }

            if (login.checkCellPhoneNumber()) {
                System.out.println("Cell phone number successfully captured.");
            } else {
                System.out.println("Cell phone number is incorrectly formatted.");
            }

            // Check whether registration was successful.
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

            // Prompt the user for login information.
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

            System.out.print(
                    "How many messages would you like to enter? "
            );

            int numberOfMessages;

            try {
                numberOfMessages = Integer.parseInt(scanner.nextLine());

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

            // Store messages sent during the current session.
            ArrayList<String> sentMessages = new ArrayList<>();

            boolean quit = false;

            // Display the menu until the user chooses to quit.
            while (!quit) {

                System.out.println("\n================================");
                System.out.println("QUICKCHAT MENU");
                System.out.println("1) Send Messages");
                System.out.println("2) Show recently sent messages");
                System.out.println("3) Quit");
                System.out.print("Choose an option: ");

                String option = scanner.nextLine();

                switch (option) {

                    // Allow the user to send messages.
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

                            if (!message.checkMessageID()) {
                                System.out.println(
                                        "Message ID is incorrectly formatted."
                                );
                                continue;
                            }

                            String recipientResult =
                                    message.checkRecipientCell();

                            System.out.println(recipientResult);

                            if (!recipientResult.startsWith(
                                    "Cell phone number successfully")) {

                                messageNumber--;
                                break;
                            }

                            String lengthResult =
                                    message.checkMessageLength();

                            System.out.println(lengthResult);

                            if (!"Message ready to send.".equals(
                                    lengthResult)) {

                                messageNumber--;
                                break;
                            }

                            System.out.println(
                                    "Message ID generated: "
                                    + message.getMessageID()
                            );

                            System.out.println(
                                    "Message Hash: "
                                    + message.createMessageHash()
                            );

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
                                action = Integer.parseInt(
                                        scanner.nextLine()
                                );

                            } catch (NumberFormatException e) {
                                action = -1;
                            }

                            System.out.println(
                                    message.SentMessage(action)
                            );

                            // Keep track of sent messages.
                            if (action == 1) {
                                totalSent++;
                                sentMessages.add(
                                        message.printMessages()
                                );
                            }

                            System.out.println(
                                    "\nFull message details:"
                            );

                            System.out.println(
                                    message.printMessages()
                            );
                        }
                    }

                    // Display recently sent messages.
                    case "2" -> {

                        if (sentMessages.isEmpty()) {
                            System.out.println(
                                    "No recently sent messages."
                            );

                        } else {

                            System.out.println(
                                    "Recently Sent Messages:"
                            );

                            for (int i = sentMessages.size() - 1;
                                    i >= 0; i--) {

                                System.out.println(
                                        "------------------------------"
                                );

                                System.out.println(
                                        sentMessages.get(i)
                                );
                            }
                        }
                    }

                    // Exit the program.
                    case "3" -> {

                        quit = true;

                        System.out.println(
                                "Total messages sent: " + totalSent
                        );

                        System.out.println(
                                "Thank you for using QuickChat."
                        );
                    }

                    default -> System.out.println(
                            "Invalid option. Please choose 1, 2 or 3."
                    );
                }
            }
        }
    }
}