// Use Case 4 - Room Search & Availability Check
// Version: 4.1

import java.util.HashMap;
import java.util.Map;

// Abstract Room class
abstract class Room {
    String type;
    double price;

    Room(String type, double price) {
        this.type = type;
        this.price = price;
    }

    void displayDetails() {
        System.out.println("Room Type: " + type);
        System.out.println("Price: $" + price);
    }
}

// Room Types
class SingleRoom extends Room {
    SingleRoom() {
        super("Single Room", 100);
    }
}

class DoubleRoom extends Room {
    DoubleRoom() {
        super("Double Room", 180);
    }
}

class SuiteRoom extends Room {
    SuiteRoom() {
        super("Suite Room", 300);
    }
}

// Inventory (Read-only access during search)
class RoomInventory {
    private HashMap<String, Integer> inventory;

    RoomInventory() {
        inventory = new HashMap<>();
        inventory.put("Single Room", 2);
        inventory.put("Double Room", 0); // unavailable
        inventory.put("Suite Room", 1);
    }

    int getAvailability(String roomType) {
        return inventory.getOrDefault(roomType, 0);
    }

    Map<String, Integer> getAllInventory() {
        return inventory; // read-only usage
    }
}

// Search Service
class RoomSearchService {

    void searchAvailableRooms(RoomInventory inventory) {

        System.out.println("\n--- Available Rooms ---");

        for (Map.Entry<String, Integer> entry : inventory.getAllInventory().entrySet()) {

            String type = entry.getKey();
            int available = entry.getValue();

            // Show only available rooms
            if (available > 0) {

                Room room;

                // Polymorphic object creation
                if (type.equals("Single Room")) {
                    room = new SingleRoom();
                } else if (type.equals("Double Room")) {
                    room = new DoubleRoom();
                } else {
                    room = new SuiteRoom();
                }

                room.displayDetails();
                System.out.println("Available: " + available + "\n");
            }
        }
    }
}

// Main class
public class UC4BookMyStayApp {

    public static void main(String[] args) {

        System.out.println("===== Book My Stay App (v4.1) =====");

        // Initialize inventory
        RoomInventory inventory = new RoomInventory();

        // Search (read-only operation)
        RoomSearchService searchService = new RoomSearchService();
        searchService.searchAvailableRooms(inventory);

        System.out.println("Search completed. No data was modified.");
    }
}