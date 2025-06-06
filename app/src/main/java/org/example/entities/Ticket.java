package org.example.entities;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import java.util.Date;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
public class Ticket {
    private String ticketId;
    private String userId;
    private String source;
    private String destination;
    private Date dateOfTravel;
    private Train train;
    private int seatRow = -1; // default: not set
    private int seatCol = -1; // default: not set

    // Original constructor (for backward compatibility)
    public Ticket(String ticketId, String userId, String source, String destination, Date dateOfTravel, Train train) {
        this.ticketId = ticketId;
        this.userId = userId;
        this.source = source;
        this.destination = destination;
        this.dateOfTravel = dateOfTravel;
        this.train = train;
    }

    // New constructor (for future use)
    public Ticket(String ticketId, String userId, String source, String destination, Date dateOfTravel, Train train, int seatRow, int seatCol) {
        this(ticketId, userId, source, destination, dateOfTravel, train);
        this.seatRow = seatRow;
        this.seatCol = seatCol;
    }

    public Ticket() {}

    public String getTicketInfo(){
        if (seatRow == -1 || seatCol == -1) {
            return String.format("Ticket ID: %s belongs to %s from %s to %s on %s",
                    ticketId, userId, source, destination, dateOfTravel);
        }
        return String.format("Ticket ID: %s belongs to %s from %s to %s on %s, Seat: (%d,%d)",
                ticketId, userId, source, destination, dateOfTravel, seatRow, seatCol);
    }

    public String getTicketId() { return ticketId; }
    public void setTicketId(String ticketId) { this.ticketId = ticketId; }
    public String getUserId() { return userId; }
    public void setUserId(String userId) { this.userId = userId; }
    public String getSource() { return source; }
    public void setSource(String source) { this.source = source; }
    public String getDestination() { return destination; }
    public void setDestination(String destination) { this.destination = destination; }
    public Date getDateOfTravel() { return dateOfTravel; }
    public void setDateOfTravel(Date dateOfTravel) { this.dateOfTravel = dateOfTravel; }
    public Train getTrain() { return train; }
    public void setTrain(Train train) { this.train = train; }
    public int getSeatRow() { return seatRow; }
    public void setSeatRow(int seatRow) { this.seatRow = seatRow; }
    public int getSeatCol() { return seatCol; }
    public void setSeatCol(int seatCol) { this.seatCol = seatCol; }
}