/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package student.Registration;

import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXDatePicker;
import com.jfoenix.controls.JFXRadioButton;
import com.jfoenix.controls.JFXTextArea;
import com.jfoenix.controls.JFXTextField;
import java.io.File;
import java.io.FileInputStream;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import databaseconnection.DatabaseConnection;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Date;
import java.sql.SQLException;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;

import javafx.geometry.Pos;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.stage.Modality;
import javafx.util.Duration;
import org.controlsfx.control.Notifications;

/**
 * FXML Controller class
 *
 * @author khan
 */
public class formController implements Initializable {
    @FXML
    private ImageView studentImage;
    @FXML
    private JFXTextField txfRegNo;
    @FXML
    private JFXTextField txfStuName;
    @FXML
    private JFXRadioButton radioMale;
    @FXML
    private RadioButton radioFemale;
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
    private JFXComboBox<String> comboClass;
    @FXML
    private JFXComboBox<String> comboSection;

    
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
    
    // **************for class and section observable list*******************
    
    ObservableList<String> classesNames = FXCollections.observableArrayList();
    ObservableList<String> sectionName = FXCollections.observableArrayList();

    
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        //group radio button
        group = new ToggleGroup();
        radioMale.setToggleGroup(group);
        radioMale.setSelected(true);
        radioFemale.setToggleGroup(group);
        try {
            //loading classes into combobox
            showClasses();
            // loading respective sections of the class
            loadSectionsName();
            // loading register student data
             loadeStudent() ;
             loadeStudent() ;
             loadeStudent() ;
            
            
        } catch (ClassNotFoundException | SQLException ex) {
            Logger.getLogger(formController.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        
    }    

    @FXML
    private void saveStudent(ActionEvent event) throws FileNotFoundException, ClassNotFoundException, SQLException {
        String classPrimaryKey = null, sectionPrimaryKey = null;
         
        if(txfRegNo==null || txfRegNo.getText().isEmpty()){
        txfRegNo.requestFocus();
        return;
        }
      
        if (txfStuName == null || txfStuName.getText().isEmpty()) {
            txfStuName.requestFocus();
            return;
        }
       
        if (txfFatherName == null || txfFatherName.getText().isEmpty()) {
            txfFatherName.requestFocus();
            return;
        }
        if(txfDateBith==null){
        txfDateBith.requestFocus();
        return;
        }
        if (txfPhone == null || txfPhone.getText().isEmpty()) {
            txfPhone.requestFocus();
            return;
        }
        if (txfEmail == null || txfEmail.getText().isEmpty()) {
            txfEmail.requestFocus();
            return;
        }
        if (txfAddress == null || txfAddress.getText().isEmpty()) {
            txfAddress.requestFocus();
            return;
        } 
         if(comboClass==null){
        comboClass.requestFocus();
        return;
        }
         if (comboSection == null) {
            comboSection.requestFocus();
            return;
        }
        //get the selected button
        RadioButton selectbtn = (RadioButton) group.getSelectedToggle();
        connection = DatabaseConnection.getConnection();
        sql = "select * from class where name=?";
        statement = connection.prepareStatement(sql);
        statement.setString(1, comboClass.getValue());
        resultSet = statement.executeQuery();
       
        if (resultSet.next()) {
           classPrimaryKey=resultSet.getString("id");

        }
        sql = "select * from sections where name=?";
        statement = connection.prepareStatement(sql);
        statement.setString(1, comboSection.getValue());
        resultSet = statement.executeQuery();

        if (resultSet.next()) {
            sectionPrimaryKey = resultSet.getString("id");

        } 
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmation Dialog");
        alert.setHeaderText(null);
        alert.setContentText("Are you soure to save student record?");
        alert.initModality(Modality.APPLICATION_MODAL);
        Optional<ButtonType> action = alert.showAndWait();
        if (action.get() == ButtonType.OK) {
            if(file==null) {
               
                sql = "insert into student (reg_id,name,father_name,date_brith ,gender,phone,email,address,photo,leave_status,ALUMI_STATUS,section_id,class_id) values(?,?,?,?,?,?,?,?,?,?,?,?,?)";
                statement = connection.prepareStatement(sql);
                statement.setInt(1, Integer.parseInt(txfRegNo.getText()));
                statement.setString(2, txfStuName.getText());
                statement.setString(3, txfFatherName.getText());
                statement.setDate(4, Date.valueOf(txfDateBith.getValue()));
                statement.setString(5, selectbtn.getText());
                statement.setString(6, txfPhone.getText());
                statement.setString(7, txfEmail.getText());
                statement.setString(8, txfAddress.getText());

                statement.setBinaryStream(9, null);
                statement.setInt(10, 0);
                statement.setInt(11, 0);
                statement.setString(12, sectionPrimaryKey);
                statement.setString(13, classPrimaryKey);
                statement.execute();
                showNotifications("Student record", "Student Add Successfully");
                clearFields();
            }else{
                // to get image 

                imageInputStream = new FileInputStream(file);
                sql = "insert into student (reg_id,name,father_name,date_brith ,gender,phone,email,address,photo,leave_status,ALUMI_STATUS,section_id,class_id)values(?,?,?,?,?,?,?,?,?,?,?,?,?)";
                statement = connection.prepareStatement(sql);
                statement.setInt(1, Integer.parseInt(txfRegNo.getText()));
                statement.setString(2, txfStuName.getText());
                statement.setString(3, txfFatherName.getText());
                statement.setDate(4, Date.valueOf(txfDateBith.getValue()));
                statement.setString(5, selectbtn.getText());
                statement.setString(6, txfPhone.getText());
                statement.setString(7, txfEmail.getText());
                statement.setString(8, txfAddress.getText());

                statement.setBinaryStream(9, imageInputStream, file.length());
                statement.setInt(10, 0);
                statement.setInt(11, 0);
                statement.setString(12, sectionPrimaryKey);
                statement.setString(13, classPrimaryKey);
                statement.execute();
                showNotifications("Student record", "Student Add Successfully");
                clearFields();
            
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
    private void clearFields(){
    
        txfRegNo.setText("");
        txfStuName.setText("");
        txfFatherName.setText("");
        txfPhone.setText("");
        txfEmail.setText("");
        txfAddress.setText("");
        imageInputStream = null;
        txfDateBith.setValue(null);
        studentImage.setImage(null);
        comboClass.setValue("");
        comboSection.setValue("");
    
    }
    @FXML
    private void selectImage(ActionEvent event) {
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
    //for loading classes name into combobox
    private void showClasses() throws ClassNotFoundException, SQLException{
       
        
        connection=DatabaseConnection.getConnection();
        sql="select * from class";
        statement = connection.prepareStatement(sql);
        
        resultSet = statement.executeQuery();
         classesNames.clear();
        while (resultSet.next()) {
            classesNames.add(resultSet.getString("name"));
            
        }
        comboClass.getItems().clear();
        comboClass.setItems(classesNames);
    
    }
    // this fuction is used to load respective sections of class
    private void loadSectionsName(){
     
        comboClass.valueProperty().addListener((observable, oldValue, newValue) -> {
        String classPrimryKey=null;
            try {
                connection = DatabaseConnection.getConnection();
                sql = "select id from class where name= ?";
                statement = connection.prepareStatement(sql);
                statement.setString(1, newValue);
                resultSet=statement.executeQuery();
               
                if(resultSet.next()){
                
                classPrimryKey=resultSet.getString("id");
                
                }
                sql="select * from sections where class_id=?";
                statement = connection.prepareStatement(sql);
                statement.setString(1, classPrimryKey);
                resultSet = statement.executeQuery();
                sectionName.clear();
                while (resultSet.next()) {

                  sectionName.add(resultSet.getString("name"));

                }
                        
                
                
                
            } catch (ClassNotFoundException | SQLException ex) {
                Logger.getLogger(formController.class.getName()).log(Level.SEVERE, null, ex);
            }
       
        
        });
        
    comboSection.getItems().clear();
    comboSection.setItems(sectionName);
    }
    // this function loads student when it register already with register id
    private void loadeStudent() throws ClassNotFoundException, SQLException{
    
       
      //  statement.setString(1, newValue);
        
        txfRegNo.textProperty().addListener((observable, oldValue, newValue) -> {
        
            try {
                connection = DatabaseConnection.getConnection();
                sql = "select *  from student  where reg_id= ?";
                statement = connection.prepareStatement(sql);
                statement.setInt(1,Integer.parseInt(newValue));
                resultSet= statement.executeQuery();
                InputStream ism = null;
                File file = new File("sample_image.jpg");
                FileOutputStream fos = new FileOutputStream(file);
                if(resultSet.next()){
                    
                    byte content[] = new byte[1024];
                    int size = 0;
                    ism = resultSet.getBinaryStream("photo");
                    if(ism !=null){
                        while ((size = ism.read(content)) != -1) {
                            fos.write(content, 0, size);
                        }
                        fos.close();
                        ism.close();
                        Image image = new Image(file.toURI().toString());
                        studentImage.setImage(image);
                       }
                    txfStuName.setText(resultSet.getString("name"));
                    txfFatherName.setText(resultSet.getString("father_name"));
                    txfPhone.setText(resultSet.getString("phone"));
                    txfEmail.setText(resultSet.getString("email"));
                    txfAddress.setText(resultSet.getString("address"));
                    txfDateBith.setValue(resultSet.getDate("date_brith").toLocalDate());
                    if(resultSet.getString("gender").equals("Male")){
                    radioMale.setSelected(true);
                    }else{
                    radioFemale.setSelected(true);
                    }
                    int classNameid=0,sectionNameid=0;
                    classNameid=resultSet.getInt("class_id");
                    sectionNameid=resultSet.getInt("section_id");
                    sql="select name from class where id=? ";
                    statement=connection.prepareStatement(sql);
                    statement.setInt(1,classNameid);
                    resultSet=statement.executeQuery();
                    if(resultSet.next()){
                    
                    comboClass.setValue(resultSet.getString("name"));
                    }
                    sql = "select name from sections where id=? ";
                    statement = connection.prepareStatement(sql);
                    statement.setInt(1, sectionNameid);
                    resultSet = statement.executeQuery();
                    if (resultSet.next()) {

                        comboSection.setValue(resultSet.getString("name"));
                    }
                    
                
                }else{
                    txfStuName.setText("");
                    txfFatherName.setText("");
                    txfPhone.setText("");
                    txfEmail.setText("");
                    txfAddress.setText("");
                    imageInputStream = null;
                    txfDateBith.setValue(null);
                    studentImage.setImage(null);
                    comboClass.setValue("");
                    comboSection.setValue("");
                
                }
                
            } catch (ClassNotFoundException | SQLException ex) {
                Logger.getLogger(formController.class.getName()).log(Level.SEVERE, null, ex);
            } catch (IOException ex) {
                Logger.getLogger(formController.class.getName()).log(Level.SEVERE, null, ex);
            }
          
        
        
        });
        
    }
    
}
