/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package employee;

import com.jfoenix.controls.JFXButton;
import databaseconnection.DatabaseConnection;
import java.awt.Dimension;
import java.awt.Toolkit;
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
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.effect.BoxBlur;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
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
import static student.Registration.StudentController.data;
import student.Registration.StudentLeaveModal;
import student.Registration.StudentModel;

/**
 * FXML Controller class
 *
 * @author khan
 */
public class jjjController implements Initializable {
    @FXML
    private TextField txfSearxhBox;
    @FXML
    private TableView<jjjViewModal> table;
    @FXML
    private TableColumn<jjjViewModal, Integer> tbRegNo;
    @FXML
    private TableColumn<jjjViewModal, String> tbName;
    @FXML
    private TableColumn<jjjViewModal, String> tbFatherName;
    @FXML
    private TableColumn<jjjViewModal, Date> tbDateJoin;
    @FXML
    private TableColumn<jjjViewModal,String> tbGender;
    @FXML
    private TableColumn<jjjViewModal, String> tbAddress;
    @FXML
    private TableColumn<jjjViewModal,String> tbPhone;
    @FXML
    private TableColumn<jjjViewModal, String> tbEmail;
    @FXML
    private TableColumn<jjjViewModal, String> tbDesignation;
    @FXML
    private TableColumn<jjjViewModal, String> tbExperience;
    @FXML
    private TableColumn<jjjViewModal, String> tbQualifiction;
    @FXML
    private TableColumn<jjjViewModal, Double> tbSalary;
    @FXML
    private TableColumn<jjjViewModal, Void> tbUpdate;
    @FXML
    private TableColumn<jjjViewModal, Void> tbDelete;
    @FXML
    private TableColumn<jjjViewModal, Void> tbView;
    //************** for database connection***********
    Connection connection = null;
    PreparedStatement statement = null;
    ResultSet resultSet = null;
    String sql = null;
    public static jjjViewModal data;
    @Override
    public void initialize(URL url, ResourceBundle rb) {
      tbRegNo.setCellValueFactory(new PropertyValueFactory<>("RegNo"));
        tbName.setCellValueFactory(new PropertyValueFactory<>("Name"));
        tbFatherName.setCellValueFactory(new PropertyValueFactory<>("FatherName"));
        tbGender.setCellValueFactory(new PropertyValueFactory<>("Gender"));
        tbPhone.setCellValueFactory(new PropertyValueFactory<>("Phone"));
        tbEmail.setCellValueFactory(new PropertyValueFactory<>("Email"));
        tbAddress.setCellValueFactory(new PropertyValueFactory<>("Address"));
        tbDateJoin.setCellValueFactory(new PropertyValueFactory<>("date"));
        tbExperience.setCellValueFactory(new PropertyValueFactory<>("Experience"));
        tbQualifiction.setCellValueFactory(new PropertyValueFactory<>("Qualifiction"));
        tbDesignation.setCellValueFactory(new PropertyValueFactory<>("Designation"));
        tbSalary.setCellValueFactory(new PropertyValueFactory<>("Salary"));
        addUpdateToTable( tbUpdate,"Update");
        addDeleteToTable(tbDelete,"Delete");
        addViewToTable(tbView, "View");
        try {
            loadDataIntoTable();
            searchingHandler();
        } catch (ClassNotFoundException | SQLException ex) {
            Logger.getLogger(jjjController.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    
    }    

    @FXML
    private void backToSystemSetting(ActionEvent event) throws IOException {
        Parent root;
        root = FXMLLoader.load(getClass().getResource("/home/dashbord.fxml"));
        final Node source = (Node) event.getSource();
        Stage stage = (Stage) source.getScene().getWindow();

        Scene sceen = new Scene(root);

        stage.setScene(sceen);
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        stage.setWidth(screenSize.getWidth());
        stage.setHeight(screenSize.getHeight() - 30);
        stage.show();
    }
    private void addUpdateToTable(TableColumn column, String name) {

        Callback<TableColumn<jjjViewModal, Void>, TableCell<jjjViewModal, Void>> cellFactory = new Callback<TableColumn<jjjViewModal, Void>, TableCell<jjjViewModal, Void>>() {
            @Override
            public TableCell<jjjViewModal, Void> call(final TableColumn<jjjViewModal, Void> param) {
                final TableCell<jjjViewModal, Void> cell = new TableCell<jjjViewModal, Void>() {

                    JFXButton btn = new JFXButton(name);

                    {
                        
                        btn.setStyle("-fx-background-color: #1d62d1;-fx-text-fill:white; ");
                        btn.setOnAction((ActionEvent event) -> {
                            final Node source = (Node) event.getSource();
                            final Stage primaryStage = (Stage) source.getScene().getWindow();
                            data = getTableView().getItems().get(getIndex());
                            Parent root;
                            try {
                                //  Stage stage=new Stage();
                                root = FXMLLoader.load(getClass().getResource("employeeUpdateView.fxml"));
                                final Stage dialog = new Stage(StageStyle.UNDECORATED);
                                dialog.initModality(Modality.WINDOW_MODAL);
                                dialog.initOwner(primaryStage);

                                Scene scene = new Scene(root);
                                dialog.setScene(scene);
                                BoxBlur bb = new BoxBlur();
                                bb.setWidth(5);
                                bb.setHeight(5);
                                bb.setIterations(3);

                                dialog.show();
                            } catch (IOException ex) {
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
   
    

   
    private void addDeleteToTable(TableColumn column, String name) {

        Callback<TableColumn<jjjViewModal, Void>, TableCell<jjjViewModal, Void>> cellFactory = new Callback<TableColumn<jjjViewModal, Void>, TableCell<jjjViewModal, Void>>() {
            @Override
            public TableCell<jjjViewModal, Void> call(final TableColumn<jjjViewModal, Void> param) {
                final TableCell<jjjViewModal, Void> cell = new TableCell<jjjViewModal, Void>() {

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
                                    loadDataIntoTable();
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

        Callback<TableColumn<jjjViewModal, Void>, TableCell<jjjViewModal, Void>> cellFactory = new Callback<TableColumn<jjjViewModal, Void>, TableCell<jjjViewModal, Void>>() {
            @Override
            public TableCell<jjjViewModal, Void> call(final TableColumn<jjjViewModal, Void> param) {
                final TableCell<jjjViewModal, Void> cell = new TableCell<jjjViewModal, Void>() {

                    JFXButton btn = new JFXButton(name);

                    {
                        btn.setStyle("-fx-background-color:#43b8c2;-fx-text-fill:white; ");
                        btn.setOnAction((ActionEvent event) -> {
                            data = getTableView().getItems().get(getIndex());
                            try {
                                Map parameters = new HashMap();

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
    private void loadDataIntoTable() throws ClassNotFoundException, SQLException{
        ObservableList<jjjViewModal> employees = FXCollections.observableArrayList();
        connection = DatabaseConnection.getConnection();
        sql = "SELECT * FROM employee where leave_status=?";
        statement = connection.prepareStatement(sql);
        statement.setInt(1, 0);
        resultSet = statement.executeQuery();
        employees.clear();
        while(resultSet.next()){
            //int RegNo, String Name, String FatherName, String Gender, String Address, String Phone, String Email, String Designation, String Experience, String Qualifiction, double Salary
           employees.add(new jjjViewModal( resultSet.getInt("reg_no"),resultSet.getString("name"),resultSet.getString("father_name"), resultSet.getString("gender"),resultSet.getString("address"),   resultSet.getString("phone"),    resultSet.getString("email"), resultSet.getString("designation"),  resultSet.getString("experience"), resultSet.getString("qualification"), resultSet.getDouble("salary"),resultSet.getDate("join_date")    ));
        
        }
    
    table.setItems(employees);
    } 
    
   private void searchingHandler(){
   
       // 1. Wrap the ObservableList in a FilteredList (initially display all data).
       FilteredList<jjjViewModal> filteredData = new FilteredList<>(table.getItems(), p -> true);

       // 2. Set the filter Predicate whenever the filter changes.
       txfSearxhBox.textProperty().addListener((observable, oldValue, newValue) -> {
           filteredData.setPredicate((Predicate<? super jjjViewModal>) student -> {
               // If filter text is empty, display all persons.
               if (newValue == null || newValue.isEmpty()) {
                   return true;
               }

               // Compare first name and last name of every person with filter text.
               String lowerCaseFilter = newValue.toLowerCase();

               if (student.getPhone().toLowerCase().contains(lowerCaseFilter)) {
                   return true; // Filter matches ID.

               } else if (student.getName().toLowerCase().contains(lowerCaseFilter)) {
                   return true; // Filter matches name.
               }
               return false; // Does not match.
           });
       });

       // 3. Wrap the FilteredList in a SortedList. 
       SortedList<jjjViewModal> sortedData = new SortedList<>(filteredData);

       // 4. Bind the SortedList comparator to the TableView comparator.
       sortedData.comparatorProperty().bind(table.comparatorProperty());

       // 5. Add sorted (and filtered) data to the table.
       table.setItems(sortedData);
   
   }

    @FXML
    private void refresh(ActionEvent event) throws ClassNotFoundException, SQLException {
        
        loadDataIntoTable();
        searchingHandler();
    }
    
}
