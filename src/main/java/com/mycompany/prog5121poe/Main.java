package com.mycompany.prog5121poe;

import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        try (Scanner scanner = new Scanner(System.in)) {

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

            Login login = new Login(firstName, lastName, username,
                    password, cellPhoneNumber);

            System.out.println(login.registerUser());

            if (!login.checkUserName() || !login.checkPasswordComplexity()
                    || !login.checkCellPhoneNumber()) {
                System.out.println("Registration failed. Please correct your details.");
                return;
            }

            System.out.println("Registration successful.");
            System.out.println("================================");
            System.out.println("LOGIN");
            System.out.println("================================");

            System.out.print("Enter username: ");
            String loginUsername = scanner.nextLine();
            System.out.print("Enter password: ");
            String loginPassword = scanner.nextLine();

            if (!login.loginUser(loginUsername, loginPassword)) {
                System.out.println("Username or password incorrect, please try again.");
                return;
            }

            System.out.println("Welcome " + firstName + ", " + lastName
                    + " it is great to see you again.");
            System.out.println("Welcome to QuickChat.");

            System.out.print("How many messages would you like to enter? ");
            int numberOfMessages;
            try {
                numberOfMessages = Integer.parseInt(scanner.nextLine());
                if (numberOfMessages < 0) {
                    System.out.println("Please enter zero or a positive number.");
                    return;
                }
            } catch (NumberFormatException e) {
                System.out.println("Please enter a valid whole number.");
                return;
            }

            int messageNumber = 0;
            int totalSent = 0;
            MessageManager manager = new MessageManager();
            boolean quit = false;

            while (!quit) {
                System.out.println("\n================================");
                System.out.println("QUICKCHAT MENU");
                System.out.println("1) Send Messages");
                System.out.println("2) Show recently sent messages");
                System.out.println("3) Quit");
                System.out.println("4) Stored Messages");
                System.out.print("Choose an option: ");

                String option = scanner.nextLine();

                switch (option) {
                    case "1" -> {
                        if (messageNumber >= numberOfMessages) {
                            System.out.println("You have reached the number of messages you selected.");
                            break;
                        }

                        for (; messageNumber < numberOfMessages; messageNumber++) {
                            System.out.println("\nMessage " + (messageNumber + 1));
                            System.out.print("Enter recipient number (+27 followed by 9 digits): ");
                            String recipient = scanner.nextLine();
                            System.out.print("Enter message (maximum 250 characters): ");
                            String messageText = scanner.nextLine();

                            Message message = new Message(messageNumber,
                                    cellPhoneNumber, recipient, messageText);

                            System.out.println(message.checkRecipientCell());
                            if (!message.checkRecipientCell().startsWith(
                                    "Cell phone number successfully")) {
                                break;
                            }

                            String lengthResult = message.checkMessageLength();
                            System.out.println(lengthResult);
                            if (!"Message ready to send.".equals(lengthResult)) {
                                break;
                            }

                            System.out.println("Message ID generated: " + message.getMessageID());
                            System.out.println("Message Hash: " + message.createMessageHash());
                            System.out.println("Choose what to do with this message:");
                            System.out.println("1) Send Message");
                            System.out.println("2) Disregard Message");
                            System.out.println("3) Store Message to send later");
                            System.out.print("Choose an option: ");

                            int action;
                            try {
                                action = Integer.parseInt(scanner.nextLine());
                            } catch (NumberFormatException e) {
                                action = -1;
                            }

                            System.out.println(message.SentMessage(action));
                            manager.addMessage(message);

                            if (action == 1) {
                                totalSent++;
                            }

                            System.out.println("\nFull message details:");
                            System.out.println(message.printMessages());
                        }
                    }

                    case "2" -> System.out.println(manager.displaySentMessageReport());

                    case "3" -> {
                        quit = true;
                        System.out.println("Total messages sent: " + totalSent);
                        System.out.println("Thank you for using QuickChat.");
                    }

                    case "4" -> storedMessagesMenu(scanner, manager);

                    default -> System.out.println("Invalid option. Please choose 1, 2, 3 or 4.");
                }
            }
        }
    }

    private static void storedMessagesMenu(Scanner scanner, MessageManager manager) {
        boolean back = false;

        while (!back) {
            System.out.println("\n========== STORED MESSAGES ==========");
            System.out.println("1) Display sender and recipient of stored messages");
            System.out.println("2) Display the longest stored message");
            System.out.println("3) Search for a message ID");
            System.out.println("4) Search sent or stored messages for a recipient");
            System.out.println("5) Delete a message using its message hash");
            System.out.println("6) Display stored messages report");
            System.out.println("7) Back to main menu");
            System.out.print("Choose an option: ");

            String choice = scanner.nextLine();

            switch (choice) {
                case "1" -> System.out.println(manager.displayStoredSendersAndRecipients());
                case "2" -> {
                    System.out.println("Longest stored message:");
                    System.out.println(manager.getLongestStoredMessage());
                }
                case "3" -> {
                    System.out.print("Enter message ID: ");
                    System.out.println(manager.searchMessageByID(scanner.nextLine()));
                }
                case "4" -> {
                    System.out.print("Enter recipient number: ");
                    System.out.println(manager.searchMessagesForRecipient(scanner.nextLine()));
                }
                case "5" -> {
                    System.out.print("Enter message hash: ");
                    System.out.println(manager.deleteStoredMessageByHash(scanner.nextLine()));
                }
                case "6" -> System.out.println(manager.displayStoredMessageReport());
                case "7" -> back = true;
                default -> System.out.println("Invalid option. Please choose 1 to 7.");
            }
        }
    }
}