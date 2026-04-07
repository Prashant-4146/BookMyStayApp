import java.util.*;

// Reservation class representing a booking
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

    public int getBookingId() {
        return bookingId;
    }

    public String getGuestName() {
        return guestName;
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

// BookingHistory class to store confirmed reservations
class BookingHistory {
    private List<Reservation> reservations;

    public BookingHistory() {
        reservations = new ArrayList<>();
    }

    // Add confirmed booking
    public void addReservation(Reservation reservation) {
        reservations.add(reservation);
    }

    // Retrieve all bookings
    public List<Reservation> getAllReservations() {
        return Collections.unmodifiableList(reservations); // Prevent modification
    }
}

// BookingReportService class to generate reports
class BookingReportService {

    // Display all bookings
    public void showAllBookings(List<Reservation> reservations) {
        System.out.println("\n--- Booking History ---");
        for (Reservation r : reservations) {
            System.out.println(r);
        }
    }

    // Generate summary report
    public void generateSummary(List<Reservation> reservations) {
        System.out.println("\n--- Booking Summary Report ---");

        int totalBookings = reservations.size();
        int totalNights = 0;

        Map<String, Integer> roomTypeCount = new HashMap<>();

        for (Reservation r : reservations) {
            totalNights += r.getNights();

            roomTypeCount.put(
                r.getRoomType(),
                roomTypeCount.getOrDefault(r.getRoomType(), 0) + 1
            );
        }

        System.out.println("Total Bookings: " + totalBookings);
        System.out.println("Total Nights Booked: " + totalNights);

        System.out.println("\nRoom Type Distribution:");
        for (String type : roomTypeCount.keySet()) {
            System.out.println(type + ": " + roomTypeCount.get(type));
        }
    }
}

// Main class
public class UseCase8BookingHistoryReport {

    public static void main(String[] args) {

        // Initialize components
        BookingHistory bookingHistory = new BookingHistory();
        BookingReportService reportService = new BookingReportService();

        // Simulate confirmed bookings
        Reservation r1 = new Reservation(1, "Alice", "Deluxe", 3);
        Reservation r2 = new Reservation(2, "Bob", "Standard", 2);
        Reservation r3 = new Reservation(3, "Charlie", "Suite", 5);
        Reservation r4 = new Reservation(4, "David", "Deluxe", 1);

        // Add to booking history (in order)
        bookingHistory.addReservation(r1);
        bookingHistory.addReservation(r2);
        bookingHistory.addReservation(r3);
        bookingHistory.addReservation(r4);

        // Admin views booking history
        List<Reservation> storedReservations = bookingHistory.getAllReservations();
        reportService.showAllBookings(storedReservations);

        // Admin generates report
        reportService.generateSummary(storedReservations);
    }
}
