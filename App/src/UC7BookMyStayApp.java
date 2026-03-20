// Use Case 7 - Add-On Service Selection
// Version: 7.1

import java.util.*;

// Add-On Service class
class AddOnService {
    String serviceName;
    double cost;

    AddOnService(String serviceName, double cost) {
        this.serviceName = serviceName;
        this.cost = cost;
    }

    void display() {
        System.out.println(serviceName + " ($" + cost + ")");
    }
}

// Add-On Service Manager
class AddOnServiceManager {

    // Map: Reservation ID -> List of Services
    private HashMap<String, List<AddOnService>> serviceMap;

    AddOnServiceManager() {
        serviceMap = new HashMap<>();
    }

    // Add service to reservation
    void addService(String reservationId, AddOnService service) {
        serviceMap.putIfAbsent(reservationId, new ArrayList<>());
        serviceMap.get(reservationId).add(service);

        System.out.println("Added service '" + service.serviceName +
                "' to Reservation ID: " + reservationId);
    }

    // Display services for a reservation
    void displayServices(String reservationId) {
        System.out.println("\nServices for Reservation ID: " + reservationId);

        List<AddOnService> services = serviceMap.get(reservationId);

        if (services == null || services.isEmpty()) {
            System.out.println("No services selected.");
            return;
        }

        for (AddOnService s : services) {
            s.display();
        }
    }

    // Calculate total cost
    double calculateTotalCost(String reservationId) {
        List<AddOnService> services = serviceMap.get(reservationId);
        double total = 0;

        if (services != null) {
            for (AddOnService s : services) {
                total += s.cost;
            }
        }

        return total;
    }
}

// Main class
public class UC7BookMyStayApp {

    public static void main(String[] args) {

        System.out.println("===== Book My Stay App (v7.1) =====");

        // Sample reservation IDs (from Use Case 6)
        String res1 = "SI-1234";
        String res2 = "SU-5678";

        // Initialize service manager
        AddOnServiceManager manager = new AddOnServiceManager();

        // Add services
        manager.addService(res1, new AddOnService("Breakfast", 20));
        manager.addService(res1, new AddOnService("Airport Pickup", 40));

        manager.addService(res2, new AddOnService("Spa Access", 60));

        // Display services
        manager.displayServices(res1);
        manager.displayServices(res2);

        // Show total cost
        System.out.println("\nTotal Add-On Cost for " + res1 + ": $" +
                manager.calculateTotalCost(res1));

        System.out.println("Total Add-On Cost for " + res2 + ": $" +
                manager.calculateTotalCost(res2));

        System.out.println("\nCore booking and inventory remain unchanged.");
    }
}