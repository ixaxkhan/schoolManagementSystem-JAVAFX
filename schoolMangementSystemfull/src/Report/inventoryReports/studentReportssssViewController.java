/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Report.inventoryReports;

import Report.studentReports.*;
import com.jfoenix.controls.JFXComboBox;
import databaseconnection.DatabaseConnection;
import java.io.File;
import java.io.FileNotFoundException;
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
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
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
import student.Registration.formController;

/**
 * FXML Controller class
 *
 * @author khan
 */
public class studentReportssssViewController implements Initializable {
    @FXML
    private JFXComboBox<String> comboClass1;
    @FXML
    private JFXComboBox<String> comboSection1;
    @FXML
    private JFXComboBox<String> comboClass2;
    @FXML
    private JFXComboBox<String> comboSection2;
    @FXML
    private JFXComboBox<Integer> comboRegNo;

    //************** for database connection***********
    Connection connection = null;
    PreparedStatement statement = null;
    ResultSet resultSet = null;
    String sql = null;
    
      // **************for class and section observable list*******************
    ObservableList<String> classesNames = FXCollections.observableArrayList();
    ObservableList<String> sectionName = FXCollections.observableArrayList();
    
    
    @FXML
    private JFXComboBox<String> status1;
    @FXML
    private JFXComboBox<String> status2;
    @FXML
    private JFXComboBox<String> status3;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
       status2.getItems().addAll("Leave","Not Leave");
       status1.getItems().addAll("Leave","Not Leave");
       status3.getItems().addAll("Leave","Not Leave");
        try {
            showClasses1();
            loadSectionsName1();
            loadSectionsName1();
            showClasses2();
            loadSectionsName2();
            loadSectionsName2();
            loadRegNo();
        } catch (ClassNotFoundException | SQLException ex) {
            Logger.getLogger(studentReportssssViewController.class.getName()).log(Level.SEVERE, null, ex);
        }
      
    }    

    @FXML
    private void tolStudentView(ActionEvent event) throws ClassNotFoundException, JRException {
        connection = DatabaseConnection.getConnection();
        Map parameters = new HashMap();
        if(status1.getValue().equals("Leave")){
        parameters.put("status", 1);
        }else{
        
            parameters.put("status", 0);
        }
        

        File file = new File("src/Report/studentReports/TotalStudentRecord.jrxml");
        JasperDesign jasperDesign = JRXmlLoader.load("C:\\reports\\TotalStudentRecord.jrxml");
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
    private void totalStudentByClassSectionView(ActionEvent event) throws  ClassNotFoundException, SQLException, JRException {
         int classPrimaryKey = 0, sectionPrimaryKey = 0;
        connection = DatabaseConnection.getConnection();
        sql = "select * from class where name=?";
        statement = connection.prepareStatement(sql);
        statement.setString(1, comboClass1.getValue());
        resultSet = statement.executeQuery();

        if (resultSet.next()) {
            classPrimaryKey = resultSet.getInt("id");

        }
        sql = "select * from sections where name=?";
        statement = connection.prepareStatement(sql);
        statement.setString(1, comboSection1.getValue());
        resultSet = statement.executeQuery();

        if (resultSet.next()) {
            sectionPrimaryKey = resultSet.getInt("id");

        }
        Map parameters = new HashMap();
        if (status2.getValue().equals("Leave")) {
            parameters.put("status", 1);
        } else {

            parameters.put("status", 0);
        }
        parameters.put("class_id", classPrimaryKey);
        parameters.put("section_id", sectionPrimaryKey);
        parameters.put("class_name", comboClass1.getValue());
        parameters.put("Section_name", comboSection1.getValue());
        File file = new File("src/Report/studentReports/TotalStudentRecordbyClassSectionWise.jrxml");
        JasperDesign jasperDesign = JRXmlLoader.load("C:\\reports\\TotalStudentRecordbyClassSectionWise.jrxml");
        JasperReport jasperReport = JasperCompileManager.compileReport(jasperDesign);
        JasperPrint JasperPrint = (JasperPrint) JasperFillManager.fillReport(jasperReport, parameters, connection);
        JasperViewer jv = new JasperViewer(JasperPrint, false);
        JasperViewer.viewReport(JasperPrint, false);
        
        
    }

    @FXML
    private void indivStudentView(ActionEvent event) throws  ClassNotFoundException, SQLException, JRException{
        int classPrimaryKey = 0, sectionPrimaryKey = 0;
        connection = DatabaseConnection.getConnection();
        sql = "select * from class where name=?";
        statement = connection.prepareStatement(sql);
        statement.setString(1, comboClass2.getValue());
        resultSet = statement.executeQuery();

        if (resultSet.next()) {
            classPrimaryKey = resultSet.getInt("id");

        }
        sql = "select * from sections where name=?";
        statement = connection.prepareStatement(sql);
        statement.setString(1, comboSection2.getValue());
        resultSet = statement.executeQuery();

        if (resultSet.next()) {
            sectionPrimaryKey = resultSet.getInt("id");

        }
        Map parameters = new HashMap();
        if (status3.getValue().equals("Leave")) {
            parameters.put("status", 1);
  
        } else {

            parameters.put("status", 0);
          
        }
        parameters.put("class_id", classPrimaryKey);
        parameters.put("section_id", sectionPrimaryKey);
        parameters.put("class_name", comboClass2.getValue());
        parameters.put("Section_name", comboSection2.getValue());
        parameters.put("student_id", comboRegNo.getValue());
        File file = new File("src/Report/studentReports/IndividualStudentRecord.jrxml");
        JasperDesign jasperDesign = JRXmlLoader.load("C:\\reports\\IndividualStudentRecord.jrxml");
        JasperReport jasperReport = JasperCompileManager.compileReport(jasperDesign);
        JasperPrint JasperPrint = (JasperPrint) JasperFillManager.fillReport(jasperReport, parameters, connection);
        JasperViewer jv = new JasperViewer(JasperPrint, false);
        JasperViewer.viewReport(JasperPrint, false);
    }
    
    private void showClasses1() throws ClassNotFoundException, SQLException {

        connection = DatabaseConnection.getConnection();
        sql = "select * from class";
        statement = connection.prepareStatement(sql);

        resultSet = statement.executeQuery();
        classesNames.clear();
        while (resultSet.next()) {
            classesNames.add(resultSet.getString("name"));

        }
       comboClass1.getItems().clear();
       comboClass1.setItems(classesNames);

    }

    // this fuction is used to load respective sections of class
    private void loadSectionsName1() {

        comboClass1.valueProperty().addListener((observable, oldValue, newValue) -> {
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
                Logger.getLogger(studentReportssssViewController.class.getName()).log(Level.SEVERE, null, ex);
            }

        });

        comboSection1.getItems().clear();
        comboSection1.setItems(sectionName);
    }
    private void showClasses2() throws ClassNotFoundException, SQLException {

        connection = DatabaseConnection.getConnection();
        sql = "select * from class";
        statement = connection.prepareStatement(sql);

        resultSet = statement.executeQuery();
        classesNames.clear();
        while (resultSet.next()) {
            classesNames.add(resultSet.getString("name"));

        }
        comboClass2.getItems().clear();
        comboClass2.setItems(classesNames);

    }

    // this fuction is used to load respective sections of class
    private void loadSectionsName2() {

        comboClass2.valueProperty().addListener((observable, oldValue, newValue) -> {
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
                Logger.getLogger(studentReportssssViewController.class.getName()).log(Level.SEVERE, null, ex);
            }

        });

        comboSection2.getItems().clear();
        comboSection2.setItems(sectionName);
    }
    private void loadRegNo() throws ClassNotFoundException, SQLException{
        connection = DatabaseConnection.getConnection();
        sql = "select * from student ";
        statement = connection.prepareStatement(sql);
        resultSet = statement.executeQuery();
        comboRegNo.getItems().clear();
        while(resultSet.next()){
        
        comboRegNo.getItems().add(resultSet.getInt("reg_id"));
        
        }
    
    
    }
}
