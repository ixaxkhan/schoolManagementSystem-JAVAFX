/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package login.superAdmin.standard;

import com.jfoenix.controls.JFXCheckBox;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextField;
import databaseconnection.DatabaseConnection;
import java.io.File;
import java.io.IOException;
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
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Duration;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.xml.JRXmlLoader;
import net.sf.jasperreports.view.JasperViewer;
import org.controlsfx.control.Notifications;

/**
 * FXML Controller class
 *
 * @author khan
 */
public class classViewController implements Initializable {
    @FXML
    private JFXTextField ClassName;
    @FXML
    private JFXCheckBox checkBox;
    @FXML
    private JFXTextField classID;

    //************** for database connection***********
    Connection connection = null;
    PreparedStatement statement = null;
    ResultSet resultSet = null;
    String sql = null;
    @FXML
    private JFXComboBox<String> comboSession;
    
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        classID.setVisible(false);
        try {
            loadYears();
            loadsClass();
            abledisableTXFID();
        } catch (ClassNotFoundException | SQLException ex) {
            Logger.getLogger(classViewController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }    

    @FXML
    private void save(ActionEvent event)throws ClassNotFoundException, SQLException {
        
        int sessionID=0;
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmation Dialog");
        alert.setHeaderText(null);
        alert.setContentText("Are you soure to save class?");
        alert.initModality(Modality.APPLICATION_MODAL);
        Optional<ButtonType> action = alert.showAndWait();
        if (action.get() == ButtonType.OK) {
        
            connection = DatabaseConnection.getConnection();
            sql = "select * from session where year=? ";
            statement = connection.prepareStatement(sql);
            statement.setString(1, comboSession.getValue());
            resultSet=statement.executeQuery();
            if(resultSet.next()){
            sessionID=resultSet.getInt("id");
            
            }
            sql = "INSERT INTO class(name,session_id) VALUES(?,?) ";
            statement = connection.prepareStatement(sql);
            statement.setString(1, ClassName.getText());
            statement.setInt(2,sessionID);
            statement.execute();
            showNotifications("Class Add","Class add sucessfully");
        
        }
        
    }
    
    private void abledisableTXFID() {

        checkBox.selectedProperty().addListener(new ChangeListener<Boolean>() {
            public void changed(ObservableValue<? extends Boolean> ov,
                    Boolean old_val, Boolean new_val) {
                if (new_val) {
                    classID.setVisible(true);

                } else {

                    classID.setVisible(false);
                    classID.setText("");
                }
            }
        });

    }
    private void showNotifications(String title, String text) {

        Notifications.create()
                .title(title)
                .text(text)
                .hideAfter(Duration.seconds(3))
                .darkStyle()
                .hideCloseButton()
                .position(Pos.TOP_LEFT)
                .showConfirm();

    }
    @FXML
    private void update(ActionEvent event)throws ClassNotFoundException, SQLException {
          int sessionID=0;
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmation Dialog");
        alert.setHeaderText(null);
        alert.setContentText("Are you soure to update class?");
        alert.initModality(Modality.APPLICATION_MODAL);
        Optional<ButtonType> action = alert.showAndWait();
        if (action.get() == ButtonType.OK) {
            connection = DatabaseConnection.getConnection();
            sql = "select * from session where year=? ";
            statement = connection.prepareStatement(sql);
            statement.setString(1, comboSession.getValue());
            resultSet = statement.executeQuery();
            if (resultSet.next()) {
                sessionID = resultSet.getInt("id");

            }
            sql="update class set name=? ,session_id=? where id=?";
            statement = connection.prepareStatement(sql);
            statement.setString(1, ClassName.getText());
            statement.setInt(2, sessionID);
            statement.setInt(3, Integer.parseInt(classID.getText()));
            statement.execute();
            showNotifications("Class Updation", "Class update sucessfully");
        }
    }

    @FXML
    private void delete(ActionEvent event)throws ClassNotFoundException, SQLException {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmation Dialog");
        alert.setHeaderText(null);
        alert.setContentText("Are you soure to delete class?");
        alert.initModality(Modality.APPLICATION_MODAL);
        Optional<ButtonType> action = alert.showAndWait();
        if (action.get() == ButtonType.OK) {
            connection = DatabaseConnection.getConnection();
            sql = "delete from class where id=?";
            statement = connection.prepareStatement(sql);
            statement.setInt(1, Integer.parseInt(classID.getText()));
            statement.execute();
            showNotifications("Class Deletion", "Class Delete sucessfully");
        }
    }

    @FXML
    private void view(ActionEvent event) throws ClassNotFoundException, JRException {
        
        connection = DatabaseConnection.getConnection();

        File file = new File("src/login/superAdmin/standard/classes.jrxml");
        JasperDesign jasperDesign = JRXmlLoader.load("C:\\reports\\classes.jrxml");
        JasperReport jasperReport = JasperCompileManager.compileReport(jasperDesign);
        JasperPrint JasperPrint = (JasperPrint) JasperFillManager.fillReport(jasperReport, null, connection);
        JasperViewer jv = new JasperViewer(JasperPrint, false);
        JasperViewer.viewReport(JasperPrint, false);
    }

    @FXML
    private void closeWindow(ActionEvent event) {
        
        final Node source = (Node) event.getSource();
        Stage stage = (Stage) source.getScene().getWindow();
        stage.close();
    }

    @FXML
    private void addSectionsToClass(ActionEvent event) throws IOException {
        
        Parent root;
        root = FXMLLoader.load(getClass().getResource("addSectionView.fxml"));
        final Node source = (Node) event.getSource();
        final Stage stage2 = (Stage) source.getScene().getWindow();
        stage2.close();
        Scene sceen = new Scene(root);
        Stage stage = new Stage();
        stage.setScene(sceen);
        stage.show();
    }
    private void loadYears()throws ClassNotFoundException, SQLException {
    
    
        connection = DatabaseConnection.getConnection();
        sql = "select * from session";
        statement = connection.prepareStatement(sql);
        resultSet=statement.executeQuery();
        comboSession.getItems().clear();
        while(resultSet.next()){
        
            comboSession.getItems().add(resultSet.getString("year"));
        
        }
    
    }
   private void loadsClass(){
       classID.textProperty().addListener((observable, oldValue, newValue) -> {
       
           try {
               connection = DatabaseConnection.getConnection();
               sql = "select * from class where id=? ";
               statement = connection.prepareStatement(sql);
               statement.setInt(1, Integer.parseInt(newValue));
               resultSet = statement.executeQuery();
               if (resultSet.next()) {
               
                   ClassName.setText(""+resultSet.getString("name"));
                  
                   sql = "select * from session where id=? ";
                   statement = connection.prepareStatement(sql);
                   statement.setInt(1, resultSet.getInt("session_id"));
                   resultSet=statement.executeQuery();
                   if(resultSet.next()){
                   
                       comboSession.setValue(resultSet.getString("year"));
                   
                   }
                   
               
               }else{
               
                   comboSession.setValue("");
                   ClassName.setText("");
               
               
               }
           } catch (ClassNotFoundException | SQLException ex) {
               Logger.getLogger(classViewController.class.getName()).log(Level.SEVERE, null, ex);
           }
          
       
       
       
       
       });
   
   
   }
    
}
