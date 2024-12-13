/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package mygui.pharmacy;

/**
 *
 * @author Mohamed
 */
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;
public class Inventory {
    private HashMap<String, Product> products;

    // constructor
    public Inventory() {
        products = new HashMap<>();
    }

    // method to add a specific product to (products)hashmap
    public void addProduct(Product product) {
        products.put(product.getProductId(), product);
    }

    // method to remove a specific product from (products)hashmap
    public void removeproduct(Product product) {
        if (products.get(product.getProductId()) != null)
            products.remove(product.getProductId());
    }

    // method to return all products Available in inventory
    public HashMap<String, Product> getAllProducts() {
        return products;
    }

    // method to update Quantity of a specific product
    public boolean updateQuantity(Product product, int quantity, boolean add) {
        if (add) {
            product.increasequantity(quantity);
            return true;
        } else if (product.getQuantity() >= Math.abs(quantity)) {
            product.decreasequantity(Math.abs(quantity));
            return true;
        } else {
            System.out.println("Insufficient quantity to reduce.");
            return false;
        }
    }

    // method to return a specific product by its productID
    public Product getProduct(String productId) {
        return products.get(productId);
    }

    // method to return boolean flag to answer query if product is available or not
    public boolean isProductAvailable(String productId, int quantity) {
        Product product = products.get(productId);
        return (product != null) && (product.getQuantity() >= quantity);
    }

    public void writeInventoryToFile(String fileName) {
        FileWriter writer = null;
        try {
            writer = new FileWriter(fileName, true);
            for (var product : products.values()) {
                writer.write("Product ID: " + product.getProductId() +
                        ", Name: " + product.getName() +
                        ", Quantity: " + product.getQuantity() + "\n");
            }
            System.out.println("Inventory has been written to file: " + fileName);
        } catch (IOException e) {
            System.out.println("An error occurred while writing the inventory to the file.");
            e.printStackTrace();
        } finally {
            try {
                if (writer != null) {
                    writer.close();
                }
            } catch (IOException e) {
                System.out.println("An error occurred while closing the file.");
                e.printStackTrace();
            }
        }
    }

    // method to desplay all products available in this Inventory
    public void display() {
        if (products.isEmpty()) {
            System.out.println("No products in inventory.");
        } else {
            for (var product : products.values()) {
                product.display();
            }
        }
    }
    public void writeinventorytofile(String fileName) {
        try (FileWriter writer = new FileWriter(fileName)) {
        	for (String key : products.keySet()) {
                Product product = products.get(key);
                writer.write(key + "," + product.getName() + "," + product.getPrice() + "," + product.getQuantity()+"\n");
                
            }
            System.out.println("Inventory saved to file: " + fileName);
        } catch (IOException e) {
            System.err.println("Error saving inventory: " + e.getMessage());
        }
    }

    // Load inventory from a file
    public void getinventoryFromFile(String fileName) {
        try (BufferedReader reader = new BufferedReader(new FileReader(fileName))) {
            products.clear();
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length == 4) {
                    String key = parts[0];
                    String name = parts[1];
                    double price = Double.parseDouble(parts[2]);
                    int quantity = Integer.parseInt(parts[3]);
                    products.put(key, new Product(name,key, price, quantity));
                }
            }
            System.out.println("Inventory loaded from " + fileName);
        } catch (IOException e) {
            System.out.println("Error loading inventory from file: " + e.getMessage());
        }
    }

}
