/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Report.employeeReports;

import com.jfoenix.controls.JFXComboBox;
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


public class employeeReportssssViewController implements Initializable {
    @FXML
    private JFXComboBox<Integer> empRegNo2;
    @FXML
    private JFXComboBox<String> status2;
    @FXML
    private JFXComboBox<String> status1;

    //************** for database connection***********
    Connection connection = null;
    PreparedStatement statement = null;
    ResultSet resultSet = null;
    String sql = null;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
       status2.getItems().addAll("Leave","Not Leave");
       status1.getItems().addAll("Leave","Not Leave");
        try {
            loadEmpRegNo();
        } catch (SQLException | ClassNotFoundException ex) {
            Logger.getLogger(employeeReportssssViewController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }    

    @FXML
    private void tolEmpView(ActionEvent event) throws ClassNotFoundException, JRException {
        connection = DatabaseConnection.getConnection();
        Map parameters = new HashMap();
        if(status1.getValue().equals("Not Leave")){
        parameters.put("leave_status", 0);
        }else{
        parameters.put("leave_status", 1);
        
        }
      
        File file = new File("src/Report/employeeReports/TotalEmployeeRecords.jrxml");
        JasperDesign jasperDesign = JRXmlLoader.load("C:\\reports\\IndividualEmployeeRecords.jrxml");
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
    private void indivEmpView(ActionEvent event)throws ClassNotFoundException, JRException {
        connection = DatabaseConnection.getConnection();
        Map parameters = new HashMap();
        if (status2.getValue().equals("Not Leave")) {
            parameters.put("leave_status", 0);
        } else {
            parameters.put("leave_status", 1);

        }
        parameters.put("reg_id", empRegNo2.getValue());
        File file = new File("src/Report/employeeReports/IndividualEmployeeRecords.jrxml");
        JasperDesign jasperDesign = JRXmlLoader.load("C:\\reports\\IndividualEmployeeRecords.jrxml");
        JasperReport jasperReport = JasperCompileManager.compileReport(jasperDesign);
        JasperPrint JasperPrint = (JasperPrint) JasperFillManager.fillReport(jasperReport, parameters, connection);
        JasperViewer jv = new JasperViewer(JasperPrint, false);
        JasperViewer.viewReport(JasperPrint, false);
    }
    private void loadEmpRegNo() throws SQLException, ClassNotFoundException{
        connection = DatabaseConnection.getConnection();
        sql = "select * from employee";
        statement = connection.prepareStatement(sql);
        
        resultSet = statement.executeQuery();
        empRegNo2.getItems().clear();
       while(resultSet.next()){
       empRegNo2.getItems().add(resultSet.getInt("reg_no"));
       
       }
       
    
    }
    
}
