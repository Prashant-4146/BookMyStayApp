import java.util.*;

// Custom Exception for invalid booking scenarios
class InvalidBookingException extends Exception {
    public InvalidBookingException(String message) {
        super(message);
    }
}

// Reservation class
class Reservation {
    private int bookingId;
    private String guestName;
    private String roomType;
    private int nights;

    public Reservation(int bookingId, String guestName, String roomType, int nights) {
        this.bookingId = bookingId;
        this.guestName = guestName;
        this.roomType = roomType;
        this.nights = nights;
    }

    public String getRoomType() {
        return roomType;
    }

    public int getNights() {
        return nights;
    }

    @Override
    public String toString() {
        return "Booking ID: " + bookingId +
               ", Guest: " + guestName +
               ", Room: " + roomType +
               ", Nights: " + nights;
    }
}

// BookingHistory class
class BookingHistory {
    private List<Reservation> reservations = new ArrayList<>();

    public void addReservation(Reservation r) {
        reservations.add(r);
    }

    public List<Reservation> getAllReservations() {
        return Collections.unmodifiableList(reservations);
    }
}

// Validator class
class InvalidBookingValidator {

    private static final Set<String> VALID_ROOM_TYPES =
            new HashSet<>(Arrays.asList("Standard", "Deluxe", "Suite"));

    public static void validate(Reservation r, Map<String, Integer> inventory)
            throws InvalidBookingException {

        // Validate room type
        if (!VALID_ROOM_TYPES.contains(r.getRoomType())) {
            throw new InvalidBookingException("Invalid room type: " + r.getRoomType());
        }

        // Validate nights
        if (r.getNights() <= 0) {
            throw new InvalidBookingException("Number of nights must be greater than 0");
        }

        // Validate inventory availability
        int available = inventory.getOrDefault(r.getRoomType(), 0);

        if (available <= 0) {
            throw new InvalidBookingException("No rooms available for type: " + r.getRoomType());
        }
    }
}

// Booking Service (handles booking + validation)
class BookingService {

    private Map<String, Integer> inventory = new HashMap<>();
    private BookingHistory history;

    public BookingService(BookingHistory history) {
        this.history = history;

        // Initialize inventory
        inventory.put("Standard", 2);
        inventory.put("Deluxe", 2);
        inventory.put("Suite", 1);
    }

    public void createBooking(Reservation r) {
        try {
            // Validate first (Fail-Fast)
            InvalidBookingValidator.validate(r, inventory);

            // Update inventory safely
            String type = r.getRoomType();
            inventory.put(type, inventory.get(type) - 1);

            // Store booking
            history.addReservation(r);

            System.out.println("Booking Successful: " + r);

        } catch (InvalidBookingException e) {
            // Graceful failure handling
            System.out.println("Booking Failed: " + e.getMessage());
        }
    }

    public void showInventory() {
        System.out.println("\nCurrent Inventory:");
        for (String type : inventory.keySet()) {
            System.out.println(type + ": " + inventory.get(type));
        }
    }
}

// Main class
public class UseCase9ErrorHandlingValidation {

    public static void main(String[] args) {

        BookingHistory history = new BookingHistory();
        BookingService service = new BookingService(history);

        // Valid booking
        Reservation r1 = new Reservation(1, "Alice", "Deluxe", 3);

        // Invalid room type
        Reservation r2 = new Reservation(2, "Bob", "Premium", 2);

        // Invalid nights
        Reservation r3 = new Reservation(3, "Charlie", "Suite", 0);

        // Exceed inventory
        Reservation r4 = new Reservation(4, "David", "Suite", 2);
        Reservation r5 = new Reservation(5, "Eve", "Suite", 1);

        // Process bookings
        service.createBooking(r1);
        service.createBooking(r2);
        service.createBooking(r3);
        service.createBooking(r4);
        service.createBooking(r5);

        // Show inventory after operations
        service.showInventory();

        // Show booking history
        System.out.println("\nConfirmed Bookings:");
        for (Reservation r : history.getAllReservations()) {
            System.out.println(r);
        }
    }
}
