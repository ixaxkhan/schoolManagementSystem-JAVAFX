/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package login.superAdmin;

import com.jfoenix.controls.JFXPasswordField;
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
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.xml.JRXmlLoader;
import net.sf.jasperreports.view.JasperViewer;
import org.controlsfx.control.Notifications;


public class ProfileViewController implements Initializable {
    @FXML
    private ImageView adminImage;
    @FXML
    private JFXTextField name;
    @FXML
    private JFXTextField currentUserName;
    @FXML
    private JFXPasswordField currentPassword;
    @FXML
    private JFXTextField newUserName;
    //for image selection variables
    final FileChooser fileChooser = new FileChooser();
    FileInputStream imageInputStream = null;
    File file;
    //************** for database connection***********
    Connection connection = null;
    PreparedStatement statement = null;
    ResultSet resultSet = null;
    String sql = null;
    @FXML
    private JFXPasswordField newPassword;
    
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        try {
            loadAdmin();
        } catch (ClassNotFoundException | SQLException | IOException ex) {
            Logger.getLogger(ProfileViewController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }    

    @FXML
    private void update(ActionEvent event) throws ClassNotFoundException, SQLException, FileNotFoundException {
        if (name.getText() == null || name.getText().equals("")) {

            name.requestFocus();
            return;

        }
           if(currentUserName.getText()==null || currentUserName.getText().equals("")){
           
           currentUserName.requestFocus();
           return ;
           
           }
        if (currentPassword.getText() == null || currentPassword.getText().equals("")) {

            currentPassword.requestFocus();
            return;

        }
        if (newUserName.getText() == null || newUserName.getText().equals("")) {

            newUserName.requestFocus();
            return;

        }
        if (newPassword.getText() == null || newPassword.getText().equals("")) {

            newPassword.requestFocus();
            return;

        }
       
            connection = DatabaseConnection.getConnection();
            sql="select * from super_admin";
            statement = connection.prepareStatement(sql);
            resultSet=statement.executeQuery();
            if(resultSet.next()){
            
            if(resultSet.getString("user_name").equals(currentUserName.getText()) && resultSet.getString("password").equals(currentPassword.getText())){
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setTitle("Confirmation Dialog");
                alert.setHeaderText(null);
                alert.setContentText("Are you soure to update profile?");
                alert.initModality(Modality.APPLICATION_MODAL);
                Optional<ButtonType> action = alert.showAndWait();
                if (action.get() == ButtonType.OK) {
                    if (file != null) {
                        imageInputStream = new FileInputStream(file);
                        sql = "update super_admin set name=? ,password=?,user_name=?,photo=? where id=?";
                        statement = connection.prepareStatement(sql);
                        statement.setString(1, name.getText());
                        statement.setString(2, newUserName.getText());
                        statement.setString(3, newPassword.getText());
                        statement.setBinaryStream(4, imageInputStream,file.length());
                        statement.setInt(5, 1);
                       
                        statement.execute();
                        showNotifications("Profile updation", "Profile update sucessfully");

                    }else{
                    
                        sql = "update super_admin set name=? ,password=?,user_name=? where id=?";
                        statement = connection.prepareStatement(sql);
                        statement.setString(1, name.getText());
                        statement.setString(2, newUserName.getText());
                        statement.setString(3, newPassword.getText());
                       
                        statement.setInt(4, 1);

                        statement.execute();
                        showNotifications("Profile updation", "Profile update sucessfully");
                    
                    
                    }
                }
            
            }else{
            
            
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Confirmation Dialog");
                alert.setHeaderText(null);
                alert.setContentText("Wrong user name or password !");
                alert.initModality(Modality.APPLICATION_MODAL);
                alert.show();
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
    private void view(ActionEvent event) throws ClassNotFoundException, JRException {
        
        connection = DatabaseConnection.getConnection();
        File file = new File("src/login/superAdmin/superAdminView.jrxml");
        JasperDesign jasperDesign = JRXmlLoader.load("C:\\reports\\superAdminView.jrxml");
        JasperReport jasperReport = JasperCompileManager.compileReport(jasperDesign);
        JasperPrint JasperPrint = (JasperPrint) JasperFillManager.fillReport(jasperReport, null, connection);
        JasperViewer jv = new JasperViewer(JasperPrint, false);
        JasperViewer.viewReport(JasperPrint, false);
    }

    @FXML
    private void loadImage(ActionEvent event) {
        Stage stage = new Stage();
        FileChooser.ExtensionFilter extFilterJPG = new FileChooser.ExtensionFilter("JPG files (*.jpg)", "*.JPG");
        FileChooser.ExtensionFilter extFilterPNG = new FileChooser.ExtensionFilter("PNG files (*.png)", "*.PNG");
        fileChooser.getExtensionFilters().addAll(extFilterJPG, extFilterPNG);

        //Show open file dialog
        file = fileChooser.showOpenDialog(stage);

        if (file != null) {

            Image image = new Image(file.toURI().toString());
            adminImage.setImage(image);

        }
    }

    @FXML
    private void closeWindow(ActionEvent event) {
        final Node source = (Node) event.getSource();
        Stage stage = (Stage) source.getScene().getWindow();
        stage.close();
    }
    private void loadAdmin() throws ClassNotFoundException, SQLException, FileNotFoundException, IOException{
    
        connection = DatabaseConnection.getConnection();
        sql = "select * from super_admin";
        statement = connection.prepareStatement(sql);
        resultSet=statement.executeQuery();
        if(resultSet.next()){
        
            name.setText(resultSet.getString("name"));
            InputStream ism = null;
            File file = new File("sample_image.jpg");
            FileOutputStream fos = new FileOutputStream(file);

            byte content[] = new byte[1024];
            int size = 0;
            ism = resultSet.getBinaryStream("photo");
            if (ism != null) {
                while ((size = ism.read(content)) != -1) {
                    fos.write(content, 0, size);
                }
                fos.close();
                ism.close();
                Image image = new Image(file.toURI().toString());
                adminImage.setImage(image);

            }
        }
    }
    
}
