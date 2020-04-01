//Author: Matthew Manning
package controller;

import java.io.IOException;
import java.net.URL;
import java.text.NumberFormat;
import java.util.Optional;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import model.Inventory;
import static model.Inventory.*;
import model.Part;
import model.Product;


public class InventoryMainController implements Initializable {
    
    Stage stage;
    Parent scene;
            
    private static int modifyIndex;
    
    @FXML
    private TableView<Part> partsTableView;

    @FXML
    private TableColumn<Part, Integer> partsIdCol;

    @FXML
    private TableColumn<Part, String> partsNameCol;

    @FXML
    private TableColumn<Part, Integer> partsInvCol;

    @FXML
    private TableColumn<Part, Double> partsCostPerCol;

    @FXML
    private TextField partsSearchTxt;

    @FXML
    private TableView<Product> productTableView;

    @FXML
    private TableColumn<Product, Integer> productIdCol;

    @FXML
    private TableColumn<Product, String> productNameCol;

    @FXML
    private TableColumn<Product, Integer> productInvCol;

    @FXML
    private TableColumn<Product, Double> productCostPerCol;

    @FXML
    private TextField productsSearchTxt;

    @FXML
    void onActionAddPart(ActionEvent event) throws IOException {
        
        stage = (Stage)((Button)event.getSource()).getScene().getWindow();       
        scene = FXMLLoader.load(getClass().getResource("/view/AddPart.fxml"));
        stage.setScene(new Scene(scene));
        stage.show();
        
    }

    @FXML
    void onActionAddProducts(ActionEvent event) throws IOException {
        
        stage = (Stage)((Button)event.getSource()).getScene().getWindow();
        scene = FXMLLoader.load(getClass().getResource("/view/AddProduct.fxml"));
        stage.setScene(new Scene(scene));
        stage.show();

    }

    @FXML
    void onActionDeletePart(ActionEvent event) {
        
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Delete Part Confirmation");
        alert.setContentText("Are you sure?");
        
        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == ButtonType.OK){
            Part part = partsTableView.getSelectionModel().getSelectedItem();
            deletePart(part); //calls from Inventory
        } else {

        }

               

    }

    @FXML
    void onActionDeleteProducts(ActionEvent event) {
        
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Delete Product Confirmation");
        alert.setContentText("Are you sure?");
        
        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == ButtonType.OK){
            Product product = productTableView.getSelectionModel().getSelectedItem();
            deleteProduct(product);
        } else {
        }


    }

    @FXML
    void onActionExit(ActionEvent event) {
        
        System.exit(0); //completely terminates JVM

    }
    
    public static int modifyIndex() {
        return modifyIndex;
    }

    @FXML
    void onActionModifyPart(ActionEvent event) throws IOException {
                        
        Part selectedPart = partsTableView.getSelectionModel().getSelectedItem();
        InventoryMainController.modifyIndex = getAllParts().indexOf(selectedPart); //return instance index variable to class variable
        
        if (selectedPart == null) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Warning Dialog");
            alert.setContentText("Please select part before modifying.");
            alert.showAndWait();
            return;
        }
        
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/view/ModifyPart.fxml"));
        loader.load();
        
        ModifyPartController MPController = loader.getController();   
        MPController.sendPart(selectedPart);  //sends selected part(MPController) to ModifyPartController
        
        
        stage = (Stage)((Button)event.getSource()).getScene().getWindow();
        Parent scene = loader.getRoot();
        stage.setScene(new Scene(scene));
        stage.show();

    }

    @FXML
    void onActionModifyProducts(ActionEvent event) throws IOException {
        
        Product selectedProduct = productTableView.getSelectionModel().getSelectedItem();
        InventoryMainController.modifyIndex = getAllProducts().indexOf(selectedProduct);
        //Exception handling for when no product is selected before trying to modify
        if (selectedProduct == null) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Warning Dialog");
            alert.setContentText("Please select product before modifying.");
            alert.showAndWait();
            return;
        }
        
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/view/ModifyProduct.fxml"));
        loader.load();
        
        ModifyProductController MPrController = loader.getController();   
        MPrController.sendProduct(selectedProduct);
        
        stage = (Stage)((Button)event.getSource()).getScene().getWindow();
        Parent scene = loader.getRoot();
        stage.setScene(new Scene(scene));
        stage.show();


    }

    @FXML
    void onActionSearchProducts(ActionEvent event) {
        
        Inventory.getFilteredProducts().clear();
        
        String search = productsSearchTxt.getText();
        if (search.isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Warning Dialog");
            alert.setContentText("Enter product name");
            alert.showAndWait();
        }
        
        lookupProduct(search);
        productTableView.setItems(Inventory.getFilteredProducts());
   
    }
    
    @FXML
    void onActionSearchParts(ActionEvent event) {
        

        
        Inventory.getFilteredParts().clear();
        
        String search = partsSearchTxt.getText();
        //Exception handling for no string in searchbar before search button clicked
        if (search.isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Warning Dialog");
            alert.setContentText("Enter part name");
            alert.showAndWait();
        }


        
        lookupPart(search);
        partsTableView.setItems(Inventory.getFilteredParts());


    }
  
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
        partsTableView.setItems(Inventory.getAllParts());     
        
        partsIdCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        partsNameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        partsInvCol.setCellValueFactory(new PropertyValueFactory<>("stock"));
        partsCostPerCol.setCellValueFactory(new PropertyValueFactory<>("price"));
        
        //change cost column to display as currency
        NumberFormat currencyFormat = NumberFormat.getCurrencyInstance(); 
        partsCostPerCol.setCellFactory(tc -> new TableCell<Part, Double>() {

        @Override
            protected void updateItem(Double price, boolean empty) {
                super.updateItem(price, empty);
                    if (empty) {
                    setText(null);
                    } else {
                    setText(currencyFormat.format(price));
                    }
                }
             });
        
        
        productTableView.setItems(Inventory.getAllProducts());     
        
        productIdCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        productNameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        productInvCol.setCellValueFactory(new PropertyValueFactory<>("stock"));
        productCostPerCol.setCellValueFactory(new PropertyValueFactory<>("price"));
        
        productCostPerCol.setCellFactory(cell -> new TableCell<Product, Double>() {

        @Override
            protected void updateItem(Double price, boolean empty) {
                super.updateItem(price, empty);
                    if (empty) {
                    setText(null);
                    } else {
                    setText(currencyFormat.format(price));
                    }
                }
             });
        
    
    }    
    
}
