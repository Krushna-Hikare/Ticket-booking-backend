package org.example;

import org.example.entities.Train;
import org.example.entities.User;
import org.example.services.UserBookingService;
import org.example.util.UserServiceUtil;

import java.io.IOException;
import java.util.*;

public class App {

    public static void main(String[] args) {
        System.out.println("Running Train Booking System");
        Scanner scanner = new Scanner(System.in);
        int option = 0;
        UserBookingService userBookingService = null;
        boolean isLoggedIn = false;

        try {
            userBookingService = new UserBookingService();
        } catch (IOException e) {
            System.out.println("There is something wrong initializing the booking service.");
            e.printStackTrace();  // Print the actual exception details
            return;
        }

        Train trainSelectedForBooking = null;

        while (option != 7) {
            System.out.println("\nChoose option");
            System.out.println("1. Sign up");
            System.out.println("2. Login");
            System.out.println("3. Fetch Bookings" + (isLoggedIn ? "" : " (Login required)"));
            System.out.println("4. Search Trains");
            System.out.println("5. Book a Seat" + (isLoggedIn ? "" : " (Login required)"));
            System.out.println("6. Cancel my Booking" + (isLoggedIn ? "" : " (Login required)"));
            System.out.println("7. Exit the App");

            if (!scanner.hasNextInt()) {
                System.out.println("Please enter a valid number.");
                scanner.next(); // Consume invalid input
                continue;
            }
            option = scanner.nextInt();
            scanner.nextLine(); // Consume newline

            switch (option) {
                case 1:
                    System.out.println("Enter the username to sign up:");
                    String nameToSignUp = scanner.nextLine();
                    System.out.println("Enter the password to sign up:");
                    String passwordToSignUp = scanner.nextLine();
                    User userToSignup = new User(
                            nameToSignUp,
                            passwordToSignUp,
                            UserServiceUtil.hashPassword(passwordToSignUp),
                            new ArrayList<>(),
                            UUID.randomUUID().toString()
                    );
                    userBookingService.signUp(userToSignup);
                    break;

                case 2:
                    System.out.println("Enter the username to log in:");
                    String nameToLogin = scanner.nextLine();
                    System.out.println("Enter the password to log in:");
                    String passwordToLogin = scanner.nextLine();
                    User userToLogin = new User(
                            nameToLogin,
                            passwordToLogin,
                            "", // We don't need to hash here, the service will check
                            new ArrayList<>(),
                            ""
                    );
                    try {
                        userBookingService = new UserBookingService(userToLogin);
                        isLoggedIn = true;
                        System.out.println("Login successful! Welcome " + nameToLogin);
                    } catch (IOException ex) {
                        System.out.println("Login failed. Please try again.");
                    }
                    break;

                case 3:
                    if (!isLoggedIn) {
                        System.out.println("Please login first.");
                        break;
                    }
                    System.out.println("Fetching your bookings...");
                    userBookingService.fetchBookings();
                    break;

                case 4:
                    System.out.println("Enter the source station:");
                    String source = scanner.nextLine();
                    System.out.println("Enter the destination station:");
                    String dest = scanner.nextLine();
                    List<Train> trains = userBookingService.getTrains(source, dest);
                    if (trains.isEmpty()) {
                        System.out.println("No trains available for this route.");
                        break;
                    }
                    int index = 1;
                    for (Train t : trains) {
                        System.out.println(index + ". Train ID: " + t.getTrainId() + " | Train No: " + t.getTrainNo());
                        System.out.println("   Route: " + String.join(" -> ", t.getStations()));
                        for (Map.Entry<String, String> entry : t.getStationTimes().entrySet()) {
                            System.out.println("   (Time) " + entry.getKey() + " - " + entry.getValue());
                        }
                        index++;
                    }
                    System.out.println("Select a train by typing the corresponding number (or 0 to cancel):");
                    int selectedIndex = scanner.nextInt() - 1;
                    scanner.nextLine(); // Consume newline
                    if (selectedIndex < -1 || selectedIndex >= trains.size()) {
                        System.out.println("Invalid selection.");
                        break;
                    }
                    if (selectedIndex == -1) {
                        System.out.println("Selection cancelled.");
                        break;
                    }
                    trainSelectedForBooking = trains.get(selectedIndex);
                    System.out.println("✅ Train Selected: " + trainSelectedForBooking.getTrainId());
                    break;

                case 5:
                    if (!isLoggedIn) {
                        System.out.println("Please login first.");
                        break;
                    }
                    if (trainSelectedForBooking == null) {
                        System.out.println("Please search and select a train first (Option 4).");
                        break;
                    }
                    System.out.println("Available Seats (0 = available, 1 = booked):");
                    List<List<Integer>> seats = userBookingService.fetchSeats(trainSelectedForBooking);
                    for (int i = 0; i < seats.size(); i++) {
                        System.out.print("Row " + i + ": ");
                        for (Integer val : seats.get(i)) {
                            System.out.print(val + " ");
                        }
                        System.out.println();
                    }
                    System.out.println("Enter the row:");
                    int row = scanner.nextInt();
                    System.out.println("Enter the column:");
                    int col = scanner.nextInt();
                    scanner.nextLine(); // Consume newline
                    System.out.println("Booking your seat....");
                    boolean booked = userBookingService.bookTrainSeat(trainSelectedForBooking, row, col);
                    if (booked) {
                        System.out.println("Booked! Enjoy your journey");
                    } else {
                        System.out.println("Can't book this seat");
                    }
                    break;

                case 6:
                    if (!isLoggedIn) {
                        System.out.println("Please login first.");
                        break;
                    }
                    System.out.println("Enter the ticket ID to cancel the booking:");
                    String ticketIdToCancel = scanner.nextLine();
                    boolean canceled = userBookingService.cancelBooking(ticketIdToCancel);
            }}
        scanner.close();
    }}
