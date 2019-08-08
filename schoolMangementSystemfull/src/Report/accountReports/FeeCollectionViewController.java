/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Report.accountReports;

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
public class FeeCollectionViewController implements Initializable {
    @FXML
    private JFXDatePicker tolSchoolFromDate;
    @FXML
    private JFXDatePicker tolSchoolToDate;
    @FXML
    private JFXDatePicker schoolSectionFromDate;
    @FXML
    private JFXDatePicker schoolSectionToDate;
    @FXML
    private JFXComboBox<String> comboClass1;
    @FXML
    private JFXComboBox<String> comboSection1;
    @FXML
    private JFXComboBox<String> comboClass2;
    @FXML
    private JFXComboBox<String> comboSection2;
    @FXML
    private JFXComboBox<Integer> indivStudentRegNo;
    @FXML
    private JFXDatePicker indivStudentToDate;
    @FXML
    private JFXComboBox<String> comboStatus1;
    @FXML
    private JFXComboBox<String> comboStatus2;
    @FXML
    private JFXComboBox<String> comboFeeType3;
    @FXML
    private JFXComboBox<String> comboStatus3;
    @FXML
    private JFXDatePicker indivStudentFromDate;
    @FXML
    private JFXComboBox<String> comboFeeType2;
    @FXML
    private JFXComboBox<String> comboFeeType1;

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
        try {
            loadFeeTypes( comboFeeType1);
            loadFeeTypes( comboFeeType2);
            loadFeeTypes( comboFeeType3);
            comboStatus1.getItems().addAll("Paid", "Not Full Paid", "Not Paid");
            comboStatus2.getItems().addAll("Paid", "Not Full Paid", "Not Paid");
            comboStatus3.getItems().addAll("Paid", "Not Full Paid", "Not Paid");
            showClasses1();
            loadSectionsName1();
            loadSectionsName1();
            
            showClasses2();
            loadSectionsName2();
            loadSectionsName2();
            loadSectionsStudentRegNo();
            
        } catch (SQLException ex) {
            Logger.getLogger(FeeCollectionViewController.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(FeeCollectionViewController.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }    

    @FXML
    private void tolSchoolView(ActionEvent event) throws ClassNotFoundException, JRException, SQLException {
        int  feeTypeID=0;
        connection = DatabaseConnection.getConnection();
        sql = "select * from FEE_TYPE where FEE_CATEGORY=?";
        statement = connection.prepareStatement(sql);
        statement.setString(1, comboFeeType2.getValue());
        resultSet = statement.executeQuery();

        if (resultSet.next()) {
            feeTypeID = resultSet.getInt("id");

        }
        Map parameters = new HashMap();
        parameters.put("fee_status", comboStatus2.getValue());
        parameters.put("ffrom", Date.valueOf(tolSchoolFromDate.getValue()));
        parameters.put("tto", Date.valueOf(tolSchoolToDate.getValue()));
        parameters.put("fee_type", comboFeeType2.getValue());
        parameters.put("fee_type_id", feeTypeID);
        
       // Path currentRelativePath = Paths.get("");
       // String s = currentRelativePath.toAbsolutePath().toString();
        //File file= new File("C:\\reports\\report1.jrxml");
        JasperDesign jasperDesign = JRXmlLoader.load("C:\\reports\\report1.jrxml");
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
    private void schoolSectionView(ActionEvent event)throws ClassNotFoundException, JRException, SQLException {
       
        int classPrimaryKey = 0, sectionPrimaryKey = 0, feeTypeID = 0;
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
        sql = "select * from FEE_TYPE where FEE_CATEGORY=?";
        statement = connection.prepareStatement(sql);
        statement.setString(1, comboFeeType1.getValue());
        resultSet = statement.executeQuery();

        if (resultSet.next()) {
            feeTypeID = resultSet.getInt("id");

        }
        Map parameters = new HashMap();
        parameters.put("fee_status", comboStatus1.getValue());
        parameters.put("ffrom", Date.valueOf(schoolSectionFromDate.getValue()));
        parameters.put("tto", Date.valueOf(schoolSectionToDate.getValue()));
        parameters.put("fee_type", comboFeeType1.getValue());
        parameters.put("fee_type_id", feeTypeID);
        parameters.put("class_id", classPrimaryKey);
        parameters.put("section_id", sectionPrimaryKey);
        parameters.put("section_name", comboSection1.getValue());
        parameters.put("class_name", comboClass1.getValue());
        //File file = new File("C:\\reports\\ClassSectionWisFeeReport.jrxml");
        JasperDesign jasperDesign = JRXmlLoader.load("C:\\reports\\ClassSectionWisFeeReport.jrxml");
        JasperReport jasperReport = JasperCompileManager.compileReport(jasperDesign);
        JasperPrint JasperPrint = (JasperPrint) JasperFillManager.fillReport(jasperReport, parameters, connection);
        JasperViewer jv = new JasperViewer(JasperPrint, false);
        JasperViewer.viewReport(JasperPrint, false);
        
    }

    @FXML
    private void indivStudentView(ActionEvent event)throws ClassNotFoundException, JRException, SQLException  {
        
        int classPrimaryKey = 0, sectionPrimaryKey = 0, feeTypeID = 0;
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
        sql = "select * from FEE_TYPE where FEE_CATEGORY=?";
        statement = connection.prepareStatement(sql);
        statement.setString(1, comboFeeType3.getValue());
        resultSet = statement.executeQuery();

        if (resultSet.next()) {
            feeTypeID = resultSet.getInt("id");

        }
        
       
        Map parameters2 = new HashMap();
        parameters2.put("fee_status", comboStatus3.getValue());
        parameters2.put("ffrom", Date.valueOf(indivStudentFromDate.getValue()));
        parameters2.put("tto", Date.valueOf(indivStudentToDate.getValue()));
        parameters2.put("class_id", classPrimaryKey);
        parameters2.put("section_id", sectionPrimaryKey);
        parameters2.put("student_id", indivStudentRegNo.getValue());
        parameters2.put("fee_type_id", feeTypeID);
        parameters2.put("fee_type", comboFeeType3.getValue());
        parameters2.put("section_name", comboSection2.getValue());
        parameters2.put("class_name", comboClass2.getValue());
        
        //File file = new File("C:\\reports\\IndividualStudentFeeRecord.jrxml");
        JasperDesign jasperDesign = JRXmlLoader.load("C:\\reports\\IndividualStudentFeeRecord.jrxml");
        JasperReport jasperReport = JasperCompileManager.compileReport(jasperDesign);
        JasperPrint JasperPrint = (JasperPrint) JasperFillManager.fillReport(jasperReport, parameters2, connection);
        JasperViewer jv = new JasperViewer(JasperPrint, false);
        JasperViewer.viewReport(JasperPrint, false);
       
    }
    //for loading classes name into combobox

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
                Logger.getLogger(FeeCollectionViewController.class.getName()).log(Level.SEVERE, null, ex);
            }

        });

        comboSection1.getItems().clear();
        comboSection1.setItems(sectionName);
    }
    //for loading classes name into combobox

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
                Logger.getLogger(FeeCollectionViewController.class.getName()).log(Level.SEVERE, null, ex);
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
                studentRegNo.clear();
                while (resultSet.next()) {

                    studentRegNo.add(resultSet.getInt("reg_id"));

                }

            } catch (ClassNotFoundException | SQLException ex) {
                Logger.getLogger(FeeCollectionViewController.class.getName()).log(Level.SEVERE, null, ex);
            }

        });

        indivStudentRegNo.getItems().clear();
        indivStudentRegNo.setItems(studentRegNo);
    }
    private void loadFeeTypes(JFXComboBox     comboFeeType) throws SQLException, ClassNotFoundException {
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
