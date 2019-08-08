/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package login.superAdmin.AddNewAdmin;

import com.jfoenix.controls.JFXCheckBox;
import com.jfoenix.controls.JFXTextField;
import databaseconnection.DatabaseConnection;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
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
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Duration;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.xml.JRXmlLoader;
import net.sf.jasperreports.view.JasperViewer;

import org.controlsfx.control.Notifications;

/**
 *
 * @author khan
 */
public class newAdminController implements Initializable{
    
    Connection connection = null;
    PreparedStatement statement = null;
    ResultSet resultSet = null;

    String sql = null;
    //THIS FOR ADMIN GENDER
    ToggleGroup group;
    //********************************* FOR NEW ADMIN ADD AND DELETE AND UPDATE *************************************** 

    @FXML
    private TextField txfAdminName;
    @FXML
    private RadioButton radioAminMale;
    @FXML
    private RadioButton radioAdminFemale;
    @FXML
    private TextArea txfAdminAddress;
    @FXML
    private TextField txfAdminPhone;
    @FXML
    private TextField txfAdminUserName;
    @FXML
    private TextField txfAdminPassword;
    @FXML
    private JFXTextField txfAdminId;
    @FXML
    private JFXCheckBox checkOperation;
    
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
    private void saveAdmin(ActionEvent event) throws ClassNotFoundException, SQLException {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmation Dialog");
        alert.setHeaderText(null);
        alert.setContentText("Are you soure to add new admin?");
        alert.initModality(Modality.APPLICATION_MODAL);
        Optional<ButtonType> action = alert.showAndWait();
        if (action.get() == ButtonType.OK) {
            connection = DatabaseConnection.getConnection();
            sql = "INSERT INTO ADMIN (name,gender,phone,address,user_name,password)VALUES(?,?,?,?,?,?)";
            statement = connection.prepareStatement(sql);
            statement.setString(1, txfAdminName.getText());
            RadioButton selectbtn = (RadioButton) group.getSelectedToggle();
            statement.setString(2, selectbtn.getText());
            statement.setString(3, txfAdminPhone.getText());
            statement.setString(4, txfAdminAddress.getText());
            statement.setString(5, txfAdminUserName.getText());
            statement.setString(6, txfAdminPassword.getText());
            statement.execute();
            showNotifications("Add Admin", "Admin add   sucessuffully");
            clearFieldss();
        }

    }

    @FXML
    private void updateAdmin(ActionEvent event) throws ClassNotFoundException, SQLException {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmation Dialog");
        alert.setHeaderText(null);
        alert.setContentText("Are you soure to update admin?");
        alert.initModality(Modality.APPLICATION_MODAL);
        Optional<ButtonType> action = alert.showAndWait();
        if (action.get() == ButtonType.OK) {
            connection = DatabaseConnection.getConnection();
            sql = "UPDATE  ADMIN SET name=?,gender=?,phone=?,address=?,user_name=?,password=? WHERE id=?";
            statement = connection.prepareStatement(sql);
            statement.setString(1, txfAdminName.getText());
            RadioButton selectbtn = (RadioButton) group.getSelectedToggle();
            statement.setString(2, selectbtn.getText());
            statement.setString(3, txfAdminPhone.getText());
            statement.setString(4, txfAdminAddress.getText());
            statement.setString(5, txfAdminUserName.getText());
            statement.setString(6, txfAdminPassword.getText());
            statement.setString(7, txfAdminId.getText());
            statement.execute();
            showNotifications("Update admin", "Admin update   sucessuffully");
            clearFieldss();
        }

    }

    @FXML
    private void deleteAdmin(ActionEvent event) throws ClassNotFoundException, SQLException {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmation Dialog");
        alert.setHeaderText(null);
        alert.setContentText("Are you soure to delete admin?");
        alert.initModality(Modality.APPLICATION_MODAL);
        Optional<ButtonType> action = alert.showAndWait();
        if (action.get() == ButtonType.OK) {
            connection = DatabaseConnection.getConnection();
            sql = "DELETE FROM ADMIN WHERE id=?";
            statement = connection.prepareStatement(sql);
            statement.setString(1, txfAdminId.getText());
            statement.execute();
            showNotifications("Delete admin", "Admin delete   sucessuffully");
            clearFieldss();
        }
    }

    @FXML
    private void clearAdmin(ActionEvent event) {
        clearFieldss();
    }

    private void clearFieldss() {
        txfAdminName.setText("");
        radioAminMale.setSelected(true);
        txfAdminAddress.setText("");
        txfAdminPhone.setText("");
        txfAdminUserName.setText("");
        txfAdminPassword.setText("");
        txfAdminId.setText("");
        checkOperation.setSelected(false);
        
        

    }

    @FXML
    private void ViewAdmins(ActionEvent event) throws ClassNotFoundException, JRException {
                connection = DatabaseConnection.getConnection();
                Path currentRelativePath = Paths.get("");
                String s = currentRelativePath.toAbsolutePath().toString();
                JasperDesign jasperDesign = JRXmlLoader.load("C:\\reports\\adminList.jrxml");
                JasperReport jasperReport = JasperCompileManager.compileReport(jasperDesign);
                JasperPrint JasperPrint = (JasperPrint)JasperFillManager.fillReport(jasperReport, null, connection);
                JasperViewer jv = new JasperViewer(JasperPrint, false);
                JasperViewer.viewReport(JasperPrint, false);
                DatabaseConnection.closeConnecton();
         
    }

    private void loadAdmins() throws ClassNotFoundException, SQLException {
        connection = DatabaseConnection.getConnection();
        sql = "SELECT *  FROM ADMIN WHERE id=?";
        statement = connection.prepareStatement(sql);
        statement.setString(1, txfAdminId.getText());
        resultSet = statement.executeQuery();
        if (resultSet.next()) {

            txfAdminName.setText(resultSet.getString("name"));
            if (resultSet.getString("gender").equals("Male")) {
                radioAminMale.setSelected(true);
            } else {
                radioAdminFemale.setSelected(true);
            }
            txfAdminAddress.setText(resultSet.getString("address"));
            txfAdminPhone.setText(resultSet.getString("phone"));
            txfAdminUserName.setText(resultSet.getString("user_name"));
            txfAdminPassword.setText(resultSet.getString("password"));

        } else {
            radioAminMale.setSelected(true);
            txfAdminAddress.setText("");
            txfAdminPhone.setText("");
            txfAdminUserName.setText("");
            txfAdminPassword.setText("");
            
        }

    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // THIS IS USED FOR ADMIN GENDER
        group = new ToggleGroup();

        radioAminMale.setToggleGroup(group);
        radioAminMale.setSelected(true);
        radioAdminFemale.setToggleGroup(group);
        txfAdminId.setVisible(false);
        checkOperation.selectedProperty().addListener(new ChangeListener<Boolean>() {
            public void changed(ObservableValue<? extends Boolean> ov,
                    Boolean old_val, Boolean new_val) {
                if (new_val) {
                    txfAdminId.setVisible(true);
                  

                } else {

                    txfAdminId.setVisible(false);
                 
                }
            }
        });
         txfAdminId.textProperty().addListener((observable, oldValue, newValue) -> {
            

                try {
                    loadAdmins();
                } catch (ClassNotFoundException ex) {
                    Logger.getLogger(newAdminController.class.getName()).log(Level.SEVERE, null, ex);
                } catch (SQLException ex) {
                    Logger.getLogger(newAdminController.class.getName()).log(Level.SEVERE, null, ex);
                }  

           
        });
        
    
    }

    private void backToSystemSetting(ActionEvent event) throws IOException {
        loadFXML("/login/superAdmin/superAdminView.fxml", event);
    }
    private void loadFXML(String path, ActionEvent event) throws IOException {

        Parent root;
        root = FXMLLoader.load(getClass().getResource(path));
        final Node source = (Node) event.getSource();
        Stage stage = (Stage) source.getScene().getWindow();

        Scene sceen = new Scene(root);

        stage.setScene(sceen);
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        stage.setWidth(screenSize.getWidth());
        stage.setHeight(screenSize.getHeight() - 30);
        stage.show();

    }

    @FXML
    private void closeWindow(ActionEvent event) {
        final Node source = (Node) event.getSource();
        Stage stage = (Stage) source.getScene().getWindow();
        stage.close();
    }
}
