package org.example.services;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import org.example.entities.Ticket;
import org.example.entities.Train;
import org.example.entities.User;
import org.example.util.UserServiceUtil;

import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

public class UserBookingService {

    private ObjectMapper objectMapper;
    private List<User> userList;
    private User user;
    private final String USER_FILE_PATH = "D:\\IRCTC\\app\\src\\main\\java\\org\\example\\localDB\\users.json";

    public UserBookingService(User user) throws IOException {
        this.user = user;
        initObjectMapper();
        loadUserListFromFile();
        if (!loginUser()) {
            throw new IOException("Login failed: Invalid credentials");
        }
    }

    public UserBookingService() throws IOException {
        initObjectMapper();
        loadUserListFromFile();
    }

    private void initObjectMapper() {
        objectMapper = new ObjectMapper();
        // Configure ObjectMapper to handle snake_case property names
        objectMapper.setPropertyNamingStrategy(PropertyNamingStrategy.SNAKE_CASE);
        // Configure ObjectMapper to ignore unknown properties
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    }

    private void loadUserListFromFile() throws IOException {
        File file = new File(USER_FILE_PATH);
        if (file.exists() && file.length() > 0) {
            userList = objectMapper.readValue(file, new TypeReference<List<User>>() {});
        } else {
            userList = new ArrayList<>();
        }
    }

    private void saveUserListToFile() throws IOException {
        objectMapper.writeValue(new File(USER_FILE_PATH), userList);
    }

    public Boolean loginUser() {
        return userList.stream().anyMatch(u ->
                user.getUsername() != null &&
                        u.getUsername() != null &&
                        user.getUsername().equals(u.getUsername()) &&
                        UserServiceUtil.checkPassword(user.getPassword(), u.getHashedPassword())
        );
    }

    public Boolean signUp(User user1) {
        try {
            // Check if username already exists
            if (userList.stream().anyMatch(u ->
                    user1.getUsername() != null &&
                            u.getUsername() != null &&
                            user1.getUsername().equals(u.getUsername()))) {
                System.out.println("Username already exists. Please choose a different username.");
                return Boolean.FALSE;
            }

            userList.add(user1);
            saveUserListToFile();
            System.out.println("User registered successfully!");
            return Boolean.TRUE;
        } catch (IOException ex) {
            System.out.println("Error during signup: " + ex.getMessage());
            return Boolean.FALSE;
        }
    }

    public void fetchBookings() {
        Optional<User> userFetched = userList.stream().filter(u ->
                user.getUsername() != null &&
                        u.getUsername() != null &&
                        user.getUsername().equals(u.getUsername()) &&
                        UserServiceUtil.checkPassword(user.getPassword(), u.getHashedPassword())
        ).findFirst();

        userFetched.ifPresent(User::printTickets);
    }

    public Boolean cancelBooking(String ticketId) {
        if (ticketId == null || ticketId.trim().isEmpty()) {
            System.out.println("Ticket ID cannot be null or empty.");
            return Boolean.FALSE;
        }

        Optional<User> currentUser = userList.stream().filter(u ->
                user.getUsername() != null &&
                        u.getUsername() != null &&
                        user.getUsername().equals(u.getUsername()) &&
                        UserServiceUtil.checkPassword(user.getPassword(), u.getHashedPassword())
        ).findFirst();

        if (currentUser.isPresent()) {
            User realUser = currentUser.get();
            boolean removed = realUser.getTicketsBooked().removeIf(ticket -> ticket.getTicketId().equals(ticketId));
            if (removed) {
                try {
                    saveUserListToFile();
                } catch (IOException e) {
                    System.out.println("Error saving after cancellation.");
                }
                System.out.println("Ticket with ID " + ticketId + " has been canceled.");
                return Boolean.TRUE;
            } else {
                System.out.println("No ticket found with ID " + ticketId);
            }
        }
        return Boolean.FALSE;
    }

    public List<Train> getTrains(String source, String destination) {
        try {
            TrainService trainService = new TrainService();
            return trainService.searchTrains(source, destination);
        } catch (IOException ex) {
            System.out.println("Error fetching trains: " + ex.getMessage());
            return new ArrayList<>();
        }
    }

    public List<List<Integer>> fetchSeats(Train train) {
        return train.getSeats();
    }

    public Boolean bookTrainSeat(Train train, int row, int seat) {
        try {
            // Get the current user
            Optional<User> currentUser = userList.stream().filter(u ->
                    user.getUsername() != null &&
                            u.getUsername() != null &&
                            user.getUsername().equals(u.getUsername()) &&
                            UserServiceUtil.checkPassword(user.getPassword(), u.getHashedPassword())
            ).findFirst();

            if (!currentUser.isPresent()) {
                System.out.println("User not found or not logged in.");
                return Boolean.FALSE;
            }

            List<List<Integer>> seats = train.getSeats();
            if (row >= 0 && row < seats.size() && seat >= 0 && seat < seats.get(row).size()) {
                if (seats.get(row).get(seat) == 0) {
                    seats.get(row).set(seat, 1);
                    train.setSeats(seats);

                    // Create a new ticket
                    String ticketId = UserServiceUtil.generateTicketId();
                    Ticket newTicket = new Ticket(
                            ticketId,
                            currentUser.get().getUserId(),
                            train.getStations().get(0), // Source
                            train.getStations().get(train.getStations().size() - 1), // Destination
                            new Date(), // Current date
                            train,
                            row,
                            seat
                    );

                    // Add ticket to user's bookings
                    User realUser = currentUser.get();
                    realUser.getTicketsBooked().add(newTicket);

                    // Save changes
                    new TrainService().updateTrain(train);
                    saveUserListToFile();

                    return Boolean.TRUE;
                } else {
                    System.out.println("This seat is already booked.");
                }
            } else {
                System.out.println("Invalid seat selection.");
            }
            return Boolean.FALSE;
        } catch (IOException ex) {
            System.out.println("Error booking seat: " + ex.getMessage());
            return Boolean.FALSE;
        }
    }
}