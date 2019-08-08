/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package employee;

import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXDatePicker;
import com.jfoenix.controls.JFXRadioButton;
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
import java.sql.Date;
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
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Duration;
import org.controlsfx.control.Notifications;
import student.Registration.formController;

/**
 * FXML Controller class
 *
 * @author khan
 */
public class employeeViewUpdateController implements Initializable {
    @FXML
    private ImageView studentImage;
    @FXML
    private JFXTextField txfRegNo;
    @FXML
    private JFXTextField txfName;
    @FXML
    private JFXRadioButton radioMale;
    @FXML
    private JFXRadioButton radioFemale;
    @FXML
    private JFXTextField txfFatherName;
    @FXML
    private JFXDatePicker txfDateBith;
    @FXML
    private JFXTextField txfPhone;
    @FXML
    private JFXTextField txfEmail;
    @FXML
    private JFXTextArea txfAddress;
    @FXML
    private JFXComboBox<String> comboDesignation;
    @FXML
    private JFXTextField txfSalary;
    @FXML
    private JFXTextArea txfExperience;
    @FXML
    private JFXTextArea txfQualification;
//for image selection variables
    final FileChooser fileChooser = new FileChooser();
    FileInputStream imageInputStream = null;
    File file;
    //************** for database connection***********
    Connection connection = null;
    PreparedStatement statement = null;
    ResultSet resultSet = null;
    String sql = null;
    //************** for radio button***********
    ToggleGroup group;
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        //group radio button
        group = new ToggleGroup();
        radioMale.setToggleGroup(group);
        radioMale.setSelected(true);
        radioFemale.setToggleGroup(group);
        comboDesignation.getItems().addAll("Principle", "Teacher", "cleark", "Naib Qasid");
        loadeRegisterEmployee();
        txfRegNo.setText(""+jjjController.data.getRegNo());
    
    }    

    @FXML
    private void uploadImage(ActionEvent event) {
        Stage stage = new Stage();
        FileChooser.ExtensionFilter extFilterJPG = new FileChooser.ExtensionFilter("JPG files (*.jpg)", "*.JPG");
        FileChooser.ExtensionFilter extFilterPNG = new FileChooser.ExtensionFilter("PNG files (*.png)", "*.PNG");
        fileChooser.getExtensionFilters().addAll(extFilterJPG, extFilterPNG);

        //Show open file dialog
        file = fileChooser.showOpenDialog(stage);

        if (file != null) {

            Image image = new Image(file.toURI().toString());
            studentImage.setImage(image);

        }
    }

    @FXML
    private void saveEmployee(ActionEvent event) throws ClassNotFoundException, SQLException, FileNotFoundException {
   
        connection = DatabaseConnection.getConnection();
         Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmation Dialog");
        alert.setHeaderText(null);
        alert.setContentText("Are you soure to save student record?");
        alert.initModality(Modality.APPLICATION_MODAL);
        Optional<ButtonType> action = alert.showAndWait();
        if (action.get() == ButtonType.OK) {
               if(file!=null){
                   imageInputStream = new FileInputStream(file);
                   sql = "update employee set name=? ,address=?,phone=?,email=?,experience=?,qualification=?,join_date=?,photo=?,gender=? ,salary=?,father_name=?,designation=? where reg_no=?";
                   statement = connection.prepareStatement(sql);
                   statement.setString(1, txfName.getText());
                   statement.setString(2, txfAddress.getText());
                   statement.setString(3, txfPhone.getText());
                   statement.setString(4, txfEmail.getText());
                   statement.setString(5, txfExperience.getText());
                   statement.setString(6, txfQualification.getText());
                   statement.setDate(7, Date.valueOf(txfDateBith.getValue()));

                   statement.setBinaryStream(8, imageInputStream, file.length());
                   RadioButton selectButton = (RadioButton) group.getSelectedToggle();
                   statement.setString(9, selectButton.getText());
                   statement.setDouble(10, Double.parseDouble(txfSalary.getText()));
                   statement.setString(11, txfFatherName.getText());
                   statement.setString(12, comboDesignation.getValue());
                   statement.setString(13, txfRegNo.getText());
                   statement.execute();
                   showNotifications("Employee Update","Employee update sucessfully");
               }else{
               
                  
                   sql = "update employee set name=? ,address=?,phone=?,email=?,experience=?,qualification=?,join_date=?,gender=? ,salary=?,father_name=?,designation=? where reg_no=?";
                   statement = connection.prepareStatement(sql);
                   statement.setString(1, txfName.getText());
                   statement.setString(2, txfAddress.getText());
                   statement.setString(3, txfPhone.getText());
                   statement.setString(4, txfEmail.getText());
                   statement.setString(5, txfExperience.getText());
                   statement.setString(6, txfQualification.getText());
                   statement.setDate(7, Date.valueOf(txfDateBith.getValue()));

                   RadioButton selectButton = (RadioButton) group.getSelectedToggle();
                   statement.setString(8, selectButton.getText());
                   statement.setDouble(9, Double.parseDouble(txfSalary.getText()));
                   statement.setString(10, txfFatherName.getText());
                   statement.setString(11, comboDesignation.getValue());
                   statement.setString(12, txfRegNo.getText());
                   statement.execute();
                   showNotifications("Employee Update","Employee update sucessfully");
               
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
    private void closeWindow(ActionEvent event) {
        
        final Node source = (Node) event.getSource();
        Stage stage = (Stage) source.getScene().getWindow();
        stage.close();
    }
    
    private void loadeRegisterEmployee() {

        txfRegNo.textProperty().addListener((observable, oldValue, newValue) -> {
            try {
                connection = DatabaseConnection.getConnection();
                sql = "select * from employee where leave_status=? and reg_no=?";
                statement = connection.prepareStatement(sql);
                statement.setInt(1, 0);
                statement.setInt(2, Integer.parseInt(newValue));
                resultSet = statement.executeQuery();
                if (resultSet.next()) {

                    txfFatherName.setText(resultSet.getString("father_name"));
                    txfDateBith.setValue(resultSet.getDate("join_date").toLocalDate());
                    txfPhone.setText(resultSet.getString("phone"));
                    txfEmail.setText(resultSet.getString("email"));
                    txfAddress.setText(resultSet.getString("address"));
                    txfName.setText(resultSet.getString("name"));
                    txfQualification.setText(resultSet.getString("qualification"));
                    txfExperience.setText(resultSet.getString("experience"));
                    if (resultSet.getString("gender").equals("Male")) {
                        radioMale.setSelected(true);
                    } else {

                        radioFemale.setSelected(true);

                    }
                    txfSalary.setText("" + resultSet.getDouble("salary"));
                    comboDesignation.setValue(resultSet.getString("designation"));

                    InputStream ism = null;
                    File file = new File("sample_image.jpg");
                    FileOutputStream fos = new FileOutputStream(file);

                    byte content[] = new byte[1024];
                    int size = 0;
                    ism = resultSet.getBinaryStream("photo");
                    while ((size = ism.read(content)) != -1) {
                        fos.write(content, 0, size);
                    }
                    fos.close();
                    ism.close();
                    Image image = new Image(file.toURI().toString());
                    studentImage.setImage(image);

                }
            } catch (ClassNotFoundException | SQLException | IOException ex) {
                Logger.getLogger(formController.class.getName()).log(Level.SEVERE, null, ex);
            }

        });

    }
    
}
