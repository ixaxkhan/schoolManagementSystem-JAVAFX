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


public class GiveSalaryUpdateViewController implements Initializable {
    @FXML
    private JFXComboBox<Integer> comboEmpRegNo;
    @FXML
    private JFXTextField txfEmpName;
    @FXML
    private JFXTextField txfEmpFName;
    @FXML
    private JFXTextField txfAmount;
    @FXML
    private JFXComboBox<String> comboAmountType;
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
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        txfPaidAmount.setVisible(false);
        labRemainAmount.setVisible(false);
        comboStatus.getItems().clear();
        comboStatus.getItems().addAll("Paid", "Not Full Paid", "Not Paid");
        comboAmountType.getItems().clear();
        comboAmountType.getItems().addAll("Salary", "Product Amount");

        try {
            loadEmpRegNo();
            comboEmpRegNo.setValue(AccountManagementController.empObject.getEmpRegNo());
            txfDate.setValue(AccountManagementController.empObject.getEmpDate().toLocalDate());
            comboAmountType.setValue(AccountManagementController.empObject.getEmpFeeType());
            txfAmount.setText("" + AccountManagementController.empObject.getEmpTotalFee());
            comboStatus.setValue(AccountManagementController.empObject.getEmpStatus());
            txfPaidAmount.setText("" + AccountManagementController.empObject.getEmpPaidFee());
            labRemainAmount.setText("" + AccountManagementController.empObject.getEmpDues());
            txfEmpName.setText(AccountManagementController.empObject.getEmpName());
            txfEmpFName.setText(AccountManagementController.empObject.getEmpFName());
            loadEmpData();
            productAmountGetFocus();
        } catch (ClassNotFoundException | SQLException ex) {
            Logger.getLogger(GiveSalaryUpdateViewController.class.getName()).log(Level.SEVERE, null, ex);
        }
     
        isNotFullPaid();
        calculateRemainAmount();
    }    

    @FXML
    private void update(ActionEvent event) throws ClassNotFoundException, SQLException {
        
        connection = DatabaseConnection.getConnection();
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmation Dialog");
        alert.setHeaderText(null);
        alert.setContentText("Are you soure to save employee amount record?");
        alert.initModality(Modality.APPLICATION_MODAL);
        Optional<ButtonType> action = alert.showAndWait();
        if (action.get() == ButtonType.OK) {
            switch (comboStatus.getValue()) {
                case "Not Full Paid":
                    sql = "update  EMPLOYEE_SALARY set AMOUNT=?,STATUS=?,DATE=?, REMAIN_AMOUNT=?,PAID_AMOUNT=?,FEE_TYPE=?, EMPLOYEE_ID=? where id=?";
                    statement = connection.prepareStatement(sql);
                    statement.setDouble(1, Double.parseDouble(txfAmount.getText()));
                    statement.setString(2, comboStatus.getValue());
                    statement.setDate(3, Date.valueOf(txfDate.getValue()));
                    statement.setDouble(4, Double.parseDouble(labRemainAmount.getText()));
                    statement.setDouble(5, Double.parseDouble(txfPaidAmount.getText()));
                    statement.setString(6, comboAmountType.getValue());
                    statement.setInt(7, comboEmpRegNo.getValue());
                    statement.setInt(8, AccountManagementController.empObject.getEmpSlip());
                    statement.execute();
                    showNotifications("Employee Amount Record Updation","Employee amount record update sucessfully");
                    break;
                case "Not Paid":
                    sql = "update  EMPLOYEE_SALARY set AMOUNT=?,STATUS=?,DATE=?, REMAIN_AMOUNT=?,PAID_AMOUNT=?,FEE_TYPE=?, EMPLOYEE_ID=? where id=?";
                    statement = connection.prepareStatement(sql);
                    statement.setDouble(1, Double.parseDouble(txfAmount.getText()));
                    statement.setString(2, comboStatus.getValue());
                    statement.setDate(3, Date.valueOf(txfDate.getValue()));
                    statement.setDouble(4, Double.parseDouble(txfAmount.getText()));
                    statement.setDouble(5, 0);
                    statement.setString(6, comboAmountType.getValue());
                    statement.setInt(7, comboEmpRegNo.getValue());
                    statement.setInt(8, AccountManagementController.empObject.getEmpSlip());
                    statement.execute();
                    showNotifications("Employee Amount Record Updation", "Employee amount record update sucessfully");
                    break;
                default:
                    sql = "update  EMPLOYEE_SALARY set AMOUNT=?,STATUS=?,DATE=?, REMAIN_AMOUNT=?,PAID_AMOUNT=?,FEE_TYPE=?, EMPLOYEE_ID=? where id=?";
                    statement = connection.prepareStatement(sql);
                    statement.setDouble(1, Double.parseDouble(txfAmount.getText()));
                    statement.setString(2, comboStatus.getValue());
                    statement.setDate(3, Date.valueOf(txfDate.getValue()));
                    statement.setDouble(4, 0);
                    statement.setDouble(5, 0);
                    statement.setString(6, comboAmountType.getValue());
                    statement.setInt(7, comboEmpRegNo.getValue());
                    statement.setInt(8, AccountManagementController.empObject.getEmpSlip());
                    statement.execute();
                    showNotifications("Employee Amount Record Updation", "Employee amount record update sucessfully");
                    break;
            }
        }
    }

    @FXML
    private void cancel(ActionEvent event) {
        final Node source = (Node) event.getSource();
        Stage stage = (Stage) source.getScene().getWindow();
        stage.close();
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

    private void isNotFullPaid() {

        comboStatus.valueProperty().addListener((observable, oldValue, newValue) -> {

            if (newValue.equals("Not Full Paid")) {

                txfPaidAmount.setVisible(true);
                labRemainAmount.setVisible(true);
                labRemainAmount.setVisible(true);

            } else {

                txfPaidAmount.setVisible(false);
                labRemainAmount.setVisible(false);
                labRemainAmount.setText("");
                txfPaidAmount.setText("");
            }
        });
    }

    private void calculateRemainAmount() {

        txfPaidAmount.textProperty().addListener((observable, oldValue, newValue) -> {
            double remainBalance = Double.parseDouble(txfAmount.getText()) - Double.parseDouble(newValue);
            labRemainAmount.setText("" + remainBalance);
        });

    } 
    
    private void loadEmpRegNo() throws ClassNotFoundException, SQLException {
        connection = DatabaseConnection.getConnection();
        sql = "select * from employee where leave_status=?";
        statement = connection.prepareStatement(sql);
        statement.setInt(1, 0);
        resultSet = statement.executeQuery();
        comboEmpRegNo.getItems().clear();
        while (resultSet.next()) {

            comboEmpRegNo.getItems().add(resultSet.getInt("reg_no"));
        }

    }

    private void loadEmpData() {

        comboEmpRegNo.valueProperty().addListener((observable, oldValue, newValue) -> {

            try {
                connection = DatabaseConnection.getConnection();
                sql = "select * from employee where reg_no=?";
                statement = connection.prepareStatement(sql);
                statement.setInt(1, newValue);
                resultSet = statement.executeQuery();

                if (resultSet.next()) {

                    txfEmpName.setText(resultSet.getString("name"));
                    txfEmpFName.setText(resultSet.getString("father_name"));
                    txfAmount.setText("" + resultSet.getDouble("SALARY"));

                }
            } catch (ClassNotFoundException | SQLException ex) {
                Logger.getLogger(GiveSalaryViewController.class.getName()).log(Level.SEVERE, null, ex);
            }

        });

    }

    private void productAmountGetFocus() {

        comboAmountType.valueProperty().addListener((observable, oldValue, newValue) -> {

            if (comboAmountType.getValue().equals("Product Amount")) {

                txfAmount.setText("");
                txfAmount.requestFocus();

            } else {
                
                try {
                    connection = DatabaseConnection.getConnection();
                    sql = "select * from EMPLOYEE where REG_NO=?";
                    statement = connection.prepareStatement(sql);
                    statement.setInt(1,comboEmpRegNo.getValue() );
                    resultSet=statement.executeQuery();
                    if(resultSet.next()){
                    
                    txfAmount.setText(""+ resultSet.getDouble("salary"));
                    }
                } catch (SQLException | ClassNotFoundException ex) {
                    Logger.getLogger(GiveSalaryUpdateViewController.class.getName()).log(Level.SEVERE, null, ex);
                }
                
            }

        });

    }
}
