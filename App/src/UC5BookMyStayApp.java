// Use Case 5 - Booking Request Queue (FIFO)
// Version: 5.1

import java.util.LinkedList;
import java.util.Queue;

// Reservation class (represents a booking request)
class Reservation {
    String guestName;
    String roomType;

    Reservation(String guestName, String roomType) {
        this.guestName = guestName;
        this.roomType = roomType;
    }

    void display() {
        System.out.println("Guest: " + guestName + " | Requested Room: " + roomType);
    }
}

// Booking Queue Manager
class BookingQueue {

    private Queue<Reservation> queue;

    BookingQueue() {
        queue = new LinkedList<>();
    }

    // Add booking request (enqueue)
    void addRequest(Reservation reservation) {
        queue.offer(reservation);
        System.out.println("Request added for " + reservation.guestName);
    }

    // View all requests (without removing)
    void viewRequests() {
        System.out.println("\n--- Booking Requests (FIFO Order) ---");

        for (Reservation r : queue) {
            r.display();
        }
    }
}

// Main class
public class UC5BookMyStayApp {

    public static void main(String[] args) {

        System.out.println("===== Book My Stay App (v5.1) =====");

        // Initialize queue
        BookingQueue bookingQueue = new BookingQueue();

        // Add booking requests (arrival order matters)
        bookingQueue.addRequest(new Reservation("Alice", "Single Room"));
        bookingQueue.addRequest(new Reservation("Bob", "Double Room"));
        bookingQueue.addRequest(new Reservation("Charlie", "Suite Room"));

        // Display queue (FIFO order preserved)
        bookingQueue.viewRequests();

        System.out.println("\nAll requests stored. No allocation done yet.");
    }
}