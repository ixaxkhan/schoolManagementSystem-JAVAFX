/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package transport;

import com.jfoenix.controls.JFXComboBox;
import databaseconnection.DatabaseConnection;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.stage.Stage;
import javafx.util.Duration;
import org.controlsfx.control.Notifications;


public class ViechleAsignToEmployeeViewUpdateController implements Initializable {
    @FXML
    private JFXComboBox<String> comboEmpName;
    @FXML
    private JFXComboBox<String> comboViechleNo;

    //************** for database connection***********
    Connection connection = null;
    PreparedStatement statement = null;
    ResultSet resultSet = null;
    String sql = null;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        comboViechleNo.setValue(ViechleAsignToEmployeeViewController.date.getViechleNo());
        comboEmpName.setValue(ViechleAsignToEmployeeViewController.date.getEmpName());
        try {
            loadViechleNo();
            loadEmployeeName();
        } catch (SQLException | ClassNotFoundException ex) {
            Logger.getLogger(ViechleAsignToEmployeeViewUpdateController.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }    

    @FXML
    private void update(ActionEvent event)throws SQLException, ClassNotFoundException {
        int employeePrim = 0, viechlePrim = 0, empViechleID = 0;
        sql = "select * from vehicle where viechle_no=?";
        statement = connection.prepareStatement(sql);
        statement.setString(1, comboViechleNo.getValue());
        resultSet = statement.executeQuery();

        if (resultSet.next()) {

            viechlePrim = resultSet.getInt("id");
        }
       
        sql="update employee set viechle_id=? where reg_no=?";
        statement = connection.prepareStatement(sql);
        statement.setInt(1, viechlePrim);
        statement.setInt(2, ViechleAsignToEmployeeViewController.date.getID());
        statement.execute();
         showNotifications("Employee Assign Updation","Employee update sucessfully");
        
        
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
    private void closeWindow(ActionEvent event) {
        final Node source = (Node) event.getSource();
        Stage stage = (Stage) source.getScene().getWindow();
        stage.close();
    }
    private void loadViechleNo() throws SQLException, ClassNotFoundException {
        connection = DatabaseConnection.getConnection();
        sql = "SELECT * FROM vehicle";
        statement = connection.prepareStatement(sql);
        resultSet = statement.executeQuery();

        while (resultSet.next()) {

            comboViechleNo.getItems().addAll(resultSet.getString("viechle_no"));

        }

    }

    private void loadEmployeeName() throws SQLException, ClassNotFoundException {

        connection = DatabaseConnection.getConnection();
        sql = "SELECT * FROM employee where designation=? or designation=? and leave_status=? ";
        statement = connection.prepareStatement(sql);
        statement.setString(1, "Driver");
        statement.setString(2, "Conductor");
        statement.setInt(3, 0);

        resultSet = statement.executeQuery();

        while (resultSet.next()) {

            comboEmpName.getItems().addAll(resultSet.getString("name"));

        }

    } 
}
