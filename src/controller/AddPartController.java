//Author: Matthew Manning
package controller;

import java.io.IOException;
import java.net.URL;
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
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import model.InHouse;
import model.Inventory;
import static model.Inventory.*;
import model.Outsourced;


public class AddPartController implements Initializable {
    
    Stage stage;
    Parent scene;
    
    private int newPartID;
    
    @FXML
    private Label modifyPartRBSwitchLbl;
    
    @FXML
    private TextField partIDTxt;

    @FXML
    private TextField partNameTxt;

    @FXML
    private TextField partInvTxt;

    @FXML
    private TextField partPriceTxt;

    @FXML
    private TextField partMaxTxt;

    @FXML
    private TextField partMinTxt;

    @FXML
    private TextField partMaCoTxt;

    @FXML
    private RadioButton partInHouseRB;

    @FXML
    private RadioButton partOutsourcedRB;
    
    @FXML
    void onActionSavePart(ActionEvent event) throws IOException {
        
        try {

        int id = newPartID;
        String name = partNameTxt.getText();
        int stock = Integer.parseInt(partInvTxt.getText());
        double price = Double.parseDouble(partPriceTxt.getText());
        int min = Integer.parseInt(partMinTxt.getText());
        int max = Integer.parseInt(partMaxTxt.getText());
        //Exception handling for trying to create part with no name
        if (name.isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Part Name Error");
            alert.setContentText("Parts must have a name");
            alert.showAndWait();
            return;
        }
        //Exception handling for trying to create part with 0 price
        if (price == 0) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Part Price Error");
            alert.setContentText("Parts must have a price greater than 0");
            alert.showAndWait();
            return;
        }
      //Exception handling for creating a part with stock not inclusive with min or max
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
        //Exception handling for creating part with stock less than 0
        if (min < 0) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Inventory");
            alert.setContentText("Minimum inventory must be at least 0");
            alert.showAndWait();
            return;
        }
        
        
        if (partInHouseRB.isSelected()) {
            int machineId = Integer.parseInt(partMaCoTxt.getText());
            Inventory.addInHouse(new InHouse(id, stock, min, max, name, price, machineId));
        }
        
        if (partOutsourcedRB.isSelected()) {
            String companyName = partMaCoTxt.getText();
            if (companyName.isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Outsorced Company Name Error");
            alert.setContentText("Outsourced Parts must have origin company");
            alert.showAndWait();
            return;
            }
            Inventory.addOutsourced(new Outsourced(id, stock, min, max, name, price, companyName));
        }
        
        
        stage = (Stage)((Button)event.getSource()).getScene().getWindow();       
        scene = FXMLLoader.load(getClass().getResource("/view/InventoryMain.fxml"));
        stage.setScene(new Scene(scene));
        stage.show();
        } catch (NumberFormatException e){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Add Part Error");
            alert.setContentText("Fill all fields before saving");
            alert.showAndWait();
        }

        
        

    }

    @FXML
    void onActionToMainMenu(ActionEvent event) throws IOException {
        
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Cancel Add Part Confirmation");
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
    void onActionInHouse(ActionEvent event) {
        modifyPartRBSwitchLbl.setText("Machine ID");
    }

    @FXML
    void onActionOutsourced(ActionEvent event) {
        modifyPartRBSwitchLbl.setText("Company Name");
    }



    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
        
        
        newPartID = Inventory.getPartCountID();
        partIDTxt.setText(Integer.toString(newPartID));
        partIDTxt.setEditable(false);
        partIDTxt.setMouseTransparent(true);
        partIDTxt.setFocusTraversable(false);


    }    
    
}
