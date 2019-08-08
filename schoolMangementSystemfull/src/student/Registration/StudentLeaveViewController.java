/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package student.Registration;

import com.jfoenix.controls.JFXButton;
import databaseconnection.DatabaseConnection;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.function.Predicate;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;

import javafx.fxml.Initializable;
import javafx.geometry.Pos;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;

import javafx.stage.Modality;

import javafx.util.Callback;
import javafx.util.Duration;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.xml.JRXmlLoader;
import net.sf.jasperreports.view.JasperViewer;
import org.controlsfx.control.Notifications;


/**
 * FXML Controller class
 *
 * @author khan
 */
public class StudentLeaveViewController implements Initializable {
    @FXML
    private ComboBox<String> comboClass;
    @FXML
    private ComboBox<String> comboSection;
    @FXML
    private ComboBox<Integer> comboRegistrationNo;
    @FXML
    private TextArea txfLeavingReson;
    @FXML
    private TableView<StudentLeaveModal> table;
    @FXML
    private TableColumn<StudentLeaveModal, String> tbName;
    @FXML
    private TableColumn<StudentLeaveModal, String> tbFatherName;
    @FXML
    private TableColumn<StudentLeaveModal, String> tbGender;
    @FXML
    private TableColumn<StudentLeaveModal, String> tbReson;
    
    @FXML
    private TableColumn<StudentLeaveModal, String> tbclass;
    @FXML
    private TableColumn<StudentLeaveModal, Integer> tbRegNo;
    @FXML
    private TableColumn<StudentLeaveModal, String> tbAddress;
    @FXML
    private TableColumn<StudentLeaveModal, Void> tbDelete;
    @FXML
    private TableColumn<StudentLeaveModal, Void> tbView;
    @FXML
    private TableColumn<StudentLeaveModal, Date> tbDate;

    
    //************** for database connection***********
    Connection connection = null;
    PreparedStatement statement = null;
    ResultSet resultSet = null;
    String sql = null;
    // **************for class and section observable list*******************
    ObservableList<String> classesNames = FXCollections.observableArrayList();
    ObservableList<String> sectionName = FXCollections.observableArrayList();
    ObservableList<Integer> registrationsNo = FXCollections.observableArrayList();
    @FXML
    private DatePicker txfDate;
    @FXML
    private TextField searchBox;
  
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
       
        tbRegNo.setCellValueFactory(new PropertyValueFactory<>("regNo"));
        tbName.setCellValueFactory(new PropertyValueFactory<>("name"));
        tbFatherName.setCellValueFactory(new PropertyValueFactory<>("fatherName"));
        tbGender.setCellValueFactory(new PropertyValueFactory<>("gender"));
        tbclass.setCellValueFactory(new PropertyValueFactory<>("className"));
        tbReson.setCellValueFactory(new PropertyValueFactory<>("reason"));
        tbAddress.setCellValueFactory(new PropertyValueFactory<>("address"));
        tbDate.setCellValueFactory(new PropertyValueFactory<>("date"));
        try { 
            showClasses();
            loadSectionsName();
            loadSectionsName();
            loadRegistrationNo();
            loadRegistrationNo();
            addViewButtonToTable(tbView,"View");
            addDeleteButtonToTable(tbDelete,"Delete");
            loadDataIntoTable();
            searchingHandler();
        } catch (ClassNotFoundException | SQLException ex) {
            Logger.getLogger(StudentLeaveViewController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }    

    @FXML
    private void save(ActionEvent event) throws ClassNotFoundException, SQLException {
        int classPrimarykey=0;
        String className=null;
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmation Dialog");
        alert.setHeaderText(null);
        alert.setContentText("Are you soure to Delete student record?");
        alert.initModality(Modality.APPLICATION_MODAL);
        Optional<ButtonType> action = alert.showAndWait();
        if (action.get() == ButtonType.OK) {
            connection = DatabaseConnection.getConnection();
            sql = "select * from student where reg_id=? ";
            statement = connection.prepareStatement(sql);
            statement.setInt(1, comboRegistrationNo.getValue());
            resultSet = statement.executeQuery();
            if (resultSet.next()) {

                classPrimarykey = resultSet.getInt("class_id");

            }
            sql = "select * from class where id=? ";
            statement = connection.prepareStatement(sql);
            statement.setInt(1, classPrimarykey);
            resultSet = statement.executeQuery();
            if (resultSet.next()) {

                className = resultSet.getString("name");

            }

            sql = "update student set  reason=?,leave_class=?,leave_date=?,leave_status=? where reg_id=?";
            statement = connection.prepareStatement(sql);
           
            statement.setString(1, txfLeavingReson.getText());
            statement.setString(2, className);
            statement.setDate(3, Date.valueOf(txfDate.getValue()));
            statement.setInt(4, 1);
            statement.setInt(5, comboRegistrationNo.getValue());
            statement.execute();
            loadDataIntoTable();
            searchingHandler();
            clearFields();
            showNotifications("Student Record", "Student delete successfully");
        }
        
    }
    
    private void clearFields(){
        comboClass.setValue(null);
        comboSection.setValue(null);
        comboRegistrationNo.setValue(null);;
        txfLeavingReson.setText(null);;
        txfDate.setValue(null);
    
    
    }
    //for loading classes name into combobox

    private void showClasses() throws ClassNotFoundException, SQLException {

        connection = DatabaseConnection.getConnection();
        sql = "select * from class";
        statement = connection.prepareStatement(sql);

        resultSet = statement.executeQuery();
        classesNames.clear();
        while (resultSet.next()) {
            classesNames.add(resultSet.getString("name"));

        }
        comboClass.getItems().clear();
        comboClass.setItems(classesNames);

    }

    // this fuction is used to load respective sections of class
    private void loadSectionsName() {

        comboClass.valueProperty().addListener((observable, oldValue, newValue) -> {
            int classPrimryKey =0;
            try {
                connection = DatabaseConnection.getConnection();
                sql = "select id from class where name= ?";
                statement = connection.prepareStatement(sql);
                statement.setString(1, newValue);
                resultSet = statement.executeQuery();

                if (resultSet.next()) {

                    classPrimryKey = resultSet.getInt("id");

                }
                sql = "select * from sections where class_id=?";
                statement = connection.prepareStatement(sql);
                statement.setInt(1, classPrimryKey);
                resultSet = statement.executeQuery();
                sectionName.clear();
                while (resultSet.next()) {

                    sectionName.add(resultSet.getString("name"));

                }

            } catch (ClassNotFoundException | SQLException ex) {
                Logger.getLogger(formController.class.getName()).log(Level.SEVERE, null, ex);
            }

        });

        comboSection.getItems().clear();
        comboSection.setItems(sectionName);
    }
    private void loadRegistrationNo() throws ClassNotFoundException, SQLException{
    
        comboSection.valueProperty().addListener((observable, oldValue, newValue) -> {
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
                registrationsNo.clear();
                while (resultSet.next()) {
                    
                    registrationsNo.add(resultSet.getInt("reg_id"));

                }

            } catch (ClassNotFoundException | SQLException ex) {
                Logger.getLogger(formController.class.getName()).log(Level.SEVERE, null, ex);
            }

        });
comboRegistrationNo.getItems().clear();
comboRegistrationNo.setItems(registrationsNo);
    
    }
    private void searchingHandler() {
        // 1. Wrap the ObservableList in a FilteredList (initially display all data).
        FilteredList<StudentLeaveModal> filteredData = new FilteredList<>(table.getItems(), p -> true);

        // 2. Set the filter Predicate whenever the filter changes.
        searchBox.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredData.setPredicate((Predicate<? super StudentLeaveModal>) student -> {
                // If filter text is empty, display all persons.
                if (newValue == null || newValue.isEmpty()) {
                    return true;
                }

                // Compare first name and last name of every person with filter text.
                String lowerCaseFilter = newValue.toLowerCase();

                if (student.name.toLowerCase().contains(lowerCaseFilter)) {
                    return true; // Filter matches ID.

                } else if (student.fatherName.toLowerCase().contains(lowerCaseFilter)) {
                    return true; // Filter matches name.
                }
                return false; // Does not match.
            });
        });

        // 3. Wrap the FilteredList in a SortedList. 
        SortedList<StudentLeaveModal> sortedData = new SortedList<>(filteredData);

        // 4. Bind the SortedList comparator to the TableView comparator.
        sortedData.comparatorProperty().bind(table.comparatorProperty());

        // 5. Add sorted (and filtered) data to the table.
        table.setItems(sortedData);

    }
    public static StudentLeaveModal data;
        private void addViewButtonToTable(TableColumn column, String name) {

        Callback<TableColumn<StudentLeaveModal, Void>, TableCell<StudentLeaveModal, Void>> cellFactory = new Callback<TableColumn<StudentLeaveModal, Void>, TableCell<StudentLeaveModal, Void>>() {
            @Override
            public TableCell<StudentLeaveModal, Void> call(final TableColumn<StudentLeaveModal, Void> param) {
                final TableCell<StudentLeaveModal, Void> cell = new TableCell<StudentLeaveModal, Void>() {

                    private final JFXButton btn = new JFXButton(name);

                    {
                        btn.setStyle("-fx-background-color: #1d62d1;-fx-text-fill:white; ");
                        btn.setOnAction((ActionEvent event) -> {
                            data = getTableView().getItems().get(getIndex());
                            try {
                                int class_id = 0, section_id = 0;
                                String class_name = null, section_name = null;
                                connection = DatabaseConnection.getConnection();
                                sql = "select * from student where reg_id=?";
                                statement = connection.prepareStatement(sql);
                                statement.setInt(1, data.getRegNo());
                                resultSet = statement.executeQuery();
                                if (resultSet.next()) {
                                    class_id = resultSet.getInt("class_id");
                                    section_id = resultSet.getInt("section_id");

                                }
                                sql = "select * from sections where id=?";
                                statement = connection.prepareStatement(sql);
                                statement.setInt(1, section_id);
                                resultSet = statement.executeQuery();
                                if (resultSet.next()) {

                                    section_name = resultSet.getString("name");
                                }
                                sql = "select * from class where id=?";
                                statement = connection.prepareStatement(sql);
                                statement.setInt(1, class_id);
                                resultSet = statement.executeQuery();
                                if (resultSet.next()) {

                                    class_name = resultSet.getString("name");
                                }
                            
                                Map parameters = new HashMap();

                                parameters.put("class_name",  class_name);
                                parameters.put("Section_name", section_name);
                                parameters.put("student_id", data.getRegNo());
                                File file = new File("src/student/Registration/IndividualStudentRecord2.jrxml");
                                JasperDesign jasperDesign = JRXmlLoader.load("C:\\reports\\IndividualStudentRecord2.jrxml");
                                JasperReport jasperReport = JasperCompileManager.compileReport(jasperDesign);
                                JasperPrint JasperPrint = (JasperPrint) JasperFillManager.fillReport(jasperReport, parameters, connection);
                                JasperViewer jv = new JasperViewer(JasperPrint, false);
                                JasperViewer.viewReport(JasperPrint, false);

                            } catch (Exception ex) {
                                Logger.getLogger(StudentController.class.getName()).log(Level.SEVERE, null, ex);
                            }

                        });
                    }

                    @Override
                    public void updateItem(Void item, boolean empty) {
                        super.updateItem(item, empty);
                        //setGraphic(btn);
                        if (empty) {
                            setGraphic(null);
                        } else {
                            setGraphic(btn);
                        }
                    }
                };
                return cell;
            }
        };

        column.setCellFactory(cellFactory);

    }
    private void addDeleteButtonToTable(TableColumn column, String name) {

        Callback<TableColumn<StudentLeaveModal, Void>, TableCell<StudentLeaveModal, Void>> cellFactory = new Callback<TableColumn<StudentLeaveModal, Void>, TableCell<StudentLeaveModal, Void>>() {
            @Override
            public TableCell<StudentLeaveModal, Void> call(final TableColumn<StudentLeaveModal, Void> param) {
                final TableCell<StudentLeaveModal, Void> cell = new TableCell<StudentLeaveModal, Void>() {

                    private final JFXButton btn = new JFXButton(name);

                    {
                       btn.setStyle("-fx-background-color: #e02418;-fx-text-fill:white; ");
                        btn.setOnAction((ActionEvent event) -> {;

                            StudentLeaveModal data = getTableView().getItems().get(getIndex());
                            try {

                                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                                alert.setTitle("Confirmation Dialog");
                                alert.setHeaderText(null);
                                alert.setContentText("Are you soure to Delete student record?");
                                alert.initModality(Modality.APPLICATION_MODAL);
                                Optional<ButtonType> action = alert.showAndWait();
                                if (action.get() == ButtonType.OK) {
                                    connection = DatabaseConnection.getConnection();
                                    sql = "delete  from student where reg_id=?";
                                    statement = connection.prepareStatement(sql);
                                    statement.setInt(1, data.getRegNo());
                                    statement.execute();
                                    showNotifications("Student Record", "Student delete successfully");
                                    loadDataIntoTable();
                                }
                            } catch (Exception ex) {
                                Logger.getLogger(StudentController.class.getName()).log(Level.SEVERE, null, ex);
                            }
                        });
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

                    @Override
                    public void updateItem(Void item, boolean empty) {
                        super.updateItem(item, empty);
                        //setGraphic(btn);
                        if (empty) {
                            setGraphic(null);
                        } else {
                            setGraphic(btn);
                        }
                    }
                };
                return cell;
            }
        };

        column.setCellFactory(cellFactory);

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
   public void loadDataIntoTable() throws ClassNotFoundException, SQLException{
       ObservableList<StudentLeaveModal> students = FXCollections.observableArrayList();
       connection = DatabaseConnection.getConnection();
       sql = "SELECT * FROM student where leave_status=?";
       statement = connection.prepareStatement(sql);
       statement.setInt(1,1);
       resultSet = statement.executeQuery();

       while (resultSet.next()) {
        
           students.add(new StudentLeaveModal(resultSet.getInt("reg_id"),resultSet.getString("name"),resultSet.getString("gender") ,resultSet.getString("father_name") ,resultSet.getString("leave_class") ,resultSet.getString("reason") ,resultSet.getString("address") ,resultSet.getDate("leave_date")    ));

       }

       table.setItems(students);
   
   
   } 

    @FXML
    private void refresh(ActionEvent event) throws ClassNotFoundException, SQLException {
       
        loadDataIntoTable();
        searchingHandler();
    }
    
     
    
}


