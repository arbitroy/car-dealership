package cardealership;

import java.util.ArrayList;
import java.util.List;

public abstract class Store {
    private String storeType;
    private String storeName;
    private String storeLocation;
    private List<Object> inventory;

    public Store() {
        this.inventory = new ArrayList<>();
    }

    public Store(String type, String name, String location) {
        this.storeType = type;
        this.storeName = name;
        this.storeLocation = location;
        this.inventory = new ArrayList<>();
    }

    // Getters and Setters
    public String getStoreType() { return storeType; }
    public void setStoreType(String type) { this.storeType = type; }
    public String getStoreName() { return storeName; }
    public void setStoreName(String name) { this.storeName = name; }
    public String getStoreLocation() { return storeLocation; }
    public void setStoreLocation(String location) { this.storeLocation = location; }
    
    public List<Object> getInventory() { return inventory; }
    public void addToInventory(Object item) { inventory.add(item); }
    
    public abstract void displayInventory();

    @Override
    public String toString() {
        return String.format("Store Type: %s\nStore Name: %s\nLocation: %s", 
            storeType, storeName, storeLocation);
    }
}