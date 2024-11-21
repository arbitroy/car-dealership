package cardealership;

import java.util.ArrayList;
import java.util.List;

public class CarDealership extends Store {
    private int id;
    private List<Car> cars;

    public CarDealership() {
        super("Car Dealership", "Default Dealership", "Default Location");
        cars = new ArrayList<>();
    }

    public CarDealership(String name, String location) {
        super("Car Dealership", name, location);
        cars = new ArrayList<>();
    }


    public void addCar(Car car) {
        cars.add(car);
        addToInventory(car);
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    @Override
    public void displayInventory() {
        System.out.println("Current Car Inventory:");
        for (Car car : cars) {
            System.out.println(car);
        }
    }

    public List<Car> getCars() {
        return cars;
    }
}