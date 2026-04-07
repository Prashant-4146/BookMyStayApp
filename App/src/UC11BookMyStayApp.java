import java.util.*;

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

    public String getRoomType() {
        return roomType;
    }

    public int getBookingId() {
        return bookingId;
    }

    @Override
    public String toString() {
        return "BookingID=" + bookingId + ", Guest=" + guestName + ", Room=" + roomType;
    }
}

// Thread-safe Booking Processor
class BookingProcessor {

    private Map<String, Integer> inventory;

    public BookingProcessor(Map<String, Integer> inventory) {
        this.inventory = inventory;
    }

    // Critical section (synchronized method)
    public synchronized void processBooking(Reservation r) {

        String type = r.getRoomType();

        System.out.println(Thread.currentThread().getName() +
                " processing " + r);

        if (!inventory.containsKey(type) || inventory.get(type) <= 0) {
            System.out.println("Booking Failed for " + r.getBookingId()
                    + " (No " + type + " rooms available)");
            return;
        }

        // Simulate processing delay (to expose race conditions if unsynchronized)
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        // Safe update
        inventory.put(type, inventory.get(type) - 1);

        System.out.println("Booking Confirmed: " + r +
                " | Remaining " + type + ": " + inventory.get(type));
    }

    public void showInventory() {
        System.out.println("\nFinal Inventory:");
        for (String key : inventory.keySet()) {
            System.out.println(key + ": " + inventory.get(key));
        }
    }
}

// Worker Thread
class BookingWorker extends Thread {

    private Queue<Reservation> bookingQueue;
    private BookingProcessor processor;

    public BookingWorker(String name, Queue<Reservation> queue, BookingProcessor processor) {
        super(name);
        this.bookingQueue = queue;
        this.processor = processor;
    }

    @Override
    public void run() {
        while (true) {
            Reservation r;

            // Synchronize queue access
            synchronized (bookingQueue) {
                if (bookingQueue.isEmpty()) {
                    break;
                }
                r = bookingQueue.poll();
            }

            // Process booking (thread-safe)
            processor.processBooking(r);
        }
    }
}

// Main class
public class UseCase11ConcurrentBookingSimulation {

    public static void main(String[] args) {

        // Shared inventory
        Map<String, Integer> inventory = new HashMap<>();
        inventory.put("Standard", 2);
        inventory.put("Deluxe", 1);
        inventory.put("Suite", 1);

        // Shared booking queue
        Queue<Reservation> bookingQueue = new LinkedList<>();

        // Add multiple booking requests (simulate concurrency pressure)
        bookingQueue.add(new Reservation(1, "Alice", "Standard"));
        bookingQueue.add(new Reservation(2, "Bob", "Standard"));
        bookingQueue.add(new Reservation(3, "Charlie", "Standard")); // may fail
        bookingQueue.add(new Reservation(4, "David", "Deluxe"));
        bookingQueue.add(new Reservation(5, "Eve", "Suite"));
        bookingQueue.add(new Reservation(6, "Frank", "Suite")); // may fail

        BookingProcessor processor = new BookingProcessor(inventory);

        // Create multiple threads (simulating concurrent users)
        Thread t1 = new BookingWorker("Thread-1", bookingQueue, processor);
        Thread t2 = new BookingWorker("Thread-2", bookingQueue, processor);
        Thread t3 = new BookingWorker("Thread-3", bookingQueue, processor);

        // Start threads
        t1.start();
        t2.start();
        t3.start();

        // Wait for all threads to finish
        try {
            t1.join();
            t2.join();
            t3.join();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        // Show final inventory
        processor.showInventory();
    }
}
