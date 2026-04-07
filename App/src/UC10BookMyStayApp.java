import java.util.*;

// Custom Exception for invalid operations
class BookingException extends Exception {
    public BookingException(String message) {
        super(message);
    }
}

// Reservation class
class Reservation {
    private int bookingId;
    private String guestName;
    private String roomType;

    public Reservation(int bookingId, String guestName, String roomType) {
        this.bookingId = bookingId;
        this.guestName = guestName;
        this.roomType = roomType;
    }

    public int getBookingId() {
        return bookingId;
    }

    public String getRoomType() {
        return roomType;
    }

    @Override
    public String toString() {
        return "Booking ID: " + bookingId +
               ", Guest: " + guestName +
               ", Room: " + roomType;
    }
}

// Booking History
class BookingHistory {
    private Map<Integer, Reservation> reservations = new HashMap<>();

    public void addReservation(Reservation r) {
        reservations.put(r.getBookingId(), r);
    }

    public Reservation getReservation(int id) {
        return reservations.get(id);
    }

    public void removeReservation(int id) {
        reservations.remove(id);
    }

    public Collection<Reservation> getAllReservations() {
        return reservations.values();
    }
}

// Cancellation Service
class CancellationService {

    private Map<String, Integer> inventory;
    private BookingHistory history;
    private Stack<String> rollbackStack;

    public CancellationService(Map<String, Integer> inventory, BookingHistory history) {
        this.inventory = inventory;
        this.history = history;
        this.rollbackStack = new Stack<>();
    }

    public void cancelBooking(int bookingId) {
        try {
            // Validate existence
            Reservation r = history.getReservation(bookingId);
            if (r == null) {
                throw new BookingException("Booking does not exist: " + bookingId);
            }

            String roomType = r.getRoomType();

            // Push to rollback stack
            rollbackStack.push(roomType);

            // Restore inventory
            inventory.put(roomType, inventory.getOrDefault(roomType, 0) + 1);

            // Remove booking from history
            history.removeReservation(bookingId);

            System.out.println("Cancellation Successful for Booking ID: " + bookingId);

        } catch (BookingException e) {
            System.out.println("Cancellation Failed: " + e.getMessage());
        }
    }

    public void showRollbackStack() {
        System.out.println("\nRollback Stack (Recent Releases): " + rollbackStack);
    }
}

// Booking Service (for creating bookings)
class BookingService {

    private Map<String, Integer> inventory;
    private BookingHistory history;

    public BookingService(Map<String, Integer> inventory, BookingHistory history) {
        this.inventory = inventory;
        this.history = history;
    }

    public void createBooking(Reservation r) {
        String type = r.getRoomType();

        if (!inventory.containsKey(type) || inventory.get(type) <= 0) {
            System.out.println("Booking Failed: No rooms available for " + type);
            return;
        }

        inventory.put(type, inventory.get(type) - 1);
        history.addReservation(r);

        System.out.println("Booking Confirmed: " + r);
    }
}

// Main class
public class UseCase10BookingCancellation {

    public static void main(String[] args) {

        // Initialize inventory
        Map<String, Integer> inventory = new HashMap<>();
        inventory.put("Standard", 2);
        inventory.put("Deluxe", 1);
        inventory.put("Suite", 1);

        BookingHistory history = new BookingHistory();
        BookingService bookingService = new BookingService(inventory, history);
        CancellationService cancellationService = new CancellationService(inventory, history);

        // Create bookings
        bookingService.createBooking(new Reservation(1, "Alice", "Deluxe"));
        bookingService.createBooking(new Reservation(2, "Bob", "Suite"));
        bookingService.createBooking(new Reservation(3, "Charlie", "Standard"));

        // Attempt cancellation
        cancellationService.cancelBooking(2); // valid
        cancellationService.cancelBooking(5); // invalid (does not exist)

        // Show current inventory
        System.out.println("\nUpdated Inventory:");
        for (String type : inventory.keySet()) {
            System.out.println(type + ": " + inventory.get(type));
        }

        // Show remaining bookings
        System.out.println("\nRemaining Bookings:");
        for (Reservation r : history.getAllReservations()) {
            System.out.println(r);
        }

        // Show rollback stack
        cancellationService.showRollbackStack();
    }
}
