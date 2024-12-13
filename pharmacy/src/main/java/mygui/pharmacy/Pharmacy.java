/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

package mygui.pharmacy;
import java.util.*;
import javax.swing.SwingUtilities;
import java.awt.Color;
import java.awt.event.*;
import java.io.File;
/**
 *
 * @author Mohamed
 */
public class Pharmacy {
public  static Doctor Admin;
public static Inventory inventory = new Inventory();

public static ManageCustomerDiscount manage = new ManageCustomerDiscount();
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
        new LoginF().setVisible(true);
    });
//        LoginF myFrame = new LoginF();
//        myFrame.setVisible(true);
   }
}
