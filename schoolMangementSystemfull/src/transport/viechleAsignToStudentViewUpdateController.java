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
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.stage.Stage;
import javafx.util.Duration;
import org.controlsfx.control.Notifications;
import student.Registration.formController;


/**
 * FXML Controller class
 *
 * @author khan
 */
public class viechleAsignToStudentViewUpdateController implements Initializable {
    @FXML
    private JFXComboBox<String> comboClass;
    @FXML
    private JFXComboBox<String> comboSection;
    @FXML
    private JFXComboBox<Integer> comboRegNo;
    @FXML
    private JFXComboBox<String> comboViechleNo;

    //************** for database connection***********
    Connection connection = null;
    PreparedStatement statement = null;
    ResultSet resultSet = null;
    String sql = null;
    
    // **************for class and section observable list*******************
    ObservableList<String> classesNames = FXCollections.observableArrayList();
    ObservableList<String> sectionName = FXCollections.observableArrayList();
    ObservableList<Integer> studentRegNo = FXCollections.observableArrayList();
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
        
        try {
           showClasses();
           loadSectionsName();
           loadSectionsName();
           loadSectionsStudentRegNo();
           loadStudent();
           loadViechleNo();
            
        } catch (ClassNotFoundException | SQLException ex) {
            Logger.getLogger(viechleAsignToStudentViewUpdateController.class.getName()).log(Level.SEVERE, null, ex);
        }
          
    } 
    int sectionPrim=0,classPrim=0,viechlePrim=0;
    private void loadStudent() throws ClassNotFoundException, SQLException{
       
       
        connection = DatabaseConnection.getConnection();
        sql = "SELECT * FROM student where reg_id=?";
        statement = connection.prepareStatement(sql);
        statement.setInt(1, viechleAsignToStudentViewController.date.getStuRegNo());
        resultSet = statement.executeQuery();

        if (resultSet.next()) {
            
          sectionPrim=resultSet.getInt("section_id");
          classPrim=resultSet.getInt("class_id");
          viechlePrim=resultSet.getInt("viechle_id");
           
           
        }
        sql = "SELECT * FROM class where id=?";
        statement = connection.prepareStatement(sql);
        statement.setInt(1, classPrim);
        resultSet = statement.executeQuery();

        if (resultSet.next()) {

          comboClass.setValue(resultSet.getString("class.name")) ;

        }
        
        sql = "SELECT * FROM sections where id=?";
        statement = connection.prepareStatement(sql);
        statement.setInt(1, sectionPrim);
        resultSet = statement.executeQuery();

        if (resultSet.next()) {

            comboSection.setValue(resultSet.getString("name"));

        }
        
        sql = "SELECT * FROM vehicle where id=?";
        statement = connection.prepareStatement(sql);
        statement.setInt(1, viechlePrim);
        resultSet = statement.executeQuery();

        if (resultSet.next()) {

            comboViechleNo.setValue(resultSet.getString("viechle_no"));

        }
        
        comboRegNo.setValue(viechleAsignToStudentViewController.date.getStuRegNo());
       
    }

    @FXML
    private void update(ActionEvent event) throws SQLException, ClassNotFoundException {
        int viechle_id=0;
        connection = DatabaseConnection.getConnection();
        sql = "SELECT * FROM vehicle where viechle_no=?";
        statement = connection.prepareStatement(sql);
        statement.setString(1, comboViechleNo.getValue());
        resultSet = statement.executeQuery();

        if (resultSet.next()) {
            viechle_id = resultSet.getInt("id");
        }
        sql = "update student set viechle_id=? where reg_id=?";
        statement = connection.prepareStatement(sql);
        statement.setInt(1, viechle_id);
        statement.setInt(2, comboRegNo.getValue());
        statement.execute();
        showNotifications("Update Dialog","Record update sucessfully");
       
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
    //for loading classes name into combobox

    private void showClasses() throws ClassNotFoundException, SQLException {

        connection = DatabaseConnection.getConnection();
        sql = "select * from class";
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

    private void loadSectionsName() {

        comboClass.valueProperty().addListener((observable, oldValue, newValue) -> {
            String classPrimryKey = null;
            try {
                connection = DatabaseConnection.getConnection();
                sql = "select id from class where name= ?";
                statement = connection.prepareStatement(sql);
                statement.setString(1, comboClass.getValue());
                resultSet = statement.executeQuery();

                if (resultSet.next()) {

                    classPrimryKey = resultSet.getString("id");

                }
                sql = "select * from sections where class_id=?";
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

    private void loadSectionsStudentRegNo() {

        comboSection.valueProperty().addListener((observable, oldValue, newValue) -> {
            int sectionPrimryKey = 0;
            try {
                connection = DatabaseConnection.getConnection();
                sql = "select id from sections where name= ?";
                statement = connection.prepareStatement(sql);
                statement.setString(1, newValue);
                resultSet = statement.executeQuery();

                if (resultSet.next()) {

                    sectionPrimryKey = resultSet.getInt("id");

                }
                sql = "select * from student where section_id=? and leave_status=? and ALUMI_STATUS=?";
                statement = connection.prepareStatement(sql);
                statement.setInt(1, sectionPrimryKey);
                statement.setInt(2, 0);
                statement.setInt(3, 0);
                resultSet = statement.executeQuery();
                studentRegNo.clear();
                while (resultSet.next()) {

                    studentRegNo.add(resultSet.getInt("reg_id"));

                }
         
            } catch (ClassNotFoundException | SQLException ex) {
                Logger.getLogger(formController.class.getName()).log(Level.SEVERE, null, ex);
            }

        });

        comboRegNo.getItems().clear();
        comboRegNo.setItems(studentRegNo);
    }

    private void loadViechleNo() throws SQLException, ClassNotFoundException {
        connection = DatabaseConnection.getConnection();
        sql = "select * from vehicle ";
        statement = connection.prepareStatement(sql);
        resultSet = statement.executeQuery();
        comboViechleNo.getItems().clear();
        while (resultSet.next()) {

            comboViechleNo.getItems().add(resultSet.getString("viechle_no"));

        }

    }

}
