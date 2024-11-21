package cardealership;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class DataManager {
    private static final String DATA_DIRECTORY = "data";
    private static final String CARS_FILE = DATA_DIRECTORY + "/cars.txt";
    private static final String CUSTOMERS_FILE = DATA_DIRECTORY + "/customers.txt";
    private static final String DEALERSHIP_FILE = DATA_DIRECTORY + "/dealership.txt";
    private static final String SALES_FILE = DATA_DIRECTORY + "/sales.txt";

    public static void initializeDataDirectory() {
        File directory = new File(DATA_DIRECTORY);
        if (!directory.exists()) {
            directory.mkdir();
        }
    }

    // Dealership methods
    public static void saveDealership(CarDealership dealership) {
        try (PrintWriter writer = new PrintWriter(new FileWriter(DEALERSHIP_FILE))) {
            writer.println(dealership.getStoreName());
            writer.println(dealership.getStoreLocation());
        } catch (IOException e) {
            System.err.println("Error saving dealership: " + e.getMessage());
        }
    }

    public static CarDealership loadDealership() {
        File file = new File(DEALERSHIP_FILE);
        if (!file.exists()) {
            return null;
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(DEALERSHIP_FILE))) {
            String name = reader.readLine();
            String location = reader.readLine();
            if (name != null && location != null) {
                return new CarDealership(name, location);
            }
        } catch (IOException e) {
            System.err.println("Error loading dealership: " + e.getMessage());
        }
        return null;
    }

    // Cars methods
    public static void saveCars(List<Car> cars) {
        try (PrintWriter writer = new PrintWriter(new FileWriter(CARS_FILE))) {
            for (Car car : cars) {
                writer.println(String.format("%s,%s,%.2f", 
                    car.getModel(), 
                    car.getBrand(), 
                    car.getPrice()));
            }
        } catch (IOException e) {
            System.err.println("Error saving cars: " + e.getMessage());
        }
    }

    public static List<Car> loadCars() {
        List<Car> cars = new ArrayList<>();
        File file = new File(CARS_FILE);
        if (!file.exists()) {
            return cars;
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(CARS_FILE))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length == 3) {
                    cars.add(new Car(
                        parts[0],  // model
                        parts[1],  // brand
                        Double.parseDouble(parts[2]) // price
                    ));
                }
            }
        } catch (IOException e) {
            System.err.println("Error loading cars: " + e.getMessage());
        }
        return cars;
    }

    // Customers methods
    public static void saveCustomers(List<Customer> customers) {
        try (PrintWriter writer = new PrintWriter(new FileWriter(CUSTOMERS_FILE))) {
            for (Customer customer : customers) {
                writer.println(String.format("%s,%d,%s,%s,%s",
                    customer.getName(),
                    customer.getAge(),
                    customer.getAddress(),
                    customer.getPhone(),
                    customer.getEmail()));
            }
        } catch (IOException e) {
            System.err.println("Error saving customers: " + e.getMessage());
        }
    }

    public static List<Customer> loadCustomers() {
        List<Customer> customers = new ArrayList<>();
        File file = new File(CUSTOMERS_FILE);
        if (!file.exists()) {
            return customers;
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(CUSTOMERS_FILE))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length == 5) {
                    customers.add(new Customer(
                        parts[0], // name
                        Integer.parseInt(parts[1]), // age
                        parts[2], // address
                        parts[3], // phone
                        parts[4]  // email
                    ));
                }
            }
        } catch (IOException e) {
            System.err.println("Error loading customers: " + e.getMessage());
        }
        return customers;
    }

    // Sales methods
    public static void saveSale(Car car, Customer customer, double basePrice, 
                              double tax, double discount, double finalPrice) {
        try (PrintWriter writer = new PrintWriter(new FileWriter(SALES_FILE, true))) {
            writer.println(String.format("%s,%s,%s,%s,%.2f,%.2f,%.2f,%.2f",
                car.getBrand(),
                car.getModel(),
                customer.getName(),
                customer.getEmail(),
                basePrice,
                tax,
                discount,
                finalPrice));
        } catch (IOException e) {
            System.err.println("Error saving sale: " + e.getMessage());
        }
    }

    public static void saveVideoTestimony(Customer customer, String filePath, String comments) {
        String testimoniesFile = DATA_DIRECTORY + "/testimonies.txt";
        try (PrintWriter writer = new PrintWriter(new FileWriter(testimoniesFile, true))) {
            writer.println(String.format("%s,%s,%s,%s",
                customer.getName(),
                customer.getEmail(),
                filePath,
                comments.replace(",", ";")));  // Replace commas in comments to avoid CSV issues
        } catch (IOException e) {
            System.err.println("Error saving video testimony: " + e.getMessage());
        }
    }
}