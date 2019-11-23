//Author: Matthew Manning
package controller;

import static controller.InventoryMainController.modifyIndex;
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
import static model.Inventory.cancelPartCountID;
import model.Outsourced;
import model.Part;


public class ModifyPartController implements Initializable {
    
    Stage stage;
    Parent scene;

    @FXML
    private Label modifyPartRBSwitchLbl;
    
    @FXML
    private RadioButton partInHouseRB;

    @FXML
    private RadioButton partOutsourcedRB;

    @FXML
    private TextField partIdTxt;

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
    void onActionInHouse(ActionEvent event) {
        modifyPartRBSwitchLbl.setText("Machine ID");
    }

    @FXML
    void onActionOutsourced(ActionEvent event) {
        modifyPartRBSwitchLbl.setText("Company Name");
    }

    @FXML
    void onActionSavePart(ActionEvent event) throws IOException {
        
        try {
        int index = modifyIndex();
        int id = Integer.parseInt(partIdTxt.getText());
        String name = partNameTxt.getText();
        int stock = Integer.parseInt(partInvTxt.getText());
        double price = Double.parseDouble(partPriceTxt.getText());
        int min = Integer.parseInt(partMinTxt.getText());
        int max = Integer.parseInt(partMaxTxt.getText());
        
        if (name.isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Part Name Error");
            alert.setContentText("Parts must have a name");
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
        
        
        if (partInHouseRB.isSelected()) {
            
            int machineId = Integer.parseInt(partMaCoTxt.getText());           
            InHouse modifyPart = new InHouse(id, stock, min, max, name, price, machineId);            
            Inventory.updatePart(index, modifyPart);
            
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
            Outsourced modifyPart = new Outsourced(id, stock, min, max, name, price, companyName);
            Inventory.updatePart(index, modifyPart);
            
        }

        if (!partOutsourcedRB.isSelected() && !partInHouseRB.isSelected()) {
            
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Warning Dialog");
            alert.setContentText("Please select Outsourced or Inhouse");
            alert.showAndWait();
            return;
        }

      
        stage = (Stage)((Button)event.getSource()).getScene().getWindow();       
        scene = FXMLLoader.load(getClass().getResource("/view/InventoryMain.fxml"));
        stage.setScene(new Scene(scene));
        stage.show();
        } catch (NumberFormatException e){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Modify Part Error");
            alert.setContentText("Fill all fields before saving");
            alert.showAndWait();
        }
    }

    @FXML
    void onActionToMainMenu(ActionEvent event) throws IOException {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Cancel Modify Part Confirmation");
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
    
    public void sendPart (Part part) {        
        
        partIdTxt.setText(String.valueOf(part.getId()));
        partNameTxt.setText(String.valueOf(part.getName()));
        partInvTxt.setText(String.valueOf(part.getStock()));
        partPriceTxt.setText(String.valueOf(part.getPrice()));
        partMaxTxt.setText(String.valueOf(part.getMax()));
        partMinTxt.setText(String.valueOf(part.getMin()));
        
        if (part instanceof InHouse) {
            partMaCoTxt.setText(String.valueOf(((InHouse) part).getMachineId()));
            partInHouseRB.setSelected(true);
            modifyPartRBSwitchLbl.setText("Machine ID");
        } 
        
        if (part instanceof Outsourced) {
            partMaCoTxt.setText(String.valueOf(((Outsourced) part).getCompanyName()));
            partOutsourcedRB.setSelected(true);
            modifyPartRBSwitchLbl.setText("Company Name");
        } 
                
                 
    }


    @Override
    public void initialize(URL url, ResourceBundle rb) {
        

        
        partIdTxt.setEditable(false);
        partIdTxt.setMouseTransparent(true);
        partIdTxt.setFocusTraversable(false);

    }    
    
}
