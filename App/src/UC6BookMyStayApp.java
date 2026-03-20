// Use Case 6 - Reservation Confirmation & Room Allocation
// Version: 6.1

import java.util.*;

// Reservation (Booking Request)
class Reservation {
    String guestName;
    String roomType;

    Reservation(String guestName, String roomType) {
        this.guestName = guestName;
        this.roomType = roomType;
    }
}

// Inventory Service
class RoomInventory {
    private HashMap<String, Integer> inventory;

    RoomInventory() {
        inventory = new HashMap<>();
        inventory.put("Single Room", 2);
        inventory.put("Double Room", 1);
        inventory.put("Suite Room", 1);
    }

    int getAvailability(String roomType) {
        return inventory.getOrDefault(roomType, 0);
    }

    void decreaseRoom(String roomType) {
        int count = inventory.getOrDefault(roomType, 0);
        if (count > 0) {
            inventory.put(roomType, count - 1);
        }
    }

    void displayInventory() {
        System.out.println("\n--- Inventory Status ---");
        for (Map.Entry<String, Integer> entry : inventory.entrySet()) {
            System.out.println(entry.getKey() + " : " + entry.getValue());
        }
    }
}

// Booking Service (Core Logic)
class BookingService {

    private Queue<Reservation> queue;
    private Set<String> allocatedRoomIds;
    private HashMap<String, Set<String>> roomAllocations;

    BookingService() {
        queue = new LinkedList<>();
        allocatedRoomIds = new HashSet<>();
        roomAllocations = new HashMap<>();
    }

    // Add request
    void addRequest(Reservation r) {
        queue.offer(r);
    }

    // Generate unique room ID
    private String generateRoomId(String roomType) {
        return roomType.substring(0, 2).toUpperCase() + "-" + UUID.randomUUID().toString().substring(0, 4);
    }

    // Process booking requests
    void processBookings(RoomInventory inventory) {

        System.out.println("\n--- Processing Bookings ---");

        while (!queue.isEmpty()) {

            Reservation r = queue.poll();
            String type = r.roomType;

            // Check availability
            if (inventory.getAvailability(type) > 0) {

                String roomId;

                // Ensure uniqueness
                do {
                    roomId = generateRoomId(type);
                } while (allocatedRoomIds.contains(roomId));

                // Store ID
                allocatedRoomIds.add(roomId);

                // Map room type to allocated IDs
                roomAllocations.putIfAbsent(type, new HashSet<>());
                roomAllocations.get(type).add(roomId);

                // Update inventory
                inventory.decreaseRoom(type);

                // Confirm booking
                System.out.println("Booking CONFIRMED for " + r.guestName +
                        " | Room: " + type +
                        " | Room ID: " + roomId);

            } else {
                System.out.println("Booking FAILED for " + r.guestName +
                        " | No available " + type);
            }
        }
    }

    // Display allocated rooms
    void displayAllocations() {
        System.out.println("\n--- Allocated Rooms ---");
        for (Map.Entry<String, Set<String>> entry : roomAllocations.entrySet()) {
            System.out.println(entry.getKey() + " -> " + entry.getValue());
        }
    }
}

// Main Class
public class UC6BookMyStayApp {

    public static void main(String[] args) {

        System.out.println("===== Book My Stay App (v6.1) =====");

        // Initialize inventory
        RoomInventory inventory = new RoomInventory();

        // Initialize booking service
        BookingService bookingService = new BookingService();

        // Add booking requests (FIFO)
        bookingService.addRequest(new Reservation("Alice", "Single Room"));
        bookingService.addRequest(new Reservation("Bob", "Single Room"));
        bookingService.addRequest(new Reservation("Charlie", "Single Room")); // should fail
        bookingService.addRequest(new Reservation("David", "Suite Room"));

        // Process bookings
        bookingService.processBookings(inventory);

        // Show allocations
        bookingService.displayAllocations();

        // Show updated inventory
        inventory.displayInventory();

        System.out.println("\nAll bookings processed safely.");
    }
}