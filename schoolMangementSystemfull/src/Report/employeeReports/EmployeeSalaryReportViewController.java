/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Report.employeeReports;

import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXDatePicker;
import databaseconnection.DatabaseConnection;
import java.io.File;
import java.net.URL;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.stage.Stage;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.xml.JRXmlLoader;
import net.sf.jasperreports.view.JasperViewer;

/**
 * FXML Controller class
 *
 * @author khan
 */
public class EmployeeSalaryReportViewController implements Initializable {
    @FXML
    private JFXDatePicker fromDate1;
    @FXML
    private JFXDatePicker toDate1;
    @FXML
    private JFXComboBox<Integer> empRegNo2;
    @FXML
    private JFXDatePicker fromDate2;
    @FXML
    private JFXDatePicker toDate2;
    @FXML
    private JFXComboBox<String> status2;
    @FXML
    private JFXComboBox<String> status1;
    @FXML
    private JFXComboBox<String> amountType1;
    @FXML
    private JFXComboBox<String> amountType2;
    //************** for database connection***********
    Connection connection = null;
    PreparedStatement statement = null;
    ResultSet resultSet = null;
    String sql = null;
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        status1.getItems().clear();
        status1.getItems().addAll("Paid", "Not Full Paid", "Not Paid");
        amountType1.getItems().clear();
        amountType1.getItems().addAll("Salary", "Product Amount");
        status2.getItems().clear();
        status2.getItems().addAll("Paid", "Not Full Paid", "Not Paid");
        amountType2.getItems().clear();
        amountType2.getItems().addAll("Salary", "Product Amount");
        try {
            loadEmpRegNo();
        } catch (SQLException | ClassNotFoundException ex) {
            Logger.getLogger(EmployeeSalaryReportViewController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }    

    @FXML
    private void tolEmpSalaryView(ActionEvent event) throws JRException, ClassNotFoundException {
        connection = DatabaseConnection.getConnection();
        Map parameters = new HashMap();
        parameters.put("fee_status", status1.getValue());
        parameters.put("ffrom", Date.valueOf(fromDate1.getValue()));
        parameters.put("tto", Date.valueOf(toDate1.getValue()));
        parameters.put("fee_type", amountType1.getValue());
        File file = new File("src/Report/employeeReports/TotalEmployeeAmountRecords.jrxml");
        JasperDesign jasperDesign = JRXmlLoader.load("C:\\reports\\TotalEmployeeAmountRecords.jrxml");
        JasperReport jasperReport = JasperCompileManager.compileReport(jasperDesign);
        JasperPrint JasperPrint = (JasperPrint) JasperFillManager.fillReport(jasperReport, parameters, connection);
        JasperViewer jv = new JasperViewer(JasperPrint, false);
        JasperViewer.viewReport(JasperPrint, false);
    }

    @FXML
    private void cancel(ActionEvent event) {
        final Node source = (Node) event.getSource();
        Stage stage = (Stage) source.getScene().getWindow();
        stage.close();
    }

    @FXML
    private void indivEmpSalaryView(ActionEvent event)throws JRException, ClassNotFoundException {
        connection = DatabaseConnection.getConnection();
        Map parameters = new HashMap();
        parameters.put("fee_status", status2.getValue());
        parameters.put("ffrom", Date.valueOf(fromDate2.getValue()));
        parameters.put("tto", Date.valueOf(toDate2.getValue()));
        parameters.put("fee_type", amountType2.getValue());
        parameters.put("emp_id", empRegNo2.getValue());
        File file = new File("src/Report/employeeReports/IndividualEmployeeAmountRecords.jrxml");
        JasperDesign jasperDesign = JRXmlLoader.load("C:\\reports\\IndividualEmployeeAmountRecords.jrxml");
        JasperReport jasperReport = JasperCompileManager.compileReport(jasperDesign);
        JasperPrint JasperPrint = (JasperPrint) JasperFillManager.fillReport(jasperReport, parameters, connection);
        JasperViewer jv = new JasperViewer(JasperPrint, false);
        JasperViewer.viewReport(JasperPrint, false);
    }
    private void loadEmpRegNo() throws SQLException, ClassNotFoundException {
        connection = DatabaseConnection.getConnection();
        sql = "select * from employee";
        statement = connection.prepareStatement(sql);

        resultSet = statement.executeQuery();
        empRegNo2.getItems().clear();
        while (resultSet.next()) {
            empRegNo2.getItems().add(resultSet.getInt("reg_no"));

        }

    }
}
