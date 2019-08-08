
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


public class EmployeeRegistrationFormController implements Initializable {
    @FXML
    private ImageView studentImage;
    @FXML
    private JFXTextField txfRegNo;
    @FXML
    private JFXRadioButton radioMale;
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
    private JFXTextField txfName;
    @FXML
    private JFXComboBox<String> comboDesignation;
    @FXML
    private JFXTextField txfSalary;
    @FXML
    private JFXTextArea txfExperience;
    @FXML
    private JFXRadioButton radioFemale;
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
        
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmation Dialog");
        alert.setHeaderText(null);
        alert.setContentText("Are you soure to save student record?");
        alert.initModality(Modality.APPLICATION_MODAL);
        Optional<ButtonType> action = alert.showAndWait();
        if (action.get() == ButtonType.OK) {
            if(file!=null){
                imageInputStream= new  FileInputStream (file);
                connection = DatabaseConnection.getConnection();
                sql = "insert into employee (address,email,experience,join_date,name,phone,photo,qualification,reg_no,leave_status,designation,salary,gender,father_name) values(?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
                statement = connection.prepareStatement(sql);
                statement.setString(1, txfAddress.getText());
                statement.setString(2, txfEmail.getText());
                statement.setString(3, txfExperience.getText());
                statement.setDate(4, Date.valueOf(txfDateBith.getValue()));
                statement.setString(5, txfName.getText());
                statement.setString(6, txfPhone.getText());
                statement.setBinaryStream(7, imageInputStream, file.length());
                statement.setString(8, txfQualification.getText());
                statement.setInt(9, Integer.parseInt(txfRegNo.getText()));
                statement.setInt(10, 0);
                statement.setString(11, comboDesignation.getValue());
                statement.setDouble(12, Double.parseDouble(txfSalary.getText()));
                RadioButton selectButton = (RadioButton) group.getSelectedToggle();
                statement.setString(13, selectButton.getText());
                statement.setString(14,txfFatherName.getText());

                statement.execute();
                showNotifications("Employee add ", "Employee add sucessfully");
            }else{
            
                connection = DatabaseConnection.getConnection();
                sql = "insert into employee (address,email,experience,join_date,name,phone,photo,qualification,reg_no,leave_status,designation,salary,gender,father_name) values(?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
                statement = connection.prepareStatement(sql);
                statement.setString(1, txfAddress.getText());
                statement.setString(2, txfEmail.getText());
                statement.setString(3, txfExperience.getText());
                statement.setDate(4, Date.valueOf(txfDateBith.getValue()));
                statement.setString(5, txfName.getText());
                statement.setString(6, txfPhone.getText());
                statement.setBinaryStream(7, null);
                statement.setString(8, txfQualification.getText());
                statement.setInt(9, Integer.parseInt(txfRegNo.getText()));
                statement.setInt(10, 0);
                statement.setString(11, comboDesignation.getValue());
                statement.setDouble(12, Double.parseDouble(txfSalary.getText()));
                RadioButton selectButton = (RadioButton) group.getSelectedToggle();
                
                statement.setString(13, selectButton.getText());
                statement.setString(14, txfFatherName.getText());

                statement.execute();
                showNotifications("Employee add ", "Employee add sucessfully");
            
            }
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
      
        //group radio button
        group = new ToggleGroup();
        radioMale.setToggleGroup(group);
        radioMale.setSelected(true);
        radioFemale.setToggleGroup(group);
        comboDesignation.getItems().addAll("Principle","Teacher","cleark","Conductor","Driver");
        
        loadeRegisterEmployee();
    
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
    private void loadeRegisterEmployee(){
    
        txfRegNo.textProperty().addListener((observable, oldValue, newValue) -> {
        try{
            connection = DatabaseConnection.getConnection();
            sql = "select * from employee where leave_status=? and reg_no=?";
            statement = connection.prepareStatement(sql);
            statement.setInt(1, 0);
            statement.setInt(2,Integer.parseInt(newValue) );
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
                if(resultSet.getString("gender").equals("Male")){
                 radioMale.setSelected(true);
                }else{
                
                radioFemale.setSelected(true);
                
                }
                txfSalary.setText(""+resultSet.getDouble("salary"));
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
                }catch (ClassNotFoundException | SQLException | IOException ex) {
                Logger.getLogger(formController.class.getName()).log(Level.SEVERE, null, ex);
            }
            
           });
    
    
    
}
    
    
}
