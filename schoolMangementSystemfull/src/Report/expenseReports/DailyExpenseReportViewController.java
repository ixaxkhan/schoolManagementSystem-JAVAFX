/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Report.expenseReports;

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

public class DailyExpenseReportViewController implements Initializable {
    @FXML
    private JFXComboBox<String> comboExpenseName;
    @FXML
    private JFXDatePicker FromDate1;
    @FXML
    private JFXDatePicker toDate1;
    @FXML
    private JFXDatePicker fromDate2;
    @FXML
    private JFXDatePicker toDate2;

    //************** for database connection***********
    Connection connection = null;
    PreparedStatement statement = null;
    ResultSet resultSet = null;
    String sql = null;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        try {
            loadProductName();
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(DailyExpenseReportViewController.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SQLException ex) {
            Logger.getLogger(DailyExpenseReportViewController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }    

    @FXML
    private void tolExpenseView(ActionEvent event)throws JRException, ClassNotFoundException  {
        connection = DatabaseConnection.getConnection();
        Map parameters = new HashMap();
       
        parameters.put("ffrom", Date.valueOf(FromDate1.getValue()));
        parameters.put("tto", Date.valueOf(toDate1.getValue()));
       
        File file = new File("src/Report/expenseReports/TotalExpensesConsumeRecords.jrxml");
        JasperDesign jasperDesign = JRXmlLoader.load("C:\\reports\\TotalExpensesConsumeRecords.jrxml");
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
    private void indivExpenseView(ActionEvent event)throws JRException, ClassNotFoundException, SQLException  {
        int expense_id=0;
        connection = DatabaseConnection.getConnection();
        sql="select * from EXPENSE_TYPE where CATEGORY=?";
        statement=connection.prepareStatement(sql);
        statement.setString(1, comboExpenseName.getValue());
        resultSet=statement.executeQuery();
        if(resultSet.next()){
        
        expense_id=resultSet.getInt("EXPENSE_TYPE.id");
        }
        
        Map parameters = new HashMap();

        parameters.put("ffrom", Date.valueOf(FromDate1.getValue()));
        parameters.put("tto", Date.valueOf(toDate1.getValue()));
        parameters.put("expense_id", expense_id);
        parameters.put("expense_type", comboExpenseName.getValue());

        File file = new File("src/Report/expenseReports/TotalExpensesConsumeRecords.jrxml");
        JasperDesign jasperDesign = JRXmlLoader.load("C:\\reports\\TotalExpensesConsumeRecords.jrxml");
        JasperReport jasperReport = JasperCompileManager.compileReport(jasperDesign);
        JasperPrint JasperPrint = (JasperPrint) JasperFillManager.fillReport(jasperReport, parameters, connection);
        JasperViewer jv = new JasperViewer(JasperPrint, false);
        JasperViewer.viewReport(JasperPrint, false);
    }
    private void loadProductName() throws ClassNotFoundException, SQLException{
    
     connection = DatabaseConnection.getConnection();
     sql="select  * from EXPENSE_TYPE";
     statement=connection.prepareStatement(sql);
     resultSet=statement.executeQuery();
      comboExpenseName.getItems().clear();
     while( resultSet.next()){
     
     comboExpenseName.getItems().add(resultSet.getString("CATEGORY"));
     
     }
    
    }
    
}
