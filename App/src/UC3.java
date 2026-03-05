
import java.util.HashMap;
import java.util.Map;

/**
 * Use Case 3: Centralized Room Inventory Management (Version 3.1)
 *
 * Compile:
 *   javac UseCase3InventorySetup.java
 *
 * Run:
 *   java UseCase3InventorySetup
 */
public class UC3 {

    // Actor: RoomInventory – responsible for managing and exposing room availability across the system.
    static class RoomInventory {
        private final Map<String, Integer> availability;

        // Key Requirement: Initialize room availability using a constructor.
        public RoomInventory() {
            this.availability = new HashMap<>();
        }

        // Flow: Room types are registered with their available counts.
        public void registerRoomType(String roomType, int initialCount) {
            validateRoomType(roomType);
            if (initialCount < 0) {
                throw new IllegalArgumentException("Initial count cannot be negative: " + initialCount);
            }
            availability.put(roomType, initialCount);
        }

        // Key Requirement: Provide methods to retrieve current availability.
        public int getAvailability(String roomType) {
            validateRoomType(roomType);
            return availability.getOrDefault(roomType, 0);
        }

        // Key Requirement: Support controlled updates to room availability.
        // This is a safe "set" method (e.g., after reconciliation).
        public void updateAvailability(String roomType, int newCount) {
            validateRoomType(roomType);
            if (newCount < 0) {
                throw new IllegalArgumentException("Availability cannot be negative: " + newCount);
            }
            availability.put(roomType, newCount);
        }

        // Optional controlled update: decrement (e.g., booking consumes inventory)
        public boolean tryReserve(String roomType, int count) {
            validateRoomType(roomType);
            if (count <= 0) {
                throw new IllegalArgumentException("Reserve count must be > 0: " + count);
            }

            int current = getAvailability(roomType);
            if (current < count) return false;

            availability.put(roomType, current - count);
            return true;
        }

        // Optional controlled update: increment (e.g., cancellation returns inventory)
        public void release(String roomType, int count) {
            validateRoomType(roomType);
            if (count <= 0) {
                throw new IllegalArgumentException("Release count must be > 0: " + count);
            }

            int current = getAvailability(roomType);
            availability.put(roomType, current + count);
        }

        // Flow: The current inventory state is displayed when requested.
        public void displayInventory() {
            System.out.println("---- Current Inventory (Single Source of Truth) ----");
            if (availability.isEmpty()) {
                System.out.println("(empty)");
                return;
            }
            for (Map.Entry<String, Integer> entry : availability.entrySet()) {
                System.out.println(entry.getKey() + " -> " + entry.getValue());
            }
        }

        private void validateRoomType(String roomType) {
            if (roomType == null || roomType.trim().isEmpty()) {
                throw new IllegalArgumentException("roomType must be non-empty");
            }
        }
    }

    public static void main(String[] args) {
        // Flow: The system initializes the inventory component.
        RoomInventory inventory = new RoomInventory();

        // Flow: Room types are registered with their available counts.
        inventory.registerRoomType("STANDARD", 10);
        inventory.registerRoomType("DELUXE", 5);
        inventory.registerRoomType("SUITE", 2);

        // Display initial state
        inventory.displayInventory();
        System.out.println();

        // Demonstrate O(1) lookups via HashMap get/put
        System.out.println("Availability of DELUXE: " + inventory.getAvailability("DELUXE"));
        System.out.println();

        // Controlled updates
        System.out.println("Trying to reserve 2 DELUXE rooms...");
        boolean reserved = inventory.tryReserve("DELUXE", 2);
        System.out.println("Reserved? " + reserved);
        inventory.displayInventory();
        System.out.println();

        System.out.println("Releasing 1 DELUXE room (cancellation)...");
        inventory.release("DELUXE", 1);
        inventory.displayInventory();
        System.out.println();

        System.out.println("Updating SUITE availability to 3 (reconciliation)...");
        inventory.updateAvailability("SUITE", 3);
        inventory.displayInventory();
    }
}