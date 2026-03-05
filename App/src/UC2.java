/**
 * UseCase2RoomInitialization
 *
 * Demonstrates object-oriented modeling of hotel rooms
 * using abstraction, inheritance, and polymorphism.
 *
 * @author Student
 * @version 2.1
 */

/* Abstract Room Class */
abstract class Room {

    private String roomType;
    private int beds;
    private double price;

    public Room(String roomType, int beds, double price) {
        this.roomType = roomType;
        this.beds = beds;
        this.price = price;
    }

    public void displayRoomDetails() {
        System.out.println("Room Type : " + roomType);
        System.out.println("Beds      : " + beds);
        System.out.println("Price     : $" + price);
    }
}

/* Single Room Class */
class SingleRoom extends Room {

    public SingleRoom() {
        super("Single Room", 1, 1000.0);
    }
}

/* Double Room Class */
class DoubleRoom extends Room {

    public DoubleRoom() {
        super("Double Room", 2, 1800.0);
    }
}

/* Suite Room Class */
class SuiteRoom extends Room {

    public SuiteRoom() {
        super("Suite Room", 3, 3500.0);
    }
}

/* Application Entry Point */
public class UC2 {

    public static void main(String[] args) {

        System.out.println("=================================");
        System.out.println("     Book My Stay - Version 2.1");
        System.out.println("=================================\n");

        // Creating room objects (Polymorphism)
        Room singleRoom = new SingleRoom();
        Room doubleRoom = new DoubleRoom();
        Room suiteRoom = new SuiteRoom();

        // Static availability variables
        int singleAvailability = 10;
        int doubleAvailability = 5;
        int suiteAvailability = 2;

        // Display Single Room
        singleRoom.displayRoomDetails();
        System.out.println("Available : " + singleAvailability);
        System.out.println("---------------------------------\n");

        // Display Double Room
        doubleRoom.displayRoomDetails();
        System.out.println("Available : " + doubleAvailability);
        System.out.println("---------------------------------\n");

        // Display Suite Room
        suiteRoom.displayRoomDetails();
        System.out.println("Available : " + suiteAvailability);
        System.out.println("---------------------------------\n");

        System.out.println("Room initialization completed.");
    }
}