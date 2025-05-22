# 🎫 Ticket Booking Backend

---

## 🚆 Overview

This **Train Booking System** is a Java-based console application that simulates a railway ticket booking platform.  
Users can **search for trains** between stations, **book seats**, **view their bookings**, and **cancel reservations** with ease.

---

## 🛠️ Status

**Active development / Stable**

---

## ✨ Features

- **User Authentication**  
  - Sign up with a unique username and password  
  - Secure login with password hashing  

- **Train Search**  
  - Search trains between source and destination stations  
  - View train details including route and timings  

- **Seat Booking**  
  - Visual representation of available seats  
  - Book specific seats on selected trains  
  - Receive booking confirmation with ticket details  

- **Booking Management**  
  - View all your current bookings  
  - Cancel bookings using ticket ID  

---

## 💻 Technology Stack

| Technology | Purpose                    |
|------------|----------------------------|
| Java       | Core programming language  |
| JSON files | Persistent local data store|
| Jackson    | JSON serialization/deserialization |
| JBCrypt    | Password hashing           |
| Lombok     | Boilerplate code reduction |

---

## 📁 Project Structure

org.example
├── App.java # Main application entry point
├── entities
│ ├── Ticket.java # Ticket entity
│ ├── Train.java # Train entity
│ └── User.java # User entity
├── services
│ ├── TrainService.java # Train management service
│ └── UserBookingService.java # User and booking management
├── util
│ └── UserServiceUtil.java # Utility functions
└── localDB
├── train.json # Train data JSON file
└── users.json # User data JSON file

---


---

## 🚀 How to Use

1. **Start the Application**  
   Run the `App.java` file to launch the console application.

2. **Sign Up**  
   - Select option `1`  
   - Enter a unique username and password  

3. **Login**  
   - Select option `2`  
   - Enter your credentials  

4. **Search Trains**  
   - Select option `4`  
   - Enter source and destination stations  
   - Select a train from the displayed results  

5. **Book a Seat**  
   - Select option `5`  
   - View seat map and select an available seat  
   - Confirm your booking  

6. **View Bookings**  
   - Select option `3` to list all your bookings  

7. **Cancel Booking**  
   - Select option `6`  
   - Enter the ticket ID to cancel the booking  

---

## ⚙️ Installation

```bash
# Clone the repository
git clone https://github.com/yourusername/train-booking-system.git

# Open the project in your preferred Java IDE

# Build the project using Gradle
./gradlew build

# Run the application
./gradlew run
