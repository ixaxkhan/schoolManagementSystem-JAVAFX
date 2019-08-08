/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package account;

import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXDatePicker;
import com.jfoenix.controls.JFXTextField;
import databaseconnection.DatabaseConnection;
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
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Duration;
import org.controlsfx.control.Notifications;
import student.Registration.formController;


public class MonthlyWiseFeeCollectionUpdateViewController implements Initializable {
    @FXML
    private JFXComboBox<String> comboClass;
    @FXML
    private JFXComboBox<String> comboSection;
    @FXML
    private JFXComboBox<Integer> comboRegNo;
    @FXML
    private JFXComboBox<String> comboFeeType;
    @FXML
    private JFXTextField txfFeeAmount;
    @FXML
    private JFXDatePicker txfDate;
    @FXML
    private JFXComboBox<String> comboStatus;
    @FXML
    private JFXTextField txfPaidAmount;
    @FXML
    private Label labRemainAmount;
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
        String className=null,sectionName=null,feeTypeName=null;
        int classid=0,sectionid=0,feetypeid=0;
        txfPaidAmount.setVisible(false);
        labRemainAmount.setVisible(false);
        comboStatus.getItems().clear();
        comboStatus.getItems().addAll("Paid", "Not Full Paid", "Not Paid");
        isNotFullPaid();
        calculateRemainAmount();
        try {
            showClasses();
            loadSectionsName();
            loadSectionsName();
            loadSectionsStudentRegNo();
            loadFeeTypes();
            
        connection = DatabaseConnection.getConnection();
        sql = "select * from STUDENT_FEE where id=?";
        statement = connection.prepareStatement(sql);
        statement.setInt(1,AccountManagementController.date.getToutionSlip() );
        resultSet=statement.executeQuery();
        if(resultSet.next()){
        
        classid=resultSet.getInt("class_id");
        sectionid=resultSet.getInt("section_id");
        feetypeid=resultSet.getInt("FEE_TYPE_ID");
        
        }
        sql="select * from class where id=?";
        statement = connection.prepareStatement(sql);
        statement.setInt(1, classid);
        resultSet=statement.executeQuery();
        if(resultSet.next()){
        className=resultSet.getString("name");
        
        }
            
            sql = "select * from sections where id=?";
            statement = connection.prepareStatement(sql);
            statement.setInt(1, sectionid);
            resultSet = statement.executeQuery();
            if (resultSet.next()) {
                sectionName = resultSet.getString("name");

            }    
            sql = "select * from FEE_TYPE where id=?";
            statement = connection.prepareStatement(sql);
            statement.setInt(1, feetypeid);
            resultSet = statement.executeQuery();
            if (resultSet.next()) {
               feeTypeName = resultSet.getString("FEE_CATEGORY");

            }

           comboClass.setValue(className) ;
           comboSection.setValue(sectionName);
           comboFeeType.setValue(feeTypeName);
           txfFeeAmount.setText(""+AccountManagementController.date.getToutionTotalFee());
           txfDate.setValue(AccountManagementController.date.getToutionDate().toLocalDate());
           comboStatus.setValue(AccountManagementController.date.getToutionStatus());
           txfPaidAmount.setText(""+AccountManagementController.date.getToutionPaidFee());
           labRemainAmount.setText(""+AccountManagementController.date.getToutionDues());
           comboRegNo.setValue(AccountManagementController.date.getToutionRegNo());
        } catch (ClassNotFoundException | SQLException ex) {
            Logger.getLogger(MonthlyWiseFeeCollectionViewController.class.getName()).log(Level.SEVERE, null, ex);
        }
       
       
    }    
    private void isNotFullPaid() {

        comboStatus.valueProperty().addListener((observable, oldValue, newValue) -> {

            if (newValue.equals("Not Full Paid")) {

                txfPaidAmount.setVisible(true);
                labRemainAmount.setVisible(true);

            } else {

                txfPaidAmount.setVisible(false);
                labRemainAmount.setVisible(false);
                labRemainAmount.setText("");
                txfPaidAmount.setText("");
            }
        });
    }
    @FXML
    private void update(ActionEvent event) throws ClassNotFoundException, SQLException {
        
        int classPrimaryKey = 0, sectionPrimaryKey = 0, feeTypeID = 0;
        connection = DatabaseConnection.getConnection();
        sql = "select * from class where name=?";
        statement = connection.prepareStatement(sql);
        statement.setString(1, comboClass.getValue());
        resultSet = statement.executeQuery();

        if (resultSet.next()) {
            classPrimaryKey = resultSet.getInt("id");

        }
        sql = "select * from sections where name=?";
        statement = connection.prepareStatement(sql);
        statement.setString(1, comboSection.getValue());
        resultSet = statement.executeQuery();

        if (resultSet.next()) {
            sectionPrimaryKey = resultSet.getInt("id");

        }
        sql = "select * from FEE_TYPE where FEE_CATEGORY=?";
        statement = connection.prepareStatement(sql);
        statement.setString(1, comboFeeType.getValue());
        resultSet = statement.executeQuery();

        if (resultSet.next()) {
            feeTypeID = resultSet.getInt("id");

        }
        
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmation Dialog");
        alert.setHeaderText(null);
        alert.setContentText("Are you soure to update student fee record?");
        alert.initModality(Modality.APPLICATION_MODAL);
        Optional<ButtonType> action = alert.showAndWait();
        if (action.get() == ButtonType.OK) {
            switch (comboStatus.getValue()) {
                case "Not Full Paid":
                    sql="update student_fee set AMOUNT=?,STATUS=?,DATE=?, PAID_AMOUNT=?,DUES=?,student_id=?,class_id=?,section_id=?,FEE_TYPE_ID=? where id=?";
                    statement = connection.prepareStatement(sql);
                    statement.setDouble(1, Double.parseDouble(txfFeeAmount.getText()));
                    statement.setString(2, comboStatus.getValue());
                    statement.setDate(3, Date.valueOf(txfDate.getValue()));
                    statement.setDouble(4, Double.parseDouble(txfPaidAmount.getText()));
                    statement.setDouble(5, Double.parseDouble(labRemainAmount.getText()));
                    statement.setInt(6, comboRegNo.getValue());
                    statement.setInt(7, classPrimaryKey);
                    statement.setInt(8, sectionPrimaryKey);
                    statement.setInt(9, feeTypeID);
                    statement.setInt(10, AccountManagementController.date.getToutionSlip());
                    statement.execute();
                    showNotifications("Student Fee Record Updation","Student Fee record update sucessfully");
                    break;
                case "Not Paid":
                    sql = "update student_fee set AMOUNT=?,STATUS=?,DATE=?, PAID_AMOUNT=?,DUES=?,student_id=?,class_id=?,section_id=?,FEE_TYPE_ID=? where id=?";
                    statement = connection.prepareStatement(sql);
                    statement.setDouble(1, Double.parseDouble(txfFeeAmount.getText()));
                    statement.setString(2, comboStatus.getValue());
                    statement.setDate(3, Date.valueOf(txfDate.getValue()));
                    statement.setDouble(4, 0);
                    statement.setDouble(5, Double.parseDouble(txfFeeAmount.getText()));
                    statement.setInt(6, comboRegNo.getValue());
                    statement.setInt(7, classPrimaryKey);
                    statement.setInt(8, sectionPrimaryKey);
                    statement.setInt(9, feeTypeID);
                    statement.setInt(10, AccountManagementController.date.getToutionSlip());
                    statement.execute();
                    showNotifications("Student Fee Record Updation", "Student Fee record update sucessfully");
                    break;
                default:
                    sql = "update student_fee set AMOUNT=?,STATUS=?,DATE=?, PAID_AMOUNT=?,DUES=?,student_id=?,class_id=?,section_id=?,FEE_TYPE_ID=? where id=?";
                    statement = connection.prepareStatement(sql);
                    statement.setDouble(1, Double.parseDouble(txfFeeAmount.getText()));
                    statement.setString(2, comboStatus.getValue());
                    statement.setDate(3, Date.valueOf(txfDate.getValue()));
                    statement.setDouble(4, 0);
                    statement.setDouble(5, 0);
                    statement.setInt(6, comboRegNo.getValue());
                    statement.setInt(7, classPrimaryKey);
                    statement.setInt(8, sectionPrimaryKey);
                    statement.setInt(9, feeTypeID);
                    statement.setInt(10, AccountManagementController.date.getToutionSlip());
                    statement.execute();
                    showNotifications("Student Fee Record Updation", "Student Fee record update sucessfully");
                    break;
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
    private void cancel(ActionEvent event) {
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
                statement.setString(1, newValue);
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

    private void calculateRemainAmount() {

        txfPaidAmount.textProperty().addListener((observable, oldValue, newValue) -> {
            double remainBalance = Double.parseDouble(txfFeeAmount.getText()) - Double.parseDouble(newValue);
            labRemainAmount.setText("" + remainBalance);
        });

    }

    private void loadFeeTypes() throws SQLException, ClassNotFoundException {
        connection = DatabaseConnection.getConnection();
        sql = "select * from FEE_TYPE";
        statement = connection.prepareStatement(sql);
        resultSet = statement.executeQuery();
        comboFeeType.getItems().clear();
        while (resultSet.next()) {

            comboFeeType.getItems().add(resultSet.getString("FEE_CATEGORY"));

        }

    }
}
