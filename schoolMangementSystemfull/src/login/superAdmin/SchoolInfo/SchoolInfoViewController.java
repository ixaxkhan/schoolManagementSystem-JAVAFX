/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package login.superAdmin.SchoolInfo;

import com.jfoenix.controls.JFXTextArea;
import com.jfoenix.controls.JFXTextField;
import databaseconnection.DatabaseConnection;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
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
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Duration;
import org.controlsfx.control.Notifications;


public class SchoolInfoViewController implements Initializable {
    @FXML
    private JFXTextField txfSchoolName;
    @FXML
    private JFXTextField txfPhone;
    @FXML
    private JFXTextArea txfSchoolAddress;
    @FXML
    private ImageView schoolLogo;

    //for image selection variables
    final FileChooser fileChooser = new FileChooser();
    FileInputStream imageInputStream = null;
    File file;
    //************** for database connection***********
    Connection connection = null;
    PreparedStatement statement = null;
    ResultSet resultSet = null;
    String sql = null;
    
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        try {
            loadSchoolInfo();
        } catch (ClassNotFoundException | SQLException | IOException ex) {
            Logger.getLogger(SchoolInfoViewController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }    

    @FXML
    private void imageLoade(ActionEvent event) {
        Stage stage = new Stage();
        FileChooser.ExtensionFilter extFilterJPG = new FileChooser.ExtensionFilter("JPG files (*.jpg)", "*.JPG");
        FileChooser.ExtensionFilter extFilterPNG = new FileChooser.ExtensionFilter("PNG files (*.png)", "*.PNG");
        fileChooser.getExtensionFilters().addAll(extFilterJPG, extFilterPNG);

        //Show open file dialog
        file = fileChooser.showOpenDialog(stage);

        if (file != null) {

            Image image = new Image(file.toURI().toString());
            schoolLogo.setImage(image);

        }

    }

    @FXML
    private void update(ActionEvent event) throws FileNotFoundException, ClassNotFoundException, SQLException {
        
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmation Dialog");
        alert.setHeaderText(null);
        alert.setContentText("Are you soure to update school information?");
        alert.initModality(Modality.APPLICATION_MODAL);
        Optional<ButtonType> action = alert.showAndWait();
        if (action.get() == ButtonType.OK) {
            if (file != null) {
                imageInputStream = new FileInputStream(file);
                connection = DatabaseConnection.getConnection();
                sql = "update shool_info set name=?,phone=?,address=?,logo=? where id=?";
                statement = connection.prepareStatement(sql);
                statement.setString(1, txfSchoolName.getText());
                statement.setString(2, txfPhone.getText());
                statement.setString(3, txfSchoolAddress.getText());
                statement.setBinaryStream(4, imageInputStream,file.length());
                statement.setInt(5, 1);
                statement.execute();
                showNotifications("School Info Updation ","School info update sucessfully");
            }else{
            
                
                connection = DatabaseConnection.getConnection();
                sql = "update shool_info set name=?,phone=?,address=? where id=?";
                statement = connection.prepareStatement(sql);
                statement.setString(1, txfSchoolName.getText());
                statement.setString(2, txfPhone.getText());
                statement.setString(3, txfSchoolAddress.getText());
               
                statement.setInt(4, 1);
                statement.execute();
                showNotifications("School Info Updation ","School info update sucessfully");
            
            }
        
        
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
    private void view(ActionEvent event) {
    }

    @FXML
    private void closeWindow(ActionEvent event) {
        final Node source = (Node) event.getSource();
        Stage stage = (Stage) source.getScene().getWindow();
        stage.close();
    }
    private void loadSchoolInfo() throws ClassNotFoundException, SQLException, IOException{
    
        connection = DatabaseConnection.getConnection();
        sql = "select * from shool_info ";
        statement = connection.prepareStatement(sql);
        resultSet=statement.executeQuery();
        if(resultSet.next())
        {
        
            InputStream ism = null;
            File file = new File("sample_image.jpg");
            FileOutputStream fos = new FileOutputStream(file);

            byte content[] = new byte[1024];
            int size = 0;
            ism = resultSet.getBinaryStream("logo");
            while ((size = ism.read(content)) != -1) {
                fos.write(content, 0, size);
            }
            fos.close();
            ism.close();
            Image image = new Image(file.toURI().toString());
            schoolLogo.setImage(image);
            txfSchoolName.setText(resultSet.getString("name"));
            txfPhone.setText(resultSet.getString("phone"));
            txfSchoolAddress.setText(resultSet.getString("address"));

        
        }
        
    
    
    }
    
}
