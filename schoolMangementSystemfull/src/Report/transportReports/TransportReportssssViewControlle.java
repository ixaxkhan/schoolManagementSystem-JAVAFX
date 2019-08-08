/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Report.transportReports;

import com.jfoenix.controls.JFXComboBox;
import databaseconnection.DatabaseConnection;
import java.io.File;
import java.net.URL;
import java.sql.Connection;
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
public class TransportReportssssViewControlle implements Initializable {
    @FXML
    private JFXComboBox<String> comboViechleNo;

    
    //************** for database connection***********
    Connection connection = null;
    PreparedStatement statement = null;
    ResultSet resultSet = null;
    String sql = null;
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        try {
            loadViechleNo();
        } catch (ClassNotFoundException | SQLException ex) {
            Logger.getLogger(TransportReportssssViewControlle.class.getName()).log(Level.SEVERE, null, ex);
        }
    }    

    @FXML
    private void tolViechleView(ActionEvent event) throws  ClassNotFoundException, SQLException, JRException {
        connection = DatabaseConnection.getConnection();
        File file = new File("src/Report/transportReports/TotalViechleRecord.jrxml");
        JasperDesign jasperDesign = JRXmlLoader.load("C:\\reports\\TotalViechleRecord.jrxml");
        JasperReport jasperReport = JasperCompileManager.compileReport(jasperDesign);
        JasperPrint JasperPrint = (JasperPrint) JasperFillManager.fillReport(jasperReport,null, connection);
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
    private void studentRelatedToViechleView(ActionEvent event)throws ClassNotFoundException, SQLException,JRException {
      int viechle_id=0;
      connection = DatabaseConnection.getConnection(); 
      sql="select * from VEHICLE where VIECHLE_NO=?";
      statement=connection.prepareStatement(sql);
      statement.setString(1,comboViechleNo.getValue() );
      resultSet=statement.executeQuery();
      comboViechleNo.getItems().clear();
      if(resultSet.next()){
      
      viechle_id=resultSet.getInt("id");
      
      }
      Map parameters = new HashMap();
      parameters.put("viechle_id", viechle_id);
        File file = new File("src/Report/transportReports/TotalStudentReocordRelatedtoViechle.jrxml");
        JasperDesign jasperDesign = JRXmlLoader.load("C:\\reports\\TotalStudentReocordRelatedtoViechle.jrxml");
        JasperReport jasperReport = JasperCompileManager.compileReport(jasperDesign);
        JasperPrint JasperPrint = (JasperPrint) JasperFillManager.fillReport(jasperReport, parameters, connection);
        JasperViewer jv = new JasperViewer(JasperPrint, false);
        JasperViewer.viewReport(JasperPrint, false);
    }
  private void loadViechleNo() throws ClassNotFoundException, SQLException{
      connection = DatabaseConnection.getConnection();
      sql="select * from VEHICLE ";
      statement=connection.prepareStatement(sql);
      resultSet=statement.executeQuery();
      comboViechleNo.getItems().clear();
      while(resultSet.next()){
      
      comboViechleNo.getItems().add(resultSet.getString("VIECHLE_NO"));
      
      }
      
  
  
  }  
}
