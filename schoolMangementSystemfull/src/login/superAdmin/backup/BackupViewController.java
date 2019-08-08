/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package login.superAdmin.backup;

import databaseconnection.DatabaseConnection;
import java.io.File;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextField;
import javafx.stage.DirectoryChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Duration;
import org.controlsfx.control.Notifications;

/**
 * FXML Controller class
 *
 * @author khan
 */
public class BackupViewController implements Initializable {
    @FXML
    private TextField txfDirectory;
    @FXML
    private TextField txfBackupName;

    Connection connection = null;
    PreparedStatement statement = null;
    ResultSet resultSet = null;

    String sql = null;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    

    @FXML
    private void backup(ActionEvent event)throws ClassNotFoundException, SQLException {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmation Dialog");
        alert.setHeaderText(null);
        alert.setContentText("Are you soure to take backup?");
        alert.initModality(Modality.APPLICATION_MODAL);
        Optional<ButtonType> action = alert.showAndWait();
        if (action.get() == ButtonType.OK) {
            txfDirectory.setText(txfDirectory.getText() + "/" + txfBackupName.getText() + ".zip");
            connection = DatabaseConnection.getConnection();
            sql = "BACKUP TO " + "'" + txfDirectory.getText() + "'";
            statement = connection.prepareStatement(sql);
            statement.execute();
            showNotifications("System Backup", "Backup complete sucessuffully");
            

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
    
    File file2;
    @FXML
    private void browse(ActionEvent event) {
        DirectoryChooser directory = new DirectoryChooser();
        file2 = directory.showDialog(new Stage());
        txfDirectory.setText(file2.getAbsolutePath());
    }

    @FXML
    private void closeWindow(ActionEvent event) {
         final Node source = (Node) event.getSource();
        Stage stage = (Stage) source.getScene().getWindow();
        stage.close();
    }
    
}
