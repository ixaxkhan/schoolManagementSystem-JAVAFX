/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package login.superAdmin.backup;

import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.TextField;

import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.Duration;
import org.controlsfx.control.Notifications;
import org.h2.tools.Restore;
import databaseconnection.DatabaseConnection;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;


/**
 * FXML Controller class
 *
 * @author khan
 */
public class RestoreViewController implements Initializable {
     Connection connection = null;
    PreparedStatement statement = null;
    ResultSet resultSet = null;
    String sql = null;
    @FXML
    private TextField txfFile;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    

    @FXML
    private void restore(ActionEvent event) throws ClassNotFoundException, SQLException {
        DatabaseConnection.closeConnecton();
       
        Restore.execute(txfFile.getText() ,"C://reports//db//", "schoolManagement.mv");
      
        showNotifications("System Restore", "System restore sucessuffully");
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
        FileChooser directory = new FileChooser();
         Stage stage = new Stage();
        FileChooser.ExtensionFilter extFilterJPG = new FileChooser.ExtensionFilter("ZIP files (*.zip)", "*.ZIP");
        
        directory.getExtensionFilters().addAll(extFilterJPG);

        //Show open file dialog
        file2 = directory.showOpenDialog(stage);
        txfFile.setText(file2.getAbsolutePath());
    }

    @FXML
    private void closeWindow(ActionEvent event) {
        final Node source = (Node) event.getSource();
        Stage stage = (Stage) source.getScene().getWindow();
        stage.close();
    }
    
}
