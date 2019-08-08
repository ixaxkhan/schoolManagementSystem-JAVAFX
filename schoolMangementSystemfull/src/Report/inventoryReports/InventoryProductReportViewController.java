/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Report.inventoryReports;

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


public class InventoryProductReportViewController implements Initializable {
    @FXML
    private JFXComboBox<String> productName1;
    @FXML
    private JFXComboBox<String> productName2;

    Connection connection = null;
    PreparedStatement statement = null;
    ResultSet resultSet = null;
    String sql = null;
    @FXML
    private JFXDatePicker fromDate1;
    @FXML
    private JFXDatePicker toDate1;
    @FXML
    private JFXDatePicker fromDate2;
    @FXML
    private JFXDatePicker toDate2;
    @FXML
    private JFXDatePicker fromDate3;
    @FXML
    private JFXDatePicker toDate3;
    @FXML
    private JFXDatePicker toDate4;
    @FXML
    private JFXDatePicker fromDate4;
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        try {
            loadProductNames(productName1);
            loadProductNames(productName2);
        } catch (ClassNotFoundException | SQLException ex) {
            Logger.getLogger(InventoryProductReportViewController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }    

    @FXML
    private void tolStockView(ActionEvent event) throws ClassNotFoundException, JRException, SQLException {
         connection = DatabaseConnection.getConnection();
        File file = new File("src/Report/inventoryReports/TotalProductStockRecords.jrxml");
        JasperDesign jasperDesign = JRXmlLoader.load("C:\\reports\\TotalProductStockRecords.jrxml");
        JasperReport jasperReport = JasperCompileManager.compileReport(jasperDesign);
        JasperPrint JasperPrint = (JasperPrint) JasperFillManager.fillReport(jasperReport, null, connection);
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
    private void totalProductPurchaseView(ActionEvent event)throws ClassNotFoundException, JRException, SQLException {
        connection = DatabaseConnection.getConnection();
        Map parameters = new HashMap();
       
        parameters.put("ffrom", Date.valueOf(fromDate1.getValue()));
        parameters.put("tto", Date.valueOf(toDate1.getValue()));
        File file = new File("src/Report/inventoryReports/TotalProductPurchaseRecords.jrxml");
        JasperDesign jasperDesign = JRXmlLoader.load("C:\\reports\\TotalProductPurchaseRecords.jrxml");
        JasperReport jasperReport = JasperCompileManager.compileReport(jasperDesign);
        JasperPrint JasperPrint = (JasperPrint) JasperFillManager.fillReport(jasperReport, parameters, connection);
        JasperViewer jv = new JasperViewer(JasperPrint, false);
        JasperViewer.viewReport(JasperPrint, false);
        
    }

    @FXML
    private void totalProductSaleView(ActionEvent event) throws ClassNotFoundException, JRException, SQLException{
        connection = DatabaseConnection.getConnection();
        Map parameters = new HashMap();

        parameters.put("ffrom", Date.valueOf(fromDate2.getValue()));
        parameters.put("tto", Date.valueOf(toDate2.getValue()));
        File file = new File("src/Report/inventoryReports/TotalProductSaleRecord.jrxml");
        JasperDesign jasperDesign = JRXmlLoader.load("C:\\reports\\TotalProductSaleRecord.jrxml");
        JasperReport jasperReport = JasperCompileManager.compileReport(jasperDesign);
        JasperPrint JasperPrint = (JasperPrint) JasperFillManager.fillReport(jasperReport, parameters, connection);
        JasperViewer jv = new JasperViewer(JasperPrint, false);
        JasperViewer.viewReport(JasperPrint, false);
    }

    @FXML
    private void indivProductPurchaseView(ActionEvent event)throws ClassNotFoundException, JRException, SQLException {
        int product_id2 = 0;
        connection = DatabaseConnection.getConnection();
        sql = "select id from add_product where name=?";
        statement = connection.prepareStatement(sql);
        statement.setString(1, productName1.getValue());
         resultSet=statement.executeQuery();
        if (resultSet.next()) {

            product_id2 = resultSet.getInt("id");
        }
        
         System.out.println( product_id2);
        Map parameters = new HashMap();
        parameters.put("ffrom", Date.valueOf(fromDate3.getValue()));
        parameters.put("tto", Date.valueOf(toDate3.getValue()));
        parameters.put("product_id",product_id2 );
        File file = new File("src/Report/inventoryReports/IndividualProductPurchaseRecord.jrxml");
        JasperDesign jasperDesign = JRXmlLoader.load("C:\\reports\\IndividualProductPurchaseRecord.jrxml");
        JasperReport jasperReport = JasperCompileManager.compileReport(jasperDesign);
        JasperPrint JasperPrint = (JasperPrint) JasperFillManager.fillReport(jasperReport, parameters, connection);
        JasperViewer jv = new JasperViewer(JasperPrint, false);
        JasperViewer.viewReport(JasperPrint, false);
    }

    @FXML
    private void indivProductSaleView(ActionEvent event) throws ClassNotFoundException, JRException, SQLException{
       int product_id2=0;
        connection = DatabaseConnection.getConnection();
        sql="select id from add_product where name=?";
        statement=connection.prepareStatement(sql);
        statement.setString(1, productName2.getValue());
        resultSet=statement.executeQuery();
        if(resultSet.next()){
        
        product_id2=resultSet.getInt("id");
        }
        System.out.println( product_id2);
        Map parameters = new HashMap();
        parameters.put("ffrom", Date.valueOf(fromDate4.getValue()));
        parameters.put("tto", Date.valueOf(toDate4.getValue()));
        parameters.put("product_id",product_id2 );
        File file = new File("src/Report/inventoryReports/IndividualProductSaleRecord.jrxml");
        JasperDesign jasperDesign = JRXmlLoader.load("C:\\reports\\IndividualProductSaleRecord.jrxml");
        JasperReport jasperReport = JasperCompileManager.compileReport(jasperDesign);
        JasperPrint JasperPrint = (JasperPrint) JasperFillManager.fillReport(jasperReport, parameters, connection);
        JasperViewer jv = new JasperViewer(JasperPrint, false);
        JasperViewer.viewReport(JasperPrint, false);
    }
    private void loadProductNames(JFXComboBox poductName) throws ClassNotFoundException, SQLException{
        connection = DatabaseConnection.getConnection();
        sql = "select * from add_product";
        statement = connection.prepareStatement(sql);
        resultSet=statement.executeQuery();
        poductName.getItems().clear();
        while(resultSet.next()){
        
        poductName.getItems().add(resultSet.getString("name"));
        
        }
    
    }
    
}
