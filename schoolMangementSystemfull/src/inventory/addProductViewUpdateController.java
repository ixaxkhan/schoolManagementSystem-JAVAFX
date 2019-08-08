
package inventory;

import com.jfoenix.controls.JFXDatePicker;
import com.jfoenix.controls.JFXTextField;
import databaseconnection.DatabaseConnection;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Date;

import java.util.Optional;
import java.util.ResourceBundle;
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


public class addProductViewUpdateController implements Initializable {
    @FXML
    private JFXTextField txfName;
    @FXML
    private JFXTextField txfMin;
    @FXML
    private JFXTextField txfMax;
    @FXML
    private JFXDatePicker txfDate;
    @FXML
    private JFXTextField txfDescription;
    //************** for database connection***********
    Connection connection = null;
    PreparedStatement statement = null;
    ResultSet resultSet = null;
    String sql = null;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
       txfName.setText(ProductAddViewController.date.getProductName());
       txfMin.setText(""+ProductAddViewController.date.getTbMin());
       txfMax.setText(""+ProductAddViewController.date.getTbMax());
       txfDate.setValue(ProductAddViewController.date.getDate().toLocalDate());
       txfDescription.setText(ProductAddViewController.date.getDescription());
    }    

    @FXML
    private void update(ActionEvent event) throws ClassNotFoundException, SQLException {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmation Dialog");
        alert.setHeaderText(null);
        alert.setContentText("Are you soure to update product record?");
        alert.initModality(Modality.APPLICATION_MODAL);
        Optional<ButtonType> action = alert.showAndWait();
        if (action.get() == ButtonType.OK) {
        connection = DatabaseConnection.getConnection();
        sql = "update add_product set name=? ,date=?,min=?,mix=?,description=? where id=? ";
        statement = connection.prepareStatement(sql);
        statement.setString(1, txfName.getText());
        statement.setDate(2,Date.valueOf(txfDate.getValue()));
        statement.setInt(3, Integer.parseInt(txfMin.getText()));
        statement.setInt(4, Integer.parseInt(txfMax.getText()));
        statement.setString(5,  txfDescription.getText());
        statement.setInt(6, ProductAddViewController.date.getProductID());
        statement.execute();
        showNotifications("Upation Record","Product update sucessfully");
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
    private void closeWindow(ActionEvent event) {
        final Node source = (Node) event.getSource();
        Stage stage = (Stage) source.getScene().getWindow();
        stage.close();
    }
    
}
