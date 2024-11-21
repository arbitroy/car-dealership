package cardealership;

public class Car {
    private int id;
    private String model;
    private String brand;
    private double price;

    public Car() {}

    public Car(String model, String brand, double price) {
        this.model = model;
        this.brand = brand;
        this.price = price;
    }

    // Getters and Setters
    public String getModel() { return model; }
    public void setModel(String model) { this.model = model; }
    public String getBrand() { return brand; }
    public void setBrand(String brand) { this.brand = brand; }
    public double getPrice() { return price; }
    public void setPrice(double price) { this.price = price; }
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    @Override
    public String toString() {
        return String.format("%s %s - $%.2f", brand, model, price);
    }
}