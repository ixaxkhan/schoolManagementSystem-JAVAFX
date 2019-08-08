/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Report.attendenceReports;

import Report.accountReports.FeeCollectionViewController;
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

/**
 * FXML Controller class
 *
 * @author khan
 */
public class StudentAttendenceViewController implements Initializable {
    @FXML
    private JFXDatePicker classSectionFromDate;
   
    @FXML
    private JFXDatePicker classSectionToDate;
    @FXML
    private JFXComboBox<Integer> studentRegNo;
    @FXML
    private JFXDatePicker studentWiseFromDate;
    @FXML
    private JFXDatePicker studentWiseToDate;

    Connection connection = null;
    PreparedStatement statement = null;
    ResultSet resultSet = null;

    String sql = null;
    // **************for class and section observable list*******************
    ObservableList<String> classesNames = FXCollections.observableArrayList();
    ObservableList<String> sectionName = FXCollections.observableArrayList();
    ObservableList<Integer> studentRegNo2 = FXCollections.observableArrayList();
    @FXML
    private JFXComboBox<String> comboClass1;
    @FXML
    private JFXComboBox<String> comboSection1;
    @FXML
    private JFXComboBox<String> comboClass2;
    @FXML
    private JFXComboBox<String> comboSection2;
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        try {
            showClasses1();
            loadSectionsName1();
            loadSectionsName1();
            showClasses2();
            loadSectionsName2();
            loadSectionsName2();
            loadSectionsStudentRegNo();
            loadSectionsStudentRegNo();
            
        } catch (ClassNotFoundException | SQLException ex) {
            Logger.getLogger(StudentAttendenceViewController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }    

    @FXML
    private void classSectionWiseView(ActionEvent event)throws JRException, ClassNotFoundException, SQLException  {
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
        parameters.put("ffrom", Date.valueOf(classSectionFromDate.getValue()));
        parameters.put("tto", Date.valueOf(classSectionToDate.getValue()));
        parameters.put("class_id", classPrimaryKey);
        parameters.put("section_id", sectionPrimaryKey);
        parameters.put("class_name", comboClass1.getValue());
        parameters.put("section_name", comboSection1.getValue());
       // File file = new File("src/Report/attendenceReports/ClassSectionWise.jrxml");
        JasperDesign jasperDesign = JRXmlLoader.load("C:\\reports\\ClassSectionWise.jrxml");
        JasperReport jasperReport = JasperCompileManager.compileReport(jasperDesign);
        JasperPrint JasperPrint = (JasperPrint) JasperFillManager.fillReport(jasperReport, parameters, connection);
        JasperViewer jv = new JasperViewer(JasperPrint, false);
        JasperViewer.viewReport(JasperPrint, false);
        
    }
    
    
// this function is for loading class name section name for classSectionWiseView
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
                Logger.getLogger(StudentAttendenceViewController.class.getName()).log(Level.SEVERE, null, ex);
            }

        });

        comboSection1.getItems().clear();
        comboSection1.setItems(sectionName);
    }

    @FXML
    private void cancel(ActionEvent event) {
        final Node source = (Node) event.getSource();
        Stage stage = (Stage) source.getScene().getWindow();
        stage.close();
    }
// this function is for loading class name section name for  studentWiseView
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
                Logger.getLogger(StudentAttendenceViewController.class.getName()).log(Level.SEVERE, null, ex);
            }

        });

        comboSection2.getItems().clear();
        comboSection2.setItems(sectionName);
    }
    private void loadSectionsStudentRegNo() {

        comboSection2.valueProperty().addListener((observable, oldValue, newValue) -> {
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
                studentRegNo2.clear();
                while (resultSet.next()) {

                    studentRegNo2.add(resultSet.getInt("reg_id"));

                }

            } catch (ClassNotFoundException | SQLException ex) {
                Logger.getLogger(StudentAttendenceViewController.class.getName()).log(Level.SEVERE, null, ex);
            }

        });

        studentRegNo.getItems().clear();
        studentRegNo.setItems(studentRegNo2);
    }
    @FXML
    private void studentWiseView(ActionEvent event) throws JRException, ClassNotFoundException, SQLException {
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
        parameters.put("ffrom", Date.valueOf(studentWiseFromDate.getValue()));
        parameters.put("tto", Date.valueOf(studentWiseToDate.getValue()));
        parameters.put("class_id", classPrimaryKey);
        parameters.put("section_id", sectionPrimaryKey);
        parameters.put("class_name", comboClass2.getValue());
        parameters.put("section_name", comboSection2.getValue());
        parameters.put("student_id", studentRegNo.getValue());
        //File file = new File("src/Report/attendenceReports/StudentWise.jrxml");
        JasperDesign jasperDesign = JRXmlLoader.load("C:\\reports\\StudentWise.jrxml");
        JasperReport jasperReport = JasperCompileManager.compileReport(jasperDesign);
        JasperPrint JasperPrint = (JasperPrint) JasperFillManager.fillReport(jasperReport, parameters, connection);
        JasperViewer jv = new JasperViewer(JasperPrint, false);
        JasperViewer.viewReport(JasperPrint, false);
        
    }
    

}
 