 /*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package login.superAdmin.feeStructure;

import com.jfoenix.controls.JFXCheckBox;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextField;
import databaseconnection.DatabaseConnection;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Duration;
import org.controlsfx.control.Notifications;

/**
 * FXML Controller class
 *
 * @author khan
 */
public class ClassToutionFeeViewController implements Initializable {
    @FXML
    private JFXTextField Amount;
    @FXML
    private JFXCheckBox checkBox;
    @FXML
    private JFXTextField ToutionID;
    @FXML
    private JFXComboBox<String> className;

    //************** for database connection***********
    Connection connection = null;
    PreparedStatement statement = null;
    ResultSet resultSet = null;
    String sql = null;
    
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        try {
            ToutionID.setVisible(false);
            abledisableTXFID();
            loadClassName();
            loadToutionFee();
            
            
        } catch (ClassNotFoundException | SQLException ex) {
            Logger.getLogger(ClassToutionFeeViewController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }    

    @FXML
    private void save(ActionEvent event) throws ClassNotFoundException, SQLException {
        int classId=0;
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmation Dialog");
        alert.setHeaderText(null);
        alert.setContentText("Are you soure to save toution fee?");
        alert.initModality(Modality.APPLICATION_MODAL);
        Optional<ButtonType> action = alert.showAndWait();
        if (action.get() == ButtonType.OK) {
            connection = DatabaseConnection.getConnection();
            sql = "select id from class where name = ?";
            statement = connection.prepareStatement(sql);
            statement.setString(1, className.getValue());
            resultSet = statement.executeQuery();
            if (resultSet.next()) {

                classId = resultSet.getInt("id");
            }
            sql = " insert into toution_fee (amount,class_id) values(?,?)";
            statement = connection.prepareStatement(sql);
            statement.setDouble(1, Double.parseDouble(Amount.getText()));
            statement.setInt(2, classId);
            statement.execute();
            showNotifications("toution fee", "toution fee save for class");
        }
    }
    private void showNotifications(String title, String text) {

        Notifications.create()
                .title(title)
                .text(text)
                .hideAfter(Duration.seconds(1))
                .darkStyle()
                .hideCloseButton()
                .position(Pos.TOP_LEFT)
                .showConfirm();

    }
    @FXML
    private void update(ActionEvent event) throws ClassNotFoundException, SQLException {
        int classId = 0;
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmation Dialog");
        alert.setHeaderText(null);
        alert.setContentText("Are you soure to update toution fee?");
        alert.initModality(Modality.APPLICATION_MODAL);
        Optional<ButtonType> action = alert.showAndWait();
        if (action.get() == ButtonType.OK) {
            connection = DatabaseConnection.getConnection();
            sql = "select id from class where name = ?";
            statement = connection.prepareStatement(sql);
            statement.setString(1, className.getValue());
            resultSet = statement.executeQuery();
            if (resultSet.next()) {

                classId = resultSet.getInt("id");
            }
            sql = "update toution_fee set amount=?, class_id=? where id=?";
            statement = connection.prepareStatement(sql);
            statement.setDouble(1, Double.parseDouble(Amount.getText()));
            statement.setInt(2, classId);
            statement.setInt(3, Integer.parseInt(ToutionID.getText()));
            statement.execute();
            showNotifications("toution fee", "toution fee update sucessfully");
        
        }
    }

    @FXML
    private void delete(ActionEvent event)  throws ClassNotFoundException, SQLException{
        
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmation Dialog");
        alert.setHeaderText(null);
        alert.setContentText("Are you soure to delete toution fee?");
        alert.initModality(Modality.APPLICATION_MODAL);
        Optional<ButtonType> action = alert.showAndWait();
        if (action.get() == ButtonType.OK) {
            connection = DatabaseConnection.getConnection();
            sql = "delete from toution_fee where id=?";
            statement = connection.prepareStatement(sql);
            statement.setInt(1, Integer.parseInt(ToutionID.getText()));
            statement.execute();
            showNotifications("toution fee", "toution fee delete sucessfully");
        }
    }

    @FXML
    private void view(ActionEvent event) {
    }

    @FXML
    private void closeWindow(ActionEvent event) {
        
        final Node source = (Node) event.getSource();
        Stage stage = (Stage) source.getScene().getWindow();
        stage.close();
    }
    private  void loadClassName() throws ClassNotFoundException, SQLException{
    
        connection = DatabaseConnection.getConnection();
        sql = "select * from class";
        statement = connection.prepareStatement(sql);
        resultSet=statement.executeQuery();
         className.getItems().clear();
        while(resultSet.next()){
        
            className.getItems().add(resultSet.getString("name"));
        
        }
    
    }
    private void abledisableTXFID() {

        checkBox.selectedProperty().addListener(new ChangeListener<Boolean>() {
            public void changed(ObservableValue<? extends Boolean> ov,
                    Boolean old_val, Boolean new_val) {
                if (new_val) {
                    ToutionID.setVisible(true);

                } else {

                   ToutionID.setVisible(false);
                    ToutionID.setText("");
                }
            }
        });

    }
    private void loadToutionFee(){
    
        ToutionID.textProperty().addListener((observable, oldValue, newValue) -> {
        
            try {
                connection = DatabaseConnection.getConnection();
                
                sql = "select * from toution_fee where id= ?";
                statement = connection.prepareStatement(sql);
                statement.setInt(1, Integer.parseInt(newValue));
                resultSet=statement.executeQuery();
                if(resultSet.next()){
                   
                    Amount.setText(""+resultSet.getDouble("amount"));
                    sql = "select * from class where id= ?";
                    statement = connection.prepareStatement(sql);
                    statement.setInt(1,resultSet.getInt("class_id"));
                    resultSet=statement.executeQuery();
                    if(resultSet.next()){
                    className.setValue("" +resultSet.getString("name"));
                    }
                
                }else{
                
                className.setValue("");
                Amount.setText("");
                }
                
            } catch (ClassNotFoundException ex) {
                Logger.getLogger(ClassToutionFeeViewController.class.getName()).log(Level.SEVERE, null, ex);
            } catch (SQLException ex) {
                Logger.getLogger(ClassToutionFeeViewController.class.getName()).log(Level.SEVERE, null, ex);
            }
         
        
        
        });
    
    
    }
}
