/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package employee;

import com.jfoenix.controls.JFXButton;
import databaseconnection.DatabaseConnection;
import static employee.jjjController.data;
import java.io.File;
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
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.xml.JRXmlLoader;
import net.sf.jasperreports.view.JasperViewer;
import org.controlsfx.control.Notifications;
import student.Registration.StudentController;
import student.Registration.StudentModel;

/**
 * FXML Controller class
 *
 * @author khan
 */
public class EmployeeLeaveViewController implements Initializable {
    @FXML
    private TextArea txfReason;
    @FXML
    private TextField txfsearchBox;
    @FXML
    private ComboBox<String> comboRegNo;
    @FXML
    private TableColumn<EmployeeLeaveModal, String> tbRegNo;
    @FXML
    private TableColumn<EmployeeLeaveModal, String> tbName;
    @FXML
    private TableColumn<EmployeeLeaveModal, String> tbFatherName;
    @FXML
    private TableColumn<EmployeeLeaveModal, Date> tbLeaveDate;
    @FXML
    private TableColumn<EmployeeLeaveModal, String> tbLeaveReason;
    @FXML
    private TableColumn<EmployeeLeaveModal, Double> tbSalary;
    @FXML
    private TableColumn<EmployeeLeaveModal, Void> tbDelete;
    @FXML
    private TableColumn<EmployeeLeaveModal, Void> tbView;
    @FXML
    private TableColumn<EmployeeLeaveModal, String> tbGender;
     @FXML
    private TableView<EmployeeLeaveModal> table;
     @FXML
    private DatePicker txfDate;
    //************** for database connection***********
    Connection connection = null;
    PreparedStatement statement = null;
    ResultSet resultSet = null;
    String sql = null;
   
     public static EmployeeLeaveModal data;
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        tbRegNo.setCellValueFactory(new PropertyValueFactory<>("RegNo"));
        tbName.setCellValueFactory(new PropertyValueFactory<>("Name"));
        tbFatherName.setCellValueFactory(new PropertyValueFactory<>("FatherName"));
        tbLeaveDate.setCellValueFactory(new PropertyValueFactory<>("LeaveDate"));
        tbLeaveReason.setCellValueFactory(new PropertyValueFactory<>("LeaveReason"));
        tbSalary.setCellValueFactory(new PropertyValueFactory<>("Salary"));
        tbGender.setCellValueFactory(new PropertyValueFactory<>("Gender"));
        try {
            loadeDataIntoTable();
            loadEmployeeIntoCombo();
            addDeleteToTable(tbDelete,"Delete");
            addViewToTable(tbView,"View");
             searchingHandler();
        } catch (ClassNotFoundException | SQLException ex) {
            Logger.getLogger(EmployeeLeaveViewController.class.getName()).log(Level.SEVERE, null, ex);
        }
       
    }

    @FXML
    private void saveEmployeeLeave(ActionEvent event) throws ClassNotFoundException, SQLException {
        connection = DatabaseConnection.getConnection();
        
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmation Dialog");
        alert.setHeaderText(null);
        alert.setContentText("Are you soure to save student record?");
        alert.initModality(Modality.APPLICATION_MODAL);
        Optional<ButtonType> action = alert.showAndWait();
        if (action.get() == ButtonType.OK) {
            sql = "update employee set leave_date=? ,reason=?,leave_status=? where reg_no=?";
            statement = connection.prepareStatement(sql);
            statement.setDate(1, Date.valueOf(txfDate.getValue()));
            statement.setString(2, txfReason.getText());
            statement.setInt(3, 1);
            statement.setInt(4, Integer.parseInt(comboRegNo.getValue()));
            statement.execute();
            showNotifications("Employee Leave", "Employee leave sucessfully");
            loadeDataIntoTable();
        }
    }
         private void showNotifications(String title, String text) {

                        Notifications.create()
                                .title(title)
                                .text(text)
                                .hideAfter(Duration.seconds(1))
                                .darkStyle()
                                .hideCloseButton()
                                .position(Pos.TOP_LEFT)
                                .showConfirm();

                    }
    
    private void loadeDataIntoTable() throws ClassNotFoundException, SQLException{
    
        ObservableList<EmployeeLeaveModal> employees = FXCollections.observableArrayList();
        connection = DatabaseConnection.getConnection();
        sql = "SELECT * FROM employee where leave_status=?";
        statement = connection.prepareStatement(sql);
        statement.setInt(1, 1);
        resultSet = statement.executeQuery();
        employees.clear();
        while (resultSet.next()) {
          
            employees.add(new EmployeeLeaveModal(resultSet.getInt("reg_no"), resultSet.getString("name"), resultSet.getString("father_name"), resultSet.getDate("leave_date"), resultSet.getString("reason"), resultSet.getDouble("salary"), resultSet.getString("gender")));

        }

        table.setItems(employees);
    
    }
    
   private void loadEmployeeIntoCombo() throws ClassNotFoundException, SQLException{
   
       connection = DatabaseConnection.getConnection();
       sql = "SELECT * FROM employee where leave_status=?";
       statement = connection.prepareStatement(sql);
       statement.setInt(1, 0);
       resultSet = statement.executeQuery();
       
       while (resultSet.next()) {
           comboRegNo.getItems().add(""+resultSet.getInt("reg_no"));
          
       }
   
   }
    private void addDeleteToTable(TableColumn column, String name) {

        Callback<TableColumn<EmployeeLeaveModal, Void>, TableCell<EmployeeLeaveModal, Void>> cellFactory = new Callback<TableColumn<EmployeeLeaveModal, Void>, TableCell<EmployeeLeaveModal, Void>>() {
            @Override
            public TableCell<EmployeeLeaveModal, Void> call(final TableColumn<EmployeeLeaveModal, Void> param) {
                final TableCell<EmployeeLeaveModal, Void> cell = new TableCell<EmployeeLeaveModal, Void>() {

                    JFXButton btn = new JFXButton(name);

                    {
                        btn.setStyle("-fx-background-color: #e02418;-fx-text-fill:white; ");
                        btn.setOnAction((ActionEvent event) -> {

                            data = getTableView().getItems().get(getIndex());

                            try {

                                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                                alert.setTitle("Confirmation Dialog");
                                alert.setHeaderText(null);
                                alert.setContentText("Are you soure to Delete student record?");
                                alert.initModality(Modality.APPLICATION_MODAL);
                                Optional<ButtonType> action = alert.showAndWait();
                                if (action.get() == ButtonType.OK) {
                                    connection = DatabaseConnection.getConnection();
                                    sql = "delete  from employee where reg_no=?";
                                    statement = connection.prepareStatement(sql);
                                    statement.setInt(1, data.getRegNo());
                                    statement.execute();
                                    showNotifications("Employee Record", "Employee delete successfully");
                                    loadeDataIntoTable();
                                    searchingHandler();
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
                                .hideAfter(Duration.seconds(1))
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

    private void addViewToTable(TableColumn column, String name) {

        Callback<TableColumn<EmployeeLeaveModal, Void>, TableCell<EmployeeLeaveModal, Void>> cellFactory = new Callback<TableColumn<EmployeeLeaveModal, Void>, TableCell<EmployeeLeaveModal, Void>>() {
            @Override
            public TableCell<EmployeeLeaveModal, Void> call(final TableColumn<EmployeeLeaveModal, Void> param) {
                final TableCell<EmployeeLeaveModal, Void> cell = new TableCell<EmployeeLeaveModal, Void>() {

                    JFXButton btn = new JFXButton(name);

                    {
                        btn.setStyle("-fx-background-color: #611616;-fx-text-fill:white; ");
                        btn.setOnAction((ActionEvent event) -> {
                            
                            data = getTableView().getItems().get(getIndex());
                            try {
                            Map parameters = new HashMap();
                           connection = DatabaseConnection.getConnection();
                            parameters.put("reg_id", data.getRegNo());
                            File file = new File("src/employee/IndividualEmployeeRecords.jrxml");
                            JasperDesign jasperDesign;
                            jasperDesign = JRXmlLoader.load("C:\\reports\\IndividualEmployeeRecords2.jrxml");
                            JasperReport jasperReport = JasperCompileManager.compileReport(jasperDesign);
                            JasperPrint JasperPrint = (JasperPrint) JasperFillManager.fillReport(jasperReport, parameters, connection);
                            JasperViewer jv = new JasperViewer(JasperPrint, false);

                            JasperViewer.viewReport(JasperPrint, false);
                            } catch (JRException ex) {
                                Logger.getLogger(jjjController.class.getName()).log(Level.SEVERE, null, ex);
                            } catch (ClassNotFoundException ex) {
                                Logger.getLogger(EmployeeLeaveViewController.class.getName()).log(Level.SEVERE, null, ex);
                            }

                        });
                    }

                    private void showNotifications(String title, String text) {

                        Notifications.create()
                                .title(title)
                                .text(text)
                                .hideAfter(Duration.seconds(1))
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
    private void searchingHandler(){
    
        // 1. Wrap the ObservableList in a FilteredList (initially display all data).
        FilteredList<EmployeeLeaveModal> filteredData = new FilteredList<>(table.getItems(), p -> true);

        // 2. Set the filter Predicate whenever the filter changes.
        txfsearchBox.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredData.setPredicate((Predicate<? super EmployeeLeaveModal>) student -> {
                // If filter text is empty, display all persons.
                if (newValue == null || newValue.isEmpty()) {
                    return true;
                }

                // Compare first name and last name of every person with filter text.
                String lowerCaseFilter = newValue.toLowerCase();

                if (student.getFatherName().toLowerCase().contains(lowerCaseFilter)) {
                    return true; // Filter matches ID.

                } else if (student.getName().toLowerCase().contains(lowerCaseFilter)) {
                    return true; // Filter matches name.
                }
                return false; // Does not match.
            });
        });

        // 3. Wrap the FilteredList in a SortedList. 
        SortedList<EmployeeLeaveModal> sortedData = new SortedList<>(filteredData);

        // 4. Bind the SortedList comparator to the TableView comparator.
        sortedData.comparatorProperty().bind(table.comparatorProperty());

        // 5. Add sorted (and filtered) data to the table.
        table.setItems(sortedData);
    
    
    }

    @FXML
    private void refresh(ActionEvent event) throws ClassNotFoundException, SQLException {
        loadeDataIntoTable();
        loadEmployeeIntoCombo();
        searchingHandler();
    }
    
}
