//Author: Matthew Manning

package inventorymain;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import model.InHouse;
import model.Inventory;
import static model.Inventory.*;
import model.Outsourced;
import model.Product;

public class InventoryProgramMM extends Application {
    
    @Override
    public void start(Stage stage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("/view/InventoryMain.fxml"));
        
        Scene scene = new Scene(root);
        
        stage.setScene(scene);
        stage.show();
    }


    public static void main(String[] args) {
        
        
        InHouse part1 = new InHouse(getPartCountID(), 5, 2, 6, "IHPart1", 10.00, 101);
        InHouse part2 = new InHouse(getPartCountID(), 0, 0, 4, "IHPart2", 40.50, 102);
        InHouse part3 = new InHouse(getPartCountID(), 8, 5, 50, "IHPart3", 5.00, 103);
        
        Outsourced part4 = new Outsourced(getPartCountID(), 10, 0, 20, "OSPart4", 20.00, "ABC Inc.");
        Outsourced part5 = new Outsourced(getPartCountID(), 7, 0, 20, "OSPart5", 150.00, "BBB Inc.");
        Outsourced part6 = new Outsourced(getPartCountID(), 2, 0, 15, "OSPart6", 100.00, "DER Inc.");

        
        Product product1 = new Product(getProductCountID(), 4, 2, 5, "Product", 300.00);
        product1.addAssociatedPart(part1);
        product1.addAssociatedPart(part2);
        Product product2 = new Product(getProductCountID(), 20, 0, 25, "Another Product", 400.00);
        product2.addAssociatedPart(part3);
        product2.addAssociatedPart(part4);
        Product product3 = new Product(getProductCountID(), 50, 10, 50, "Different Product", 600.00);
        product3.addAssociatedPart(part4);
        product3.addAssociatedPart(part5);
        Product product4 = new Product(getProductCountID(), 15, 15, 50, "Unique Product", 459.99);
        product4.addAssociatedPart(part6);
        product4.addAssociatedPart(part1);
        
        Inventory.addInHouse(part1);
        Inventory.addInHouse(part2);
        Inventory.addInHouse(part3);
        Inventory.addOutsourced(part4);
        Inventory.addOutsourced(part5);
        Inventory.addOutsourced(part6);
        Inventory.addProduct(product1);
        Inventory.addProduct(product2);
        Inventory.addProduct(product3);
        Inventory.addProduct(product4);

        
        launch(args);
    }
    
}
