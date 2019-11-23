//Author: Matthew Manning
package controller;

import static controller.InventoryMainController.modifyIndex;
import java.io.IOException;
import java.net.URL;
import java.text.NumberFormat;
import java.util.Optional;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
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


public class ModifyProductController implements Initializable {
    
    Stage stage;
    Parent scene;
    
    private ObservableList<Part> partsToSave = FXCollections.observableArrayList();
    
    @FXML
    private TableView<Part> associatedPartsTableView;

    @FXML
    private TableColumn<Part, Integer> associatedPartsIdCol;

    @FXML
    private TableColumn<Part, String> associatedPartsNameCol;

    @FXML
    private TableColumn<Part, Integer> associatedPartsInvCol;

    @FXML
    private TableColumn<Part, Double> associatedPartsCostPerCol;

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
    private TextField productIdTxt;

    @FXML
    private TextField productNameTxt;

    @FXML
    private TextField productInvTxt;

    @FXML
    private TextField productPriceTxt;

    @FXML
    private TextField productMaxTxt;

    @FXML
    private TextField productMinTxt;
    
    @FXML
    private TextField PartsTxt;

    
     @FXML
    void OnActionSaveProduct(ActionEvent event) throws IOException {
        
        try {
        int stock;
        int index = modifyIndex();
        int id = Integer.parseInt(productIdTxt.getText());
        String name = productNameTxt.getText();
        
        if (productInvTxt.getText().trim().isEmpty()) {
            stock = 0;
        } else {
            stock = Integer.parseInt(productInvTxt.getText());
        }
        
        double price = Double.parseDouble(productPriceTxt.getText());
        int min = Integer.parseInt(productMinTxt.getText());
        int max = Integer.parseInt(productMaxTxt.getText());
        
        if (name.isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Product Name Error");
            alert.setContentText("Products must have a name");
            alert.showAndWait();
            return;
        }
        
        if (stock < min || stock > max) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Inventory Error");
            alert.setContentText("Current Inventory must be between minimum and maximum");
            alert.showAndWait();
            return;
        }
        
        if (min > max || max < min) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Inventory");
            alert.setContentText("Max inventory can not be greater than min OR min inventory can not be less than max");
            alert.showAndWait();
            return;
        }
        
        if (min < 0) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Inventory");
            alert.setContentText("Minimum inventory must be at least 0");
            alert.showAndWait();
            return;
        }
        
        if (associatedPartsTableView.getItems().isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Part Error");
            alert.setContentText("A product must have at least one associated part");
            alert.showAndWait();
            return;
        }
        
        if (associatedPartsTableView.getItems().isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Part Error");
            alert.setContentText("A product must have at least one associated part");
            alert.showAndWait();
            return;
        }
        
        for (Part part : partsToSave) {
            double sum =+ part.getPrice();
            if (sum > Double.parseDouble(productPriceTxt.getText())) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Product Price Error");
                alert.setContentText("A product must cost more than the sum of it's associated parts.");
                alert.showAndWait();
                return;
            }
        }
        
        Product modifyProduct = new Product(id, stock, min, max, name, price);
        modifyProduct.setAssociatedParts(partsToSave);
        
        Inventory.updateProduct(index, modifyProduct);
        
        stage = (Stage)((Button)event.getSource()).getScene().getWindow();       
        scene = FXMLLoader.load(getClass().getResource("/view/InventoryMain.fxml"));
        stage.setScene(new Scene(scene));
        stage.show();
        } catch (NumberFormatException e){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Modify Product Error");
            alert.setContentText("Fill all fields before saving");
            alert.showAndWait();
        }
    }

    @FXML
    void OnActionToMainMenu(ActionEvent event) throws IOException {
        
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Cancel Modify Product Confirmation");
        alert.setContentText("Are you sure?");
        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == ButtonType.OK) {
            
            cancelPartCountID();
        
            stage = (Stage)((Button)event.getSource()).getScene().getWindow();
            scene = FXMLLoader.load(getClass().getResource("/view/InventoryMain.fxml"));
            stage.setScene(new Scene(scene));
            stage.show();
            
        } else {
            
        }

    }

    @FXML
    void onActionAddPart(ActionEvent event) {
        
               
        Part partToAdd = partsTableView.getSelectionModel().getSelectedItem();
        
        if (associatedPartsTableView.getItems().contains(partToAdd)) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Associated Part Error");
            alert.setContentText("You can not add an existing associated part.");
            alert.showAndWait();
            return;
        }
        
        associatedPartsTableView.getItems().add(partToAdd);
        partsToSave.add(partToAdd);
    }

    @FXML
    void onActionDeletePart(ActionEvent event) {
        
        Part partToDelete = associatedPartsTableView.getSelectionModel().getSelectedItem();
        associatedPartsTableView.getItems().remove(partToDelete);
        partsToSave.remove(partToDelete);

    }
    
    public void sendProduct (Product product) {
        
        productIdTxt.setText(String.valueOf(product.getId()));
        productNameTxt.setText(String.valueOf(product.getName()));
        productInvTxt.setText(String.valueOf(product.getStock()));
        productPriceTxt.setText(String.valueOf(product.getPrice()));
        productMaxTxt.setText(String.valueOf(product.getMax()));
        productMinTxt.setText(String.valueOf(product.getMin()));
        
        associatedPartsTableView.setItems(product.getAllAssociatedParts());
        partsToSave.addAll(product.getAllAssociatedParts());
        
        
    }
    
    @FXML
    void onActionSearchParts(ActionEvent event) {

        Inventory.getFilteredParts().clear();
        
        String search = PartsTxt.getText();

        
        lookupPart(search);
        partsTableView.setItems(Inventory.getFilteredParts());


    }
    


    @Override
    public void initialize(URL url, ResourceBundle rb) {
          
        partsTableView.setItems(Inventory.getAllParts());    
        
        productIdTxt.setEditable(false);
        productIdTxt.setMouseTransparent(true);
        productIdTxt.setFocusTraversable(false);
        
        partsIdCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        partsNameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        partsInvCol.setCellValueFactory(new PropertyValueFactory<>("stock"));
        partsCostPerCol.setCellValueFactory(new PropertyValueFactory<>("price"));
            
        associatedPartsIdCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        associatedPartsNameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        associatedPartsInvCol.setCellValueFactory(new PropertyValueFactory<>("stock"));
        associatedPartsCostPerCol.setCellValueFactory(new PropertyValueFactory<>("price"));
            
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
        
        associatedPartsCostPerCol.setCellFactory(tc -> new TableCell<Part, Double>() {

        
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
