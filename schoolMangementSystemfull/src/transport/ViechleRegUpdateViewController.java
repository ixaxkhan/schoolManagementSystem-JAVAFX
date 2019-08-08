/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package transport;

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
public class ViechleRegUpdateViewController implements Initializable {
    @FXML
    private JFXTextField txfViechleName;
    @FXML
    private JFXTextField txfViechleNo;
    @FXML
    private JFXTextField txfCapacity;
    @FXML
    private JFXComboBox<String> comboRouteName;

    //************** for database connection***********
    Connection connection = null;
    PreparedStatement statement = null;
    ResultSet resultSet = null;
    String sql = null;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
       txfViechleName.setText(ViechleRegViewController.date.getName());
       txfViechleNo.setText(ViechleRegViewController.date.getNo());
       txfCapacity.setText(""+ViechleRegViewController.date.getCapacity());
       comboRouteName.setValue(ViechleRegViewController.date.getRouteName());
       
        try {
            loadRoutes();
        } catch (SQLException | ClassNotFoundException ex) {
            Logger.getLogger(ViechleRegUpdateViewController.class.getName()).log(Level.SEVERE, null, ex);
        }
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
    private void update(ActionEvent event) throws ClassNotFoundException, SQLException {
        int routeID = 0;
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmation Dialog");
        alert.setHeaderText(null);
        alert.setContentText("Are you soure to update viechle record?");
        alert.initModality(Modality.APPLICATION_MODAL);
        Optional<ButtonType> action = alert.showAndWait();
        if (action.get() == ButtonType.OK) {
            connection = DatabaseConnection.getConnection();
            sql = "select id from routs where name=?";
            statement = connection.prepareStatement(sql);
            statement.setString(1, comboRouteName.getValue());
            resultSet = statement.executeQuery();
            if (resultSet.next()) {

                routeID = resultSet.getInt("id");
            }

            sql="update vehicle set name=?,viechle_no=?,total_seats=?,route_id=? where id=?";
            statement = connection.prepareStatement(sql);
            statement.setString(1, txfViechleName.getText());
            statement.setString(2, txfViechleNo.getText());
            statement.setInt(3, Integer.parseInt(txfCapacity.getText()));
            statement.setInt(4, routeID);
            statement.setInt(5, ViechleRegViewController.date.getID());
            statement.execute();
            showNotifications("Viechle Updation","Viechle date update sucessfully");
        }
    }

    @FXML
    private void closeWindow(ActionEvent event) {
        
        final Node source = (Node) event.getSource();
        Stage stage = (Stage) source.getScene().getWindow();
        stage.close();
    }
    private void loadRoutes() throws SQLException, ClassNotFoundException {

        connection = DatabaseConnection.getConnection();
        sql = " SELECT * FROM routs ";
        statement = connection.prepareStatement(sql);
        resultSet = statement.executeQuery();
        comboRouteName.getItems().clear();
        while (resultSet.next()) {
            comboRouteName.getItems().add(resultSet.getString("name"));

        }
    } 
}
