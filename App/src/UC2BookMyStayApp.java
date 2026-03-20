// Use Case 2 - Book My Stay App
// Version: 2.1

// Abstract class
abstract class Room {
    String type;
    int beds;
    double price;

    // Constructor
    Room(String type, int beds, double price) {
        this.type = type;
        this.beds = beds;
        this.price = price;
    }

    // Method to display room details
    void displayDetails() {
        System.out.println("Room Type: " + type);
        System.out.println("Beds: " + beds);
        System.out.println("Price per night: $" + price);
    }
}

// Single Room
class SingleRoom extends Room {
    SingleRoom() {
        super("Single Room", 1, 100);
    }
}

// Double Room
class DoubleRoom extends Room {
    DoubleRoom() {
        super("Double Room", 2, 180);
    }
}

// Suite Room
class SuiteRoom extends Room {
    SuiteRoom() {
        super("Suite Room", 3, 300);
    }
}

// Main class
public class UC2BookMyStayApp {

    public static void main(String[] args) {

        System.out.println("===== Book My Stay App (v2.1) =====");

        // Create room objects (Polymorphism)
        Room r1 = new SingleRoom();
        Room r2 = new DoubleRoom();
        Room r3 = new SuiteRoom();

        // Static availability
        int singleAvailable = 5;
        int doubleAvailable = 3;
        int suiteAvailable = 2;

        // Display details
        System.out.println("\n--- Room Details ---");

        r1.displayDetails();
        System.out.println("Available: " + singleAvailable + "\n");

        r2.displayDetails();
        System.out.println("Available: " + doubleAvailable + "\n");

        r3.displayDetails();
        System.out.println("Available: " + suiteAvailable + "\n");

        System.out.println("Application finished.");
    }
}