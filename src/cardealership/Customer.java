package cardealership;

public class Customer {
    private int id;
    private String name;
    private int age;
    private String address;
    private String phone;
    private String email;

    public Customer() {}

    public Customer(String name, int age, String address, String phone, String email) {
        this.name = name;
        this.age = age;
        this.address = address;
        this.phone = phone;
        this.email = email;
    }

    // Getters and Setters
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public int getAge() { return age; }
    public void setAge(int age) { this.age = age; }
    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }
    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    @Override
    public String toString() {
        return String.format("Name: %s\nAge: %d\nAddress: %s\nPhone: %s\nEmail: %s",
            name, age, address, phone, email);
    }
}