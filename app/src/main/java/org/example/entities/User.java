package org.example.entities;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
@JsonIgnoreProperties(ignoreUnknown = true)
public class User {

    private String username;
    private String userId;
    private String password;
    private String hashedPassword;
    private List<Ticket> ticketsBooked;

    // Constructor
    public User(){}

    public User(String username, String password, String hashedPassword, List<Ticket> ticketsBooked, String userId){
        this.username = username;
        this.userId = userId;
        this.password = password;
        this.hashedPassword = hashedPassword;
        this.ticketsBooked = ticketsBooked != null ? ticketsBooked : new ArrayList<>(); // Safe handling for null lists
    }

    // Getter's
    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getHashedPassword() {
        return hashedPassword;
    }

    public List<Ticket> getTicketsBooked() {
        if (ticketsBooked == null) {
            ticketsBooked = new ArrayList<>();
        }
        return ticketsBooked;
    }

    public void printTickets(){
        if(ticketsBooked == null || ticketsBooked.isEmpty()){
            System.out.println("No tickets booked yet!");
            return;
        } else {
            System.out.println("Your bookings:");
            for (Ticket ticket : ticketsBooked) {
                System.out.println(ticket.getTicketInfo());
            }
        }
    }

    public String getUserId() {
        return userId;
    }

    // Setter's
    public void setUsername(String username) {
        this.username = username;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public void setTicketsBooked(List<Ticket> ticketsBooked) {
        this.ticketsBooked = ticketsBooked;
    }

    public void setHashedPassword(String hashedPassword) {
        this.hashedPassword = hashedPassword;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}