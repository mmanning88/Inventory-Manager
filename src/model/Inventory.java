//Author: Matthew Manning
package model;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;


public class Inventory {
    
    public static ObservableList<Part> allParts = FXCollections.observableArrayList();
    public static ObservableList<Product> allProducts = FXCollections.observableArrayList();
    
    public static ObservableList<Part> filteredParts = FXCollections.observableArrayList();
    public static ObservableList<Product> filteredProducts = FXCollections.observableArrayList();
    
    private static int partCountID = 0;
    private static int productCountID = 0;

    public static void deletePart(Part part) {
        allParts.remove(part);
    }
    
    public static void deleteProduct(Product product) {
        allProducts.remove(product);
    }
    
    public static void updatePart(int index, Part part) {
        allParts.set(index, part);
    }
    
    public static void updateProduct(int index, Product product) {
        allProducts.set(index, product);
    }
    
    public static int getPartCountID() {
        partCountID++;
        return partCountID;
    }
    
    public static int getProductCountID() {
        productCountID++;
        return productCountID;        
    }
    
    public static int cancelPartCountID() {
        partCountID--;
        return partCountID;
    }
    
    public static int cancelProductCountID() {
        productCountID--;
        return productCountID;        
    }
    

    public static Part lookupPart(int partId) {

        for (Part part : getAllParts()) {
            if (part.getId() == partId) {
                return part;
            }
        }
        return null;
    
    }
    
    public static ObservableList lookupPart(String partName) {
        
        for (Part part : getAllParts()) {
            if (part.getName().contains(partName)) {
                Inventory.getFilteredParts().add(part);
            }
        }
        
        if(Inventory.getFilteredParts().isEmpty()) {
            return Inventory.getAllParts();
        } else {
            return Inventory.getFilteredParts();
        }
    }
    
    public static ObservableList lookupProduct(String productName) {
        
        for (Product product : getAllProducts()) {
            if (product.getName().contains(productName)) {
                Inventory.getFilteredProducts().add(product);
            }
        }
        
        if(Inventory.getFilteredProducts().isEmpty()) {
            return Inventory.getAllProducts();
        } else {
            return Inventory.getFilteredProducts();
        }
    } 
    
    public static Product lookupProduct(int productId) {

        for (Product product : getAllProducts()) {
            if (product.getId() == productId) {
                return product;
            }
        }
        return null;
    
    }
                

    
    public static void addInHouse(Part part) {
        allParts.add(part);             
    }
    
    public static void addOutsourced(Part part) {
        allParts.add(part);
    }
    
    public static void addProduct(Product product) {
        allProducts.add(product);
    }
    
    public static ObservableList<Part> getAllParts() {
        return allParts;
    }
    
    public static ObservableList<Product> getAllProducts() {
        return allProducts;
    }
    
    public static ObservableList<Part> getFilteredParts() {
        return filteredParts;
    }
    
    public static ObservableList<Product> getFilteredProducts() {
        return filteredProducts;
    }
    

    
}
