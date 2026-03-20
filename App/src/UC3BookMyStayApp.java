// Use Case 3 - Centralized Room Inventory Management
// Version: 3.1

import java.util.HashMap;
import java.util.Map;

// Inventory class (Single Source of Truth)
class RoomInventory {

    private HashMap<String, Integer> inventory;

    // Constructor - initialize inventory
    RoomInventory() {
        inventory = new HashMap<>();

        // Register room types with availability
        inventory.put("Single Room", 5);
        inventory.put("Double Room", 3);
        inventory.put("Suite Room", 2);
    }

    // Get availability of a specific room type
    int getAvailability(String roomType) {
        return inventory.getOrDefault(roomType, 0);
    }

    // Update availability (add/remove rooms)
    void updateAvailability(String roomType, int change) {
        int current = inventory.getOrDefault(roomType, 0);
        int updated = current + change;

        if (updated >= 0) {
            inventory.put(roomType, updated);
        } else {
            System.out.println("Cannot reduce below 0 for " + roomType);
        }
    }

    // Display full inventory
    void displayInventory() {
        System.out.println("\n--- Current Room Inventory ---");
        for (Map.Entry<String, Integer> entry : inventory.entrySet()) {
            System.out.println(entry.getKey() + " : " + entry.getValue());
        }
    }
}

// Main class
public class UC3BookMyStayApp {

    public static void main(String[] args) {

        System.out.println("===== Book My Stay App (v3.1) =====");

        // Initialize inventory
        RoomInventory inventory = new RoomInventory();

        // Display initial state
        inventory.displayInventory();

        // Simulate updates
        System.out.println("\nBooking 1 Single Room...");
        inventory.updateAvailability("Single Room", -1);

        System.out.println("Adding 1 Suite Room...");
        inventory.updateAvailability("Suite Room", +1);

        // Display updated state
        inventory.displayInventory();

        // Check availability
        System.out.println("\nAvailable Double Rooms: "
                + inventory.getAvailability("Double Room"));

        System.out.println("\nApplication finished.");
    }
}