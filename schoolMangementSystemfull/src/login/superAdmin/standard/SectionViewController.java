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
public class SectionViewController implements Initializable {
    @FXML
    private JFXTextField sectionName;
    @FXML
    private JFXCheckBox checkBox;
    @FXML
    private JFXTextField sectionID;
    @FXML
    private JFXComboBox<String> ClassName;

    //************** for database connection***********
    Connection connection = null;
    PreparedStatement statement = null;
    ResultSet resultSet = null;
    String sql = null;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        sectionID.setVisible(false);
        try {
            loadClasses();
            abledisableTXFID();
            loadSection();
        } catch (ClassNotFoundException | SQLException ex) {
            Logger.getLogger(SectionViewController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }    
    private void abledisableTXFID() {

        checkBox.selectedProperty().addListener(new ChangeListener<Boolean>() {
            public void changed(ObservableValue<? extends Boolean> ov,
                    Boolean old_val, Boolean new_val) {
                if (new_val) {
                    sectionID.setVisible(true);

                } else {

                    sectionID.setVisible(false);
                    sectionID.setText("");
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
    private void save(ActionEvent event) throws ClassNotFoundException, SQLException {
        int classID=0;
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmation Dialog");
        alert.setHeaderText(null);
        alert.setContentText("Are you soure to save section?");
        alert.initModality(Modality.APPLICATION_MODAL);
        Optional<ButtonType> action = alert.showAndWait();
        if (action.get() == ButtonType.OK) {
            
            connection = DatabaseConnection.getConnection();
            
            sql = "select * from class where name=?";
            statement = connection.prepareStatement(sql);
            statement.setString(1,ClassName.getValue());
            resultSet=statement.executeQuery();
            if(resultSet.next()){
            
            classID=resultSet.getInt("id");
            
            }
            sql = "insert into sections (name,class_id) values(?,?) ";
            statement = connection.prepareStatement(sql);
            statement.setString(1, sectionName.getText());
            statement.setInt(2, classID);
            statement.execute();
        
            showNotifications("Section add","Section add sucessfully");
        }
    }

    @FXML
    private void update(ActionEvent event)throws ClassNotFoundException, SQLException {
        int classID = 0;
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmation Dialog");
        alert.setHeaderText(null);
        alert.setContentText("Are you soure to update section?");
        alert.initModality(Modality.APPLICATION_MODAL);
        Optional<ButtonType> action = alert.showAndWait();
        if (action.get() == ButtonType.OK) {
            connection = DatabaseConnection.getConnection();
            
            sql = "select * from class where name=?";
            statement = connection.prepareStatement(sql);
            statement.setString(1, ClassName.getValue());
            resultSet = statement.executeQuery();
            if (resultSet.next()) {

                classID = resultSet.getInt("id");

            }
            sql = "update sections set name=? , class_id=? where id=?";
            statement = connection.prepareStatement(sql);
            statement.setString(1, ClassName.getValue());
            statement.setInt(2, classID);
            statement.setInt(3, Integer.parseInt(sectionID.getText()));
            statement.execute();
            showNotifications("Section Updation", "Section update sucessfully");
        }
    }

    @FXML
    private void delete(ActionEvent event)throws ClassNotFoundException, SQLException {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmation Dialog");
        alert.setHeaderText(null);
        alert.setContentText("Are you soure to delete section?");
        alert.initModality(Modality.APPLICATION_MODAL);
        Optional<ButtonType> action = alert.showAndWait();
        if (action.get() == ButtonType.OK) {
            connection = DatabaseConnection.getConnection();
            sql = "delete from sections where id=?";
            statement = connection.prepareStatement(sql);
            statement.setInt(1, Integer.parseInt(sectionID.getText()));
            statement.execute();
            showNotifications("Section Deletion", "Section delete sucessfully");
        }
    }

    @FXML
    private void view(ActionEvent event) throws ClassNotFoundException, JRException {
        connection = DatabaseConnection.getConnection();

        File file = new File("src/login/superAdmin/standard/sections.jrxml");
        JasperDesign jasperDesign = JRXmlLoader.load("C:\\reports\\sections.jrxml");
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
   private void loadClasses()throws ClassNotFoundException, SQLException {
   
       connection = DatabaseConnection.getConnection();
       sql = "select * from class";
       statement = connection.prepareStatement(sql);
       resultSet=statement.executeQuery();
       ClassName.getItems().clear();
       while(resultSet.next()){
       
           ClassName.getItems().add(resultSet.getString("name"));
       
       }
       
   
   } 
   
    private void loadSection() {
        sectionID.textProperty().addListener((observable, oldValue, newValue) -> {

            try {
                connection = DatabaseConnection.getConnection();
                sql = "select * from sections where id=? ";
                statement = connection.prepareStatement(sql);
                statement.setInt(1, Integer.parseInt(newValue));
                resultSet = statement.executeQuery();
                if (resultSet.next()) {

                    sectionName.setText(resultSet.getString("name"));
                    sql = "select * from class where id=? ";
                    statement = connection.prepareStatement(sql);
                    statement.setInt(1, resultSet.getInt("class_id"));
                    resultSet=statement.executeQuery();
                    if(resultSet.next()){
                    
                        ClassName.setValue(resultSet.getString("name"));
                    }

                } else {

                    sectionName.setText("");
                    ClassName.setValue(null);
                }
            } catch (ClassNotFoundException | SQLException ex) {
                Logger.getLogger(classViewController.class.getName()).log(Level.SEVERE, null, ex);
            }

        });

    }
}
