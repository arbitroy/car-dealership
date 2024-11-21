package cardealership;

import javafx.application.Application;
import javafx.beans.property.SimpleStringProperty;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
import javafx.scene.paint.Color;
import javafx.collections.FXCollections;
import java.util.List;
import java.util.Optional;
import javafx.stage.FileChooser;
import java.io.File;

public class Main extends Application {
    private CarDealership dealership;
    private List<Customer> customers;
    private static final double TAX_RATE = 0.07;
    private BorderPane mainLayout;
    private VBox contentArea;

    @Override
    public void start(Stage primaryStage) {
        DataManager.initializeDataDirectory();
        dealership = DataManager.loadDealership();
        if (dealership == null) {
            dealership = new CarDealership();
        } else {
            dealership.getCars().addAll(DataManager.loadCars());
        }
        customers = DataManager.loadCustomers();

        // Create main layout
        mainLayout = new BorderPane();
        mainLayout.setStyle("-fx-background-color: #f0f0f0;");

        // Create header
        VBox header = createHeader();
        mainLayout.setTop(header);

        // Create sidebar menu
        VBox sideMenu = createSideMenu();
        mainLayout.setLeft(sideMenu);

        // Create content area
        contentArea = new VBox(20);
        contentArea.setPadding(new Insets(20));
        contentArea.setStyle("-fx-background-color: white; -fx-background-radius: 5;");
        mainLayout.setCenter(contentArea);

        // Welcome screen
        showWelcomeScreen();

        Scene scene = new Scene(mainLayout, 1024, 768);
        primaryStage.setTitle("Car Dealership Management System");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private VBox createHeader() {
        VBox header = new VBox(10);
        header.setStyle("-fx-background-color: #2c3e50; -fx-padding: 20;");

        Label title = new Label("Car Dealership Management System");
        title.setFont(Font.font("Arial", FontWeight.BOLD, 24));
        title.setTextFill(Color.WHITE);

        header.getChildren().add(title);
        return header;
    }

    private VBox createSideMenu() {
        VBox sideMenu = new VBox(5);
        sideMenu.setPrefWidth(200);
        sideMenu.setStyle("-fx-background-color: #34495e; -fx-padding: 10;");

        String buttonStyle = "-fx-background-color: transparent; -fx-text-fill: white; " +
                "-fx-min-width: 180; -fx-alignment: CENTER-LEFT; -fx-padding: 10;";
        String buttonHoverStyle = "-fx-background-color: #2c3e50;";

        Button[] menuButtons = {
                createMenuButton("Create Store", this::createStore),
                createMenuButton("View Inventory", this::viewInventory),
                createMenuButton("View Customers", this::viewCustomers),
                createMenuButton("Add Customer", this::addCustomer),
                createMenuButton("Upload Inventory", this::uploadInventory),
                createMenuButton("Purchase Items", this::purchaseItems),
                createMenuButton("Upload Video Testimony", this::uploadVideoTestimony)
        };

        for (Button btn : menuButtons) {
            btn.setStyle(buttonStyle);
            btn.setOnMouseEntered(e -> btn.setStyle(buttonStyle + buttonHoverStyle));
            btn.setOnMouseExited(e -> btn.setStyle(buttonStyle));
            btn.setMaxWidth(Double.MAX_VALUE);
        }

        sideMenu.getChildren().addAll(menuButtons);
        return sideMenu;
    }

    private Button createMenuButton(String text, Runnable action) {
        Button button = new Button(text);
        button.setOnAction(e -> action.run());
        return button;
    }

    private void showWelcomeScreen() {
        contentArea.getChildren().clear();

        Label welcomeLabel = new Label("Welcome to Car Dealership Management System");
        welcomeLabel.setFont(Font.font("Arial", FontWeight.BOLD, 20));

        Label instructionLabel = new Label("Please select an option from the menu to get started");
        instructionLabel.setFont(Font.font("Arial", 14));

        contentArea.getChildren().addAll(welcomeLabel, instructionLabel);
        contentArea.setAlignment(Pos.CENTER);
    }

    private void createStore() {
        contentArea.getChildren().clear();

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20));

        Label headerLabel = new Label("Create New Store");
        headerLabel.setFont(Font.font("Arial", FontWeight.BOLD, 18));

        TextField nameField = new TextField();
        nameField.setPromptText("Store Name");
        TextField locationField = new TextField();
        locationField.setPromptText("Store Location");

        Button submitBtn = new Button("Create Store");
        submitBtn.setStyle("-fx-background-color: #2ecc71; -fx-text-fill: white;");

        submitBtn.setOnAction(e -> {
            if (nameField.getText().isEmpty() || locationField.getText().isEmpty()) {
                showAlert("Error", "Please fill in all fields!", Alert.AlertType.ERROR);
                return;
            }

            dealership = new CarDealership(nameField.getText(), locationField.getText());
            DataManager.saveDealership(dealership);
            showAlert("Success", "Store created successfully!", Alert.AlertType.INFORMATION);
            showWelcomeScreen();
        });

        grid.add(headerLabel, 0, 0, 2, 1);
        grid.add(new Label("Store Name:"), 0, 1);
        grid.add(nameField, 1, 1);
        grid.add(new Label("Location:"), 0, 2);
        grid.add(locationField, 1, 2);
        grid.add(submitBtn, 1, 3);

        contentArea.getChildren().add(grid);
    }

    private void viewInventory() {
        contentArea.getChildren().clear();

        Label headerLabel = new Label("Current Inventory");
        headerLabel.setFont(Font.font("Arial", FontWeight.BOLD, 18));

        TableView<Car> tableView = new TableView<>();

        TableColumn<Car, String> brandCol = new TableColumn<>("Brand");
        brandCol.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getBrand()));

        TableColumn<Car, String> modelCol = new TableColumn<>("Model");
        modelCol.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getModel()));

        TableColumn<Car, String> priceCol = new TableColumn<>("Price");
        priceCol.setCellValueFactory(
                cellData -> new SimpleStringProperty(String.format("$%.2f", cellData.getValue().getPrice())));

        tableView.getColumns().addAll(brandCol, modelCol, priceCol);
        tableView.setItems(FXCollections.observableArrayList(dealership.getCars()));

        VBox.setVgrow(tableView, Priority.ALWAYS);
        contentArea.getChildren().addAll(headerLabel, tableView);
    }

    private void addCustomer() {
        contentArea.getChildren().clear();

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20));

        Label headerLabel = new Label("Add New Customer");
        headerLabel.setFont(Font.font("Arial", FontWeight.BOLD, 18));

        TextField nameField = new TextField();
        nameField.setPromptText("Full Name");
        TextField ageField = new TextField();
        ageField.setPromptText("Age");
        TextField addressField = new TextField();
        addressField.setPromptText("Address");
        TextField phoneField = new TextField();
        phoneField.setPromptText("Phone Number");
        TextField emailField = new TextField();
        emailField.setPromptText("Email Address");

        Button submitBtn = new Button("Add Customer");
        submitBtn.setStyle("-fx-background-color: #2ecc71; -fx-text-fill: white;");

        submitBtn.setOnAction(e -> {
            try {
                validateCustomerInput(nameField.getText(), ageField.getText(),
                        addressField.getText(), phoneField.getText(), emailField.getText());

                Customer customer = new Customer(
                        nameField.getText(),
                        Integer.parseInt(ageField.getText()),
                        addressField.getText(),
                        phoneField.getText(),
                        emailField.getText());
                customers.add(customer);
                DataManager.saveCustomers(customers);
                showAlert("Success", "Customer added successfully!", Alert.AlertType.INFORMATION);
                clearFields(nameField, ageField, addressField, phoneField, emailField);
            } catch (IllegalArgumentException ex) {
                showAlert("Error", ex.getMessage(), Alert.AlertType.ERROR);
            }
        });

        grid.add(headerLabel, 0, 0, 2, 1);
        grid.add(new Label("Name:"), 0, 1);
        grid.add(nameField, 1, 1);
        grid.add(new Label("Age:"), 0, 2);
        grid.add(ageField, 1, 2);
        grid.add(new Label("Address:"), 0, 3);
        grid.add(addressField, 1, 3);
        grid.add(new Label("Phone:"), 0, 4);
        grid.add(phoneField, 1, 4);
        grid.add(new Label("Email:"), 0, 5);
        grid.add(emailField, 1, 5);
        grid.add(submitBtn, 1, 6);

        contentArea.getChildren().add(grid);
    }

    private void uploadInventory() {
        contentArea.getChildren().clear();

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20));

        Label headerLabel = new Label("Add New Car to Inventory");
        headerLabel.setFont(Font.font("Arial", FontWeight.BOLD, 18));

        TextField brandField = new TextField();
        brandField.setPromptText("Car Brand");
        TextField modelField = new TextField();
        modelField.setPromptText("Car Model");
        TextField priceField = new TextField();
        priceField.setPromptText("Price");

        Button submitBtn = new Button("Add to Inventory");
        submitBtn.setStyle("-fx-background-color: #2ecc71; -fx-text-fill: white;");

        submitBtn.setOnAction(e -> {
            try {
                validateCarInput(brandField.getText(), modelField.getText(), priceField.getText());

                Car car = new Car(
                        modelField.getText(),
                        brandField.getText(),
                        Double.parseDouble(priceField.getText()));
                dealership.addCar(car);
                DataManager.saveCars(dealership.getCars());
                showAlert("Success", "Car added to inventory!", Alert.AlertType.INFORMATION);
                clearFields(brandField, modelField, priceField);
            } catch (IllegalArgumentException ex) {
                showAlert("Error", ex.getMessage(), Alert.AlertType.ERROR);
            }
        });

        grid.add(headerLabel, 0, 0, 2, 1);
        grid.add(new Label("Brand:"), 0, 1);
        grid.add(brandField, 1, 1);
        grid.add(new Label("Model:"), 0, 2);
        grid.add(modelField, 1, 2);
        grid.add(new Label("Price:"), 0, 3);
        grid.add(priceField, 1, 3);
        grid.add(submitBtn, 1, 4);

        contentArea.getChildren().add(grid);
    }

    private double calculateDiscountedTotal(double total) {
        if (total >= 100000) {
            return total * 0.8; // 20% discount
        } else if (total >= 50000) {
            return total * 0.9; // 10% discount
        } else if (total >= 25000) {
            return total * 0.95; // 5% discount
        }
        return total;
    }

    private void updateAvailableCars(ComboBox<Car> carComboBox, ListView<Car> cartListView) {
        List<Car> availableCars = dealership.getCars().stream()
                .filter(car -> !cartListView.getItems().contains(car))
                .toList();
        carComboBox.setItems(FXCollections.observableArrayList(availableCars));
    }

    private void updateCartSummary(ListView<Car> cartListView, ComboBox<Customer> customerComboBox,
            TextArea summaryArea, Label cartLabel) {
        if (cartListView.getItems().isEmpty() || customerComboBox.getValue() == null) {
            summaryArea.clear();
            return;
        }

        double totalBasePrice = 0;
        StringBuilder summary = new StringBuilder();
        summary.append(String.format("Customer: %s\n\nSelected Cars:\n",
                customerComboBox.getValue().getName()));

        for (Car car : cartListView.getItems()) {
            summary.append(String.format("%s %s - $%.2f\n",
                    car.getBrand(), car.getModel(), car.getPrice()));
            totalBasePrice += car.getPrice();
        }

        double tax = totalBasePrice * TAX_RATE;
        double subtotal = totalBasePrice + tax;
        double finalTotal = calculateDiscountedTotal(subtotal);
        double discount = subtotal - finalTotal;

        summary.append(String.format("""

                Base Total: $%.2f
                Sales Tax (7%%): $%.2f
                Subtotal: $%.2f
                Discount: $%.2f
                Final Total: $%.2f
                """,
                totalBasePrice, tax, subtotal, discount, finalTotal));

        summaryArea.setText(summary.toString());
        cartLabel.setText(String.format("Shopping Cart (%d items)", cartListView.getItems().size()));
    }

    private void purchaseItems() {
        if (dealership.getCars().isEmpty()) {
            showAlert("Error", "No cars available in inventory!", Alert.AlertType.ERROR);
            return;
        }

        if (customers.isEmpty()) {
            showAlert("Error", "No customers available. Please add a customer first!", Alert.AlertType.ERROR);
            return;
        }

        contentArea.getChildren().clear();
        
        VBox purchaseBox = new VBox(15);
        purchaseBox.setPadding(new Insets(20));

        Label headerLabel = new Label("Shopping Cart");
        headerLabel.setFont(Font.font("Arial", FontWeight.BOLD, 18));

        // Customer selection
        ComboBox<Customer> customerComboBox = new ComboBox<>(FXCollections.observableArrayList(customers));
        customerComboBox.setPromptText("Select Customer");

        // Shopping cart list view
        ListView<Car> cartListView = new ListView<>();
        VBox.setVgrow(cartListView, Priority.ALWAYS);
        Label cartLabel = new Label("Shopping Cart (0 items)");
        
        // Car selection
        ComboBox<Car> carComboBox = new ComboBox<>();
        carComboBox.setPromptText("Select a Car");
        
        // Update available cars
        updateAvailableCars(carComboBox, cartListView);

        Button addToCartBtn = new Button("Add to Cart");
        addToCartBtn.setStyle("-fx-background-color: #3498db; -fx-text-fill: white;");

        TextArea summaryArea = new TextArea();
        summaryArea.setEditable(false);
        summaryArea.setPrefRowCount(6);

        // Update cart summary
        updateCartSummary(cartListView, customerComboBox, summaryArea, cartLabel);
        // Add to cart button action
        addToCartBtn.setOnAction(e -> {
            Car selectedCar = carComboBox.getValue();
            if (selectedCar == null) {
                showAlert("Error", "Please select a car to add!", Alert.AlertType.ERROR);
                return;
            }
            cartListView.getItems().add(selectedCar);
            updateAvailableCars(carComboBox, cartListView);
            updateCartSummary(cartListView, customerComboBox, summaryArea, cartLabel);;
        });

        // Remove from cart functionality
        Button removeFromCartBtn = new Button("Remove Selected");
        removeFromCartBtn.setStyle("-fx-background-color: #e74c3c; -fx-text-fill: white;");
        removeFromCartBtn.setOnAction(e -> {
            Car selectedCar = cartListView.getSelectionModel().getSelectedItem();
            if (selectedCar != null) {
                cartListView.getItems().remove(selectedCar);
                updateAvailableCars(carComboBox, cartListView);
                updateCartSummary(cartListView, customerComboBox, summaryArea, cartLabel);;
            }
        });

        customerComboBox.setOnAction(e -> updateCartSummary(cartListView, customerComboBox, summaryArea, cartLabel));

        Button checkoutBtn = new Button("Checkout");
        checkoutBtn.setStyle("-fx-background-color: #2ecc71; -fx-text-fill: white;");
        
        checkoutBtn.setOnAction(e -> {
            if (cartListView.getItems().isEmpty()) {
                showAlert("Error", "Cart is empty!", Alert.AlertType.ERROR);
                return;
            }
            
            if (customerComboBox.getValue() == null) {
                showAlert("Error", "Please select a customer!", Alert.AlertType.ERROR);
                return;
            }

            Optional<ButtonType> result = showConfirmationDialog(
                "Confirm Purchase",
                "Are you sure you want to complete this purchase?"
            );

            if (result.isPresent() && result.get() == ButtonType.OK) {
                Customer selectedCustomer = customerComboBox.getValue();
                double totalBasePrice = 0;
                
                for (Car car : cartListView.getItems()) {
                    totalBasePrice += car.getPrice();
                }
                
                double tax = totalBasePrice * TAX_RATE;
                double subtotal = totalBasePrice + tax;
                double finalTotal = calculateDiscountedTotal(subtotal);
                double discount = subtotal - finalTotal;

                // Save each car purchase
                for (Car car : cartListView.getItems()) {
                    // Calculate individual car's proportion of total
                    double proportion = car.getPrice() / totalBasePrice;
                    DataManager.saveSale(car, selectedCustomer,
                        car.getPrice(),
                        tax * proportion,
                        discount * proportion,
                        finalTotal * proportion);
                    dealership.getCars().remove(car);
                }
                
                DataManager.saveCars(dealership.getCars());
                showAlert("Success", "Purchase completed successfully!", Alert.AlertType.INFORMATION);
                showWelcomeScreen();
            }
        });

        // Layout
        HBox cartControls = new HBox(10);
        cartControls.getChildren().addAll(removeFromCartBtn);

        // Create selection area
        VBox selectionArea = new VBox(10);
        selectionArea.getChildren().addAll(
            new Label("Select Car to Add:"),
            carComboBox,
            addToCartBtn
        );

        // Create cart area
        VBox cartArea = new VBox(10);
        cartArea.getChildren().addAll(
            cartLabel,
            cartListView,
            cartControls
        );

        // Main layout
        purchaseBox.getChildren().addAll(
            headerLabel,
            new Label("Select Customer:"),
            customerComboBox,
            selectionArea,
            cartArea,
            new Label("Order Summary:"),
            summaryArea,
            checkoutBtn
        );

        contentArea.getChildren().add(purchaseBox);
    }

    private void uploadVideoTestimony() {
        contentArea.getChildren().clear();

        VBox videoBox = new VBox(15);
        videoBox.setPadding(new Insets(20));

        Label headerLabel = new Label("Upload Video Testimony");
        headerLabel.setFont(Font.font("Arial", FontWeight.BOLD, 18));

        ComboBox<Customer> customerSelect = new ComboBox<>(FXCollections.observableArrayList(customers));
        customerSelect.setPromptText("Select Customer");
        customerSelect.setMaxWidth(Double.MAX_VALUE);

        Button selectFileBtn = new Button("Select Video File");
        selectFileBtn.setStyle("-fx-background-color: #3498db; -fx-text-fill: white;");

        Label fileNameLabel = new Label("No file selected");
        fileNameLabel.setStyle("-fx-font-style: italic;");

        // Path will be stored here
        final String[] selectedFilePath = { null };

        TextArea commentArea = new TextArea();
        commentArea.setPromptText("Add comments about your experience");
        commentArea.setPrefRowCount(4);

        Button uploadBtn = new Button("Upload Testimony");
        uploadBtn.setStyle("-fx-background-color: #2ecc71; -fx-text-fill: white;");

        selectFileBtn.setOnAction(e -> {
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Select Video File");

            // Set file extension filters
            FileChooser.ExtensionFilter videoFilter = new FileChooser.ExtensionFilter("Video Files", "*.mp4", "*.avi",
                    "*.mov", "*.wmv");
            fileChooser.getExtensionFilters().add(videoFilter);

            // Show file chooser dialog
            File selectedFile = fileChooser.showOpenDialog(contentArea.getScene().getWindow());

            if (selectedFile != null) {
                selectedFilePath[0] = selectedFile.getAbsolutePath();
                fileNameLabel.setText(selectedFile.getName());
            }
        });

        uploadBtn.setOnAction(e -> {
            if (customerSelect.getValue() == null) {
                showAlert("Error", "Please select a customer!", Alert.AlertType.ERROR);
                return;
            }
            if (selectedFilePath[0] == null) {
                showAlert("Error", "Please select a video file!", Alert.AlertType.ERROR);
                return;
            }

            DataManager.saveVideoTestimony(customerSelect.getValue(),
                    selectedFilePath[0], commentArea.getText());
            showAlert("Success", "Video testimony uploaded successfully!", Alert.AlertType.INFORMATION);
            showWelcomeScreen();
        });

        videoBox.getChildren().addAll(
                headerLabel,
                new Label("Customer:"),
                customerSelect,
                selectFileBtn,
                fileNameLabel,
                new Label("Comments:"),
                commentArea,
                uploadBtn);

        contentArea.getChildren().add(videoBox);
    }

    private void viewCustomers() {
        contentArea.getChildren().clear();

        VBox customerBox = new VBox(15);
        customerBox.setPadding(new Insets(20));

        Label headerLabel = new Label("Customer Database");
        headerLabel.setFont(Font.font("Arial", FontWeight.BOLD, 18));

        TableView<Customer> tableView = new TableView<>();

        TableColumn<Customer, String> nameCol = new TableColumn<>("Name");
        nameCol.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getName()));

        TableColumn<Customer, String> ageCol = new TableColumn<>("Age");
        ageCol.setCellValueFactory(cellData -> new SimpleStringProperty(String.valueOf(cellData.getValue().getAge())));

        TableColumn<Customer, String> phoneCol = new TableColumn<>("Phone");
        phoneCol.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getPhone()));

        TableColumn<Customer, String> emailCol = new TableColumn<>("Email");
        emailCol.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getEmail()));

        TableColumn<Customer, String> addressCol = new TableColumn<>("Address");
        addressCol.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getAddress()));

        tableView.getColumns().addAll(nameCol, ageCol, phoneCol, emailCol, addressCol);
        tableView.setItems(FXCollections.observableArrayList(customers));

        // Add search functionality
        TextField searchField = new TextField();
        searchField.setPromptText("Search customers...");
        searchField.setMaxWidth(300);

        searchField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue == null || newValue.isEmpty()) {
                tableView.setItems(FXCollections.observableArrayList(customers));
            } else {
                List<Customer> filteredList = customers.stream()
                        .filter(customer -> customer.getName().toLowerCase().contains(newValue.toLowerCase()) ||
                                customer.getEmail().toLowerCase().contains(newValue.toLowerCase()) ||
                                customer.getPhone().contains(newValue))
                        .toList();
                tableView.setItems(FXCollections.observableArrayList(filteredList));
            }
        });

        VBox.setVgrow(tableView, Priority.ALWAYS);
        customerBox.getChildren().addAll(headerLabel, searchField, tableView);
        contentArea.getChildren().add(customerBox);
    }

    private void validateCustomerInput(String name, String age, String address, String phone, String email) {
        if (name.isEmpty() || age.isEmpty() || address.isEmpty() || phone.isEmpty() || email.isEmpty()) {
            throw new IllegalArgumentException("All fields are required!");
        }

        try {
            int ageNum = Integer.parseInt(age);
            if (ageNum < 18 || ageNum > 120) {
                throw new IllegalArgumentException("Age must be between 18 and 120!");
            }
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Age must be a valid number!");
        }

        if (!email.matches("^[A-Za-z0-9+_.-]+@(.+)$")) {
            throw new IllegalArgumentException("Invalid email format!");
        }

        if (!phone.matches("^[0-9()-]{10,}$")) {
            throw new IllegalArgumentException("Invalid phone number format!");
        }
    }

    private void validateCarInput(String brand, String model, String price) {
        if (brand.isEmpty() || model.isEmpty() || price.isEmpty()) {
            throw new IllegalArgumentException("All fields are required!");
        }

        try {
            double priceNum = Double.parseDouble(price);
            if (priceNum <= 0) {
                throw new IllegalArgumentException("Price must be greater than zero!");
            }
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Price must be a valid number!");
        }
    }

    private void clearFields(TextField... fields) {
        for (TextField field : fields) {
            field.clear();
        }
    }

    private void showAlert(String title, String content, Alert.AlertType type) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }

    private Optional<ButtonType> showConfirmationDialog(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        return alert.showAndWait();
    }

    private String getDiscountDescription(double total) {
        if (total >= 100000)
            return "20% discount applied";
        if (total >= 50000)
            return "10% discount applied";
        if (total >= 25000)
            return "5% discount applied";
        return "No discount applied";
    }

    public static void main(String[] args) {
        launch(args);
    }
}