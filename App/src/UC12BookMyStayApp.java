import java.io.*;
import java.util.*;

// Reservation class (Serializable)
class Reservation implements Serializable {
    private static final long serialVersionUID = 1L;

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
        return "BookingID=" + bookingId +
               ", Guest=" + guestName +
               ", Room=" + roomType;
    }
}

// Wrapper class for full system state
class SystemState implements Serializable {
    private static final long serialVersionUID = 1L;

    Map<String, Integer> inventory;
    List<Reservation> bookings;

    public SystemState(Map<String, Integer> inventory, List<Reservation> bookings) {
        this.inventory = inventory;
        this.bookings = bookings;
    }
}

// Persistence Service
class PersistenceService {

    private static final String FILE_NAME = "system_state.ser";

    // Save state to file
    public static void save(SystemState state) {
        try (ObjectOutputStream oos =
                     new ObjectOutputStream(new FileOutputStream(FILE_NAME))) {

            oos.writeObject(state);
            System.out.println("System state saved successfully.");

        } catch (IOException e) {
            System.out.println("Error saving system state: " + e.getMessage());
        }
    }

    // Load state from file
    public static SystemState load() {
        File file = new File(FILE_NAME);

        if (!file.exists()) {
            System.out.println("No saved state found. Starting fresh.");
            return null;
        }

        try (ObjectInputStream ois =
                     new ObjectInputStream(new FileInputStream(FILE_NAME))) {

            System.out.println("System state loaded successfully.");
            return (SystemState) ois.readObject();

        } catch (IOException | ClassNotFoundException e) {
            System.out.println("Error loading state. Starting with clean state.");
            return null;
        }
    }
}

// Main class
public class UseCase12DataPersistenceRecovery {

    public static void main(String[] args) {

        Map<String, Integer> inventory;
        List<Reservation> bookings;

        // Attempt recovery
        SystemState loadedState = PersistenceService.load();

        if (loadedState != null) {
            inventory = loadedState.inventory;
            bookings = loadedState.bookings;
        } else {
            // Initialize fresh state
            inventory = new HashMap<>();
            inventory.put("Standard", 2);
            inventory.put("Deluxe", 1);
            inventory.put("Suite", 1);

            bookings = new ArrayList<>();
        }

        // Simulate new bookings
        Reservation r1 = new Reservation(1, "Alice", "Standard");
        Reservation r2 = new Reservation(2, "Bob", "Deluxe");

        // Process bookings safely
        processBooking(r1, inventory, bookings);
        processBooking(r2, inventory, bookings);

        // Display current state
        System.out.println("\nCurrent Inventory:");
        for (String key : inventory.keySet()) {
            System.out.println(key + ": " + inventory.get(key));
        }

        System.out.println("\nCurrent Bookings:");
        for (Reservation r : bookings) {
            System.out.println(r);
        }

        // Save state before shutdown
        SystemState state = new SystemState(inventory, bookings);
        PersistenceService.save(state);
    }

    // Booking logic
    public static void processBooking(Reservation r,
                                      Map<String, Integer> inventory,
                                      List<Reservation> bookings) {

        String type = r.getRoomType();

        if (!inventory.containsKey(type) || inventory.get(type) <= 0) {
            System.out.println("Booking Failed: No rooms available for " + type);
            return;
        }

        inventory.put(type, inventory.get(type) - 1);
        bookings.add(r);

        System.out.println("Booking Confirmed: " + r);
    }
}
