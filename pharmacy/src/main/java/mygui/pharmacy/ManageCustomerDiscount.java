/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package mygui.pharmacy;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

/**
 *
 * @author Mohamed
 */
public class ManageCustomerDiscount {
    private HashMap<String, Integer> frequencyForCustomer;

    // constructor
    public ManageCustomerDiscount() {
        this.frequencyForCustomer = new HashMap<>();
    }

    // method to add customer
    public void addCustomer(String id) {
        frequencyForCustomer.put(id, 1);
    }

    // method to check if this customer
    public void checkAndUpdate(String id) {
        if (!frequencyForCustomer.containsKey(id)) {
            addCustomer(id);
        } else {
            frequencyForCustomer.put(id, frequencyForCustomer.get(id) + 1);
        }
    }

    // method to calculate the percentage of discount by using Number of visits
    public double calculateDiscount(String id) {
        if (!frequencyForCustomer.containsKey(id)) {
            return 0;
        }
        int purchaseFrequency = frequencyForCustomer.get(id);
        double discount;
        if (purchaseFrequency >= 20) {
            discount = 0.20;
        } else if (purchaseFrequency >= 10) {
            discount = 0.10;
        } else if (purchaseFrequency >= 5) {
            discount = 0.05;
        } else {
            discount = 0.0;
        }
        return discount;
    }
    public void saveToFile(String fileName) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileName))) {
            for (String customer : frequencyForCustomer.keySet()) {
                writer.write(customer + "," + frequencyForCustomer.get(customer));
                writer.newLine();
            }
            System.out.println("Discount data saved to " + fileName);
        } catch (IOException e) {
            System.out.println("Error saving discount data to file: " + e.getMessage());
        }
    }

    // Load discount data from a file
    public void loadFromFile(String fileName) {
        try (BufferedReader reader = new BufferedReader(new FileReader(fileName))) {
            frequencyForCustomer.clear();
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                frequencyForCustomer.put(parts[0], Integer.parseInt(parts[1]));
            }
            System.out.println("Discount data loaded from " + fileName);
        } catch (IOException e) {
            System.out.println("Error loading discount data from file: " + e.getMessage());
        }
    }
}
