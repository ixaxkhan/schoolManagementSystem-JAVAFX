/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Report.attendenceReports;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXDatePicker;
import databaseconnection.DatabaseConnection;
import java.io.File;
import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;
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
public class employeeAttendenceViewController implements Initializable {
    @FXML
    private JFXDatePicker tolEmpFromDate;
    @FXML
    private JFXDatePicker tolEmpToDate;
    @FXML
    private JFXComboBox<Integer> indivEmpRegNo;
    @FXML
    private JFXDatePicker indivEmpFromDate;
    @FXML
    private JFXDatePicker indivEmpToDate;
    Connection connection = null;
    PreparedStatement statement = null;
    ResultSet resultSet = null;
    String sql = null;

   
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        try {
            loadEmpRegNo();
        } catch (ClassNotFoundException | SQLException ex) {
            Logger.getLogger(employeeAttendenceViewController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }    

    @FXML
    private void tolEmpView(ActionEvent event) throws JRException, ClassNotFoundException{
        connection = DatabaseConnection.getConnection();
        Map parameters = new HashMap();
        
        parameters.put("ffrom", Date.valueOf(tolEmpFromDate.getValue()));
        parameters.put("tto", Date.valueOf(tolEmpToDate.getValue()));
        //File file = new File("src/Report/attendenceReports/TotalEmployeesAttendeceRecord.jrxml");
        JasperDesign jasperDesign = JRXmlLoader.load("C:\\reports\\TotalEmployeesAttendeceRecord.jrxml");
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
    private void indivEmpView(ActionEvent event)throws JRException, ClassNotFoundException, SQLException {
        
        connection = DatabaseConnection.getConnection();
        Map parameters = new HashMap();
        parameters.put("ffrom", Date.valueOf(tolEmpFromDate.getValue()));
        parameters.put("tto", Date.valueOf(tolEmpToDate.getValue()));
        parameters.put("emp_id", indivEmpRegNo.getValue());
        //File file = new File("src/Report/attendenceReports/EmployeeWiseAttendenceRecord.jrxml");
        JasperDesign jasperDesign = JRXmlLoader.load("C:\\reports\\EmployeeWiseAttendenceRecord.jrxml");
        JasperReport jasperReport = JasperCompileManager.compileReport(jasperDesign);
        JasperPrint JasperPrint = (JasperPrint) JasperFillManager.fillReport(jasperReport, parameters, connection);
        JasperViewer jv = new JasperViewer(JasperPrint, false);
        JasperViewer.viewReport(JasperPrint, false);
    }
private void loadEmpRegNo() throws ClassNotFoundException, SQLException{

    connection = DatabaseConnection.getConnection();
    sql = "select * from employee where leave_status=? ";
    statement = connection.prepareStatement(sql);
    statement.setInt(1, 0);
    resultSet = statement.executeQuery();
    indivEmpRegNo.getItems().clear();
    while (resultSet.next()) {

        indivEmpRegNo.getItems().add(resultSet.getInt("reg_no"));

    }


}
}
