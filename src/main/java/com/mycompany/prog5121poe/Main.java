package com.mycompany.prog5121poe;

import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        // Scanner is used to get input from the user.
        try (Scanner scanner = new Scanner(System.in)) {

            // Display the registration heading.
            System.out.println("================================");
            System.out.println("QUICKCHAT REGISTRATION");
            System.out.println("================================");

            // Get the user's registration details.
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

            // Create a Login object using the user's details.
            Login login = new Login(firstName, lastName, username,
                    password, cellPhoneNumber);

            // Register the user and display the registration result.
            System.out.println(login.registerUser());

            // Check if all registration details are valid.
            if (!login.checkUserName() || !login.checkPasswordComplexity()
                    || !login.checkCellPhoneNumber()) {
                System.out.println("Registration failed. Please correct your details.");
                return;
            }

            // Ask the registered user to log in.
            System.out.println("Registration successful.");
            System.out.println("================================");
            System.out.println("LOGIN");
            System.out.println("================================");

            System.out.print("Enter username: ");
            String loginUsername = scanner.nextLine();
            System.out.print("Enter password: ");
            String loginPassword = scanner.nextLine();

            // Check whether the entered login details are correct.
            if (!login.loginUser(loginUsername, loginPassword)) {
                System.out.println("Username or password incorrect, please try again.");
                return;
            }

            // Welcome the user after a successful login.
            System.out.println("Welcome " + firstName + ", " + lastName
                    + " it is great to see you again.");
            System.out.println("Welcome to QuickChat.");

            // Ask how many messages the user wants to enter.
            System.out.print("How many messages would you like to enter? ");
            int numberOfMessages;
            try {
                numberOfMessages = Integer.parseInt(scanner.nextLine());

                // Make sure the number of messages is not negative.
                if (numberOfMessages < 0) {
                    System.out.println("Please enter zero or a positive number.");
                    return;
                }
            } catch (NumberFormatException e) {
                // Display an error if the user does not enter a whole number.
                System.out.println("Please enter a valid whole number.");
                return;
            }

            // Set up the message counters and message manager.
            int messageNumber = 0;
            int totalSent = 0;
            MessageManager manager = new MessageManager();
            boolean quit = false;

            // Keep displaying the main menu until the user chooses to quit.
            while (!quit) {
                System.out.println("\n================================");
                System.out.println("QUICKCHAT MENU");
                System.out.println("1) Send Messages");
                System.out.println("2) Show recently sent messages");
                System.out.println("3) Quit");
                System.out.println("4) Stored Messages");
                System.out.print("Choose an option: ");

                String option = scanner.nextLine();

                // Perform an action based on the user's menu choice.
                switch (option) {
                    case "1" -> {
                        // Check whether the selected message limit has been reached.
                        if (messageNumber >= numberOfMessages) {
                            System.out.println("You have reached the number of messages you selected.");
                            break;
                        }

                        // Repeat until the selected number of messages has been reached.
                        for (; messageNumber < numberOfMessages; messageNumber++) {
                            System.out.println("\nMessage " + (messageNumber + 1));

                            // Get the recipient's number and message text.
                            System.out.print("Enter recipient number (+27 followed by 9 digits): ");
                            String recipient = scanner.nextLine();
                            System.out.print("Enter message (maximum 250 characters): ");
                            String messageText = scanner.nextLine();

                            // Create a new Message object.
                            Message message = new Message(messageNumber,
                                    cellPhoneNumber, recipient, messageText);

                            // Validate the recipient's cell phone number.
                            System.out.println(message.checkRecipientCell());
                            if (!message.checkRecipientCell().startsWith(
                                    "Cell phone number successfully")) {
                                break;
                            }

                            // Check that the message meets the length requirement.
                            String lengthResult = message.checkMessageLength();
                            System.out.println(lengthResult);
                            if (!"Message ready to send.".equals(lengthResult)) {
                                break;
                            }

                            // Display the generated message ID and hash.
                            System.out.println("Message ID generated: " + message.getMessageID());
                            System.out.println("Message Hash: " + message.createMessageHash());

                            // Ask the user what to do with the message.
                            System.out.println("Choose what to do with this message:");
                            System.out.println("1) Send Message");
                            System.out.println("2) Disregard Message");
                            System.out.println("3) Store Message to send later");
                            System.out.print("Choose an option: ");

                            // Get the user's message action.
                            int action;
                            try {
                                action = Integer.parseInt(scanner.nextLine());
                            } catch (NumberFormatException e) {
                                // Use an invalid option if the input is not a number.
                                action = -1;
                            }

                            // Process the selected message action.
                            System.out.println(message.SentMessage(action));
                            manager.addMessage(message);

                            // Increase the total only when a message is sent.
                            if (action == 1) {
                                totalSent++;
                            }

                            // Display the complete details of the message.
                            System.out.println("\nFull message details:");
                            System.out.println(message.printMessages());
                        }
                    }

                    // Display the report of sent messages.
                    case "2" -> System.out.println(manager.displaySentMessageReport());

                    // Exit the application and display the total messages sent.
                    case "3" -> {
                        quit = true;
                        System.out.println("Total messages sent: " + totalSent);
                        System.out.println("Thank you for using QuickChat.");
                    }

                    // Open the stored messages menu.
                    case "4" -> storedMessagesMenu(scanner, manager);

                    // Handle an invalid main menu choice.
                    default -> System.out.println("Invalid option. Please choose 1, 2, 3 or 4.");
                }
            }
        }
    }

    // This method displays the options for managing stored messages.
    private static void storedMessagesMenu(Scanner scanner, MessageManager manager) {
        boolean back = false;

        // Keep displaying the stored messages menu until the user goes back.
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

            // Perform the selected stored message action.
            switch (choice) {
                case "1" -> System.out.println(manager.displayStoredSendersAndRecipients());

                case "2" -> {
                    // Display the longest stored message.
                    System.out.println("Longest stored message:");
                    System.out.println(manager.getLongestStoredMessage());
                }

                case "3" -> {
                    // Search for a message using its ID.
                    System.out.print("Enter message ID: ");
                    System.out.println(manager.searchMessageByID(scanner.nextLine()));
                }

                case "4" -> {
                    // Search for messages using the recipient's number.
                    System.out.print("Enter recipient number: ");
                    System.out.println(manager.searchMessagesForRecipient(scanner.nextLine()));
                }

                case "5" -> {
                    // Delete a stored message using its hash.
                    System.out.print("Enter message hash: ");
                    System.out.println(manager.deleteStoredMessageByHash(scanner.nextLine()));
                }

                case "6" -> {
                    // Display the stored messages report.
                    System.out.println(manager.displayStoredMessageReport());
                }

                // Return to the main menu.
                case "7" -> back = true;

                // Handle an invalid stored messages menu choice.
                default -> System.out.println("Invalid option. Please choose 1 to 7.");
            }
        }
    }
}