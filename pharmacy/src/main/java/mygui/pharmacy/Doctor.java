/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package mygui.pharmacy;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.util.*;

/**
 *
 * @author Mohamed
 */

public class Doctor {
    private String name;
    private String doctorId;
    public ArrayList<Order> currentOrders;
    private Inventory inventory;

    // constructor
    public Doctor(String name, String doctorId, ArrayList<Order> currentOrders, Inventory inventory) {
        this.name = name;
        this.doctorId = doctorId;
        this.currentOrders = currentOrders;
        this.inventory = inventory;
    }

    // method to get Doctor's Name
    public String getName() {
        return name;
    }

    // method to get Doctor's ID
    public String getDoctorId() {
        return doctorId;
    }

    // Method to Create a new order for a customer
    public Order createNewOrder(Customer customer, ManageCustomerDiscount dis) {
        Order newOrder = new Order(this, customer, dis);
        currentOrders.add(newOrder);
        System.out.println("Created a new order for " + customer.getname());
        return newOrder;
    }

    // Method to Add a product to an order by checking the inventory first
    public boolean addProductToOrder(Order order, String productId, int quantity) {
        if (inventory == null) {
            System.out.println("Inventory is not initialized.");
            return false ;
        }
        Product product = inventory.getProduct(productId);

        if (product == null) {
            System.out.println("Product not found in inventory.");
            return false ;
        }

        if (inventory.isProductAvailable(productId, quantity)) {
            order.addProductToOrder(product, quantity);
            inventory.updateQuantity(product, quantity, false);
            System.out.println("Added " + quantity + " units of " + product.getName() + " to the order.");
            return true;
        } else {
            System.out.println("Insufficient stock for product: " + product.getName());
            System.out.println("failled to Add this product " + product.getName());
            return false;
        }
    }

    // Method Finalize an order
    public void finalizeOrder(Order order) {
        if (order.getOrderedProducts().isEmpty()) {
            System.out.println("No products in the order to finalize.");
        } else {
            order.finalizeOrder();
        }
    }

    // Method Cancel an order
    public void cancelOrder(Order order) {
        if (currentOrders.contains(order)) {
            order.cancelOrder(inventory);
            currentOrders.remove(order); // Remove the order from the list
        } else {
            System.out.println("Order not found in current orders.");
        }
    }

    // Method View inventory products
    public void displayOrders() {
        System.out.println("\nNumber of Complated Orders : "+currentOrders.size());
        for (var order : currentOrders) {
            order.displayOrderDetailsforHistory();
        }
    }

    // Method to Add a new product to the inventory
    public void addProductToInventory(Product product) {
        if (inventory == null) {
            System.out.println("Inventory is not initialized.");
            return;
        }
        inventory.addProduct(product);
        System.out.println("Added product " + product.getName() + " to the inventory.");
    }

    // Update quantity of an existing product in the inventory
    public boolean updateProductQuantity(String productId, int quantity, boolean add) {
        if (inventory == null) {
            System.out.println("Inventory is not initialized.");
            return false;
        }
        Product product = inventory.getProduct(productId);
        if (product != null) {
            boolean done = inventory.updateQuantity(product, quantity, add);
            String actionStr = (add) ? "Increased" : "Decreased";
            if (!done)
                 return false;
            System.out.println(actionStr + " quantity of " + product.getName() + " by " + quantity + ".");
            return done;
        } else {
            System.out.println("Product not found in inventory.");
            return false;
        }
    }
    
    public void saveToFile(String fileName) {
    try (BufferedWriter writer = new BufferedWriter(
            new OutputStreamWriter(new FileOutputStream(fileName), StandardCharsets.UTF_8))) {
        writer.write(name + "\n");
        writer.write(doctorId + "\n");

        writer.write(currentOrders.size() + "\n");

        synchronized (currentOrders) {
            for (Order order : currentOrders) {
                writer.write("---Order---\n");
                Customer customer = order.getcustomer();
                if (customer != null) {
                    writer.write((customer.getname() != null ? customer.getname() : "null") + "\n");
                    writer.write((customer.getcustomerId() != null ? customer.getcustomerId() : "null") + "\n");
                    writer.write((customer.getphone() != null ? customer.getphone() : "null") + "\n");
                } else {
                    writer.write("null\nnull\nnull\n");
                }

                writer.write(order.isOrderComplete() + "\n");

                writer.write(order.getOrderedProducts().size() + "\n");
                for (Product product : order.getOrderedProducts()) {
                    writer.write((product.getName() != null ? product.getName().replace(",", "\\,") : "null") + "," +
                                 (product.getProductId() != null ? product.getProductId() : "null") + "," +
                                 product.getPrice() + "," + product.getQuantity() + "\n");
                }
            }
        }

        writer.flush(); // Ensure all data is written
        System.out.println("Doctor data saved to " + fileName);
    } catch (IOException e) {
        System.err.println("Error saving doctor data to file \"" + fileName + "\": ");
        e.printStackTrace(); // Print the full stack trace
    }
}

// Load doctor data from a file
public void loadFromFile(String fileName) {
    try (BufferedReader reader = new BufferedReader(new FileReader(fileName))) {
        this.name = reader.readLine();
        this.doctorId = reader.readLine();

        int orderCount = Integer.parseInt(reader.readLine());
        currentOrders = new ArrayList<>();

        for (int i = 0; i < orderCount; i++) {
            String orderMarker = reader.readLine();
            if (!"---Order---".equals(orderMarker)) {
                throw new IOException("File format error: expected ---Order--- marker.");
            }

            String customerName = reader.readLine();
            String customerId = reader.readLine();
            String customerPhone = reader.readLine();
            Customer customer = null;
            if (!"null".equals(customerName)) {
                customer = new Customer(customerName, customerId, customerPhone);
            }

            boolean status = Boolean.parseBoolean(reader.readLine());

            int productCount = Integer.parseInt(reader.readLine());
            ArrayList<Product> products = new ArrayList<>();
            for (int j = 0; j < productCount; j++) {
                String[] productData = reader.readLine().split(",");
                if (productData.length != 4) {
                    throw new IOException("File format error: product data must contain 4 elements.");
                }

                String productName = productData[0];
                String productId = productData[1];
                double productPrice = Double.parseDouble(productData[2]);
                int productQuantity = Integer.parseInt(productData[3]);

                Product product = new Product(productName, productId, productPrice, productQuantity);
                products.add(product);
            }

            Order order = new Order(this, customer, null);
            for (Product product : products) {
                order.addProductToOrder(product, product.getQuantity());
            }

            order.status = status;
            currentOrders.add(order);
        }

        System.out.println("Doctor data loaded from file: " + fileName);
    } catch (IOException | NumberFormatException e) {
        System.err.println("Error loading doctor data from file \"" + fileName + "\": " + e.getMessage());
    }
}

}
