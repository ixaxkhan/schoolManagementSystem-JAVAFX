/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package transport;

import com.jfoenix.controls.JFXButton;
import databaseconnection.DatabaseConnection;
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
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
import javafx.scene.control.ComboBox;
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
import org.controlsfx.control.Notifications;
import student.Registration.StudentController;


public class ViechleAsignToEmployeeViewController implements Initializable {
    @FXML
    private TableColumn<ViechleAsignToEmployeeViewModal, Integer> tbID;
    @FXML
    private TableColumn<ViechleAsignToEmployeeViewModal, String> tbEmpName;
    @FXML
    private TableColumn<ViechleAsignToEmployeeViewModal, String> tbEmpDesignation;
    @FXML
    private TableColumn<ViechleAsignToEmployeeViewModal, String> tbViechleNo;
    @FXML
    private TableColumn<ViechleAsignToEmployeeViewModal, String> tbViechleName;
    @FXML
    private TableColumn<ViechleAsignToEmployeeViewModal, Void> tbUpdate;
    @FXML
    private TableColumn<ViechleAsignToEmployeeViewModal, Void> tbDelete;
    @FXML
    private ComboBox<String> comboViechleNo;
    @FXML
    private ComboBox<String> comboEmpName;
    @FXML
    private TextField txfSearchBox;
    //************** for database connection***********
    Connection connection = null;
    PreparedStatement statement = null;
    ResultSet resultSet = null;
    String sql = null;
    @FXML
    private TableView<ViechleAsignToEmployeeViewModal> table;

    /**
     * Initializes the controller class.
     */
    private void searchingHandler() {
        // 1. Wrap the ObservableList in a FilteredList (initially display all data).
        FilteredList<ViechleAsignToEmployeeViewModal> filteredData = new FilteredList<>(table.getItems(), p -> true);

        // 2. Set the filter Predicate whenever the filter changes.
        txfSearchBox.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredData.setPredicate((Predicate<? super ViechleAsignToEmployeeViewModal>) student -> {
                // If filter text is empty, display all persons.
                if (newValue == null || newValue.isEmpty()) {
                    return true;
                }

                // Compare first name and last name of every person with filter text.
                String lowerCaseFilter = newValue.toLowerCase();

                if (student.getEmpName().toLowerCase().contains(lowerCaseFilter)) {
                    return true; // Filter matches ID.

                } else if (student.getViechleName().toLowerCase().contains(lowerCaseFilter)) {
                    return true; // Filter matches name.
                }
                return false; // Does not match.
            });
        });

        // 3. Wrap the FilteredList in a SortedList. 
        SortedList<ViechleAsignToEmployeeViewModal> sortedData = new SortedList<>(filteredData);

        // 4. Bind the SortedList comparator to the TableView comparator.
        sortedData.comparatorProperty().bind(table.comparatorProperty());

        // 5. Add sorted (and filtered) data to the table.
        table.setItems(sortedData);

    }
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        tbID.setCellValueFactory(new PropertyValueFactory<>("ID"));
        tbEmpName.setCellValueFactory(new PropertyValueFactory<>("EmpName"));
        tbEmpDesignation.setCellValueFactory(new PropertyValueFactory<>("EmpDesignation"));
        tbViechleNo.setCellValueFactory(new PropertyValueFactory<>("ViechleNo"));
        tbViechleName.setCellValueFactory(new PropertyValueFactory<>("ViechleName"));
        try {
            loadDataIntoTable();
             searchingHandler();
            loadViechleNo();
            loadEmployeeName();
            addDeleteToTable(tbDelete,"Delete");
            addUpdateToTable(tbUpdate,"Update");
        } catch (SQLException | ClassNotFoundException ex) {
            Logger.getLogger(ViechleAsignToEmployeeViewController.class.getName()).log(Level.SEVERE, null, ex);
        }
      
    }    

    @FXML
    private void saveAssignment(ActionEvent event)throws SQLException, ClassNotFoundException  {
         int employeePrim=0,viechlePrim=0,  empViechleID=0;
        sql="select * from vehicle where viechle_no=?";
        statement = connection.prepareStatement(sql);
        statement.setString(1, comboViechleNo.getValue());
        resultSet = statement.executeQuery();
        
        if (resultSet.next()) {

           viechlePrim= resultSet.getInt("id");
        }
        sql = "select * from employee where name=?";
        statement = connection.prepareStatement(sql);
        statement.setString(1, comboEmpName.getValue());
        resultSet = statement.executeQuery();

        if (resultSet.next()) {

            employeePrim = resultSet.getInt("reg_no");
            empViechleID=resultSet.getInt("viechle_id");
        }
        if(empViechleID ==viechlePrim){
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Warring Dialog");
            alert.setHeaderText(null);
            alert.setContentText("employee already registerd with  viechle !");
            alert.initModality(Modality.APPLICATION_MODAL);
            alert.show();
        }else{
            sql = "update employee set viechle_id=? where reg_no=?";
            statement = connection.prepareStatement(sql);
            statement.setInt(1, viechlePrim);
            statement.setInt(2, employeePrim);
            statement.execute();
            showNotifications("Addition Viechle to Employee", "Employee add sucessfully  to viechle");
            loadDataIntoTable();
            searchingHandler();
        
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
    private ObservableList list = FXCollections.observableArrayList();

    private void loadDataIntoTable() throws SQLException, ClassNotFoundException {
        connection = DatabaseConnection.getConnection();
        sql = "SELECT * FROM employee,vehicle where employee.viechle_id= vehicle.id ";
        statement = connection.prepareStatement(sql);
      
        resultSet = statement.executeQuery();
        list.clear();
        while (resultSet.next()) {
            
            list.addAll(new ViechleAsignToEmployeeViewModal(resultSet.getInt("employee.reg_no"), resultSet.getString("employee.name"), resultSet.getString("employee.designation"), resultSet.getString("vehicle.viechle_no"),resultSet.getString("vehicle.name")));

        }
        table.setItems(list);
    }
    static ViechleAsignToEmployeeViewModal date;

    private void addUpdateToTable(TableColumn column, String name) {

        Callback<TableColumn<ViechleAsignToEmployeeViewModal, Void>, TableCell<ViechleAsignToEmployeeViewModal, Void>> cellFactory = new Callback<TableColumn<ViechleAsignToEmployeeViewModal, Void>, TableCell<ViechleAsignToEmployeeViewModal, Void>>() {
            @Override
            public TableCell<ViechleAsignToEmployeeViewModal, Void> call(final TableColumn<ViechleAsignToEmployeeViewModal, Void> param) {
                final TableCell<ViechleAsignToEmployeeViewModal, Void> cell = new TableCell<ViechleAsignToEmployeeViewModal, Void>() {

                    JFXButton btn = new JFXButton(name);

                    {
                        btn.setStyle("-fx-background-color: #1d62d1;-fx-text-fill:white; ");
                        btn.setOnAction((ActionEvent event) -> {
                            final Node source = (Node) event.getSource();
                            final Stage primaryStage = (Stage) source.getScene().getWindow();
                            date = getTableView().getItems().get(getIndex());
                            Parent root;
                            try {
                                //  Stage stage=new Stage();
                                root = FXMLLoader.load(getClass().getResource("viechleAsignToEmployeeViewUpdate.fxml"));
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

        Callback<TableColumn<ViechleAsignToEmployeeViewModal, Void>, TableCell<ViechleAsignToEmployeeViewModal, Void>> cellFactory = new Callback<TableColumn<ViechleAsignToEmployeeViewModal, Void>, TableCell<ViechleAsignToEmployeeViewModal, Void>>() {
            @Override
            public TableCell<ViechleAsignToEmployeeViewModal, Void> call(final TableColumn<ViechleAsignToEmployeeViewModal, Void> param) {
                final TableCell<ViechleAsignToEmployeeViewModal, Void> cell = new TableCell<ViechleAsignToEmployeeViewModal, Void>() {

                    JFXButton btn = new JFXButton(name);

                    {
                        btn.setStyle("-fx-background-color: #e02418;-fx-text-fill:white; ");
                        btn.setOnAction((ActionEvent event) -> {
                           
                            date = getTableView().getItems().get(getIndex());
                           
                            try {
                                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                                alert.setTitle("Confirmation Dialog");
                                alert.setHeaderText(null);
                                alert.setContentText("Are you soure to delete Expense Category?");
                                alert.initModality(Modality.APPLICATION_MODAL);
                                Optional<ButtonType> action = alert.showAndWait();
                                if (action.get() == ButtonType.OK) {
                                    connection = DatabaseConnection.getConnection();
                                    sql = "update employee set viechle_id=? where reg_no=?";
                                    statement = connection.prepareStatement(sql);
                                    statement.setString(1, null);
                                    statement.setInt(2, date.getID());
                                    statement.execute();
                                    showNotifications("Route Delation ", "Rout delete sucessfull");
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
 private void loadViechleNo()throws SQLException, ClassNotFoundException{
     connection = DatabaseConnection.getConnection();
     sql = "SELECT * FROM vehicle";
     statement = connection.prepareStatement(sql);
     resultSet = statement.executeQuery();
     comboViechleNo.getItems().clear();
     while (resultSet.next()) {

        comboViechleNo.getItems().addAll(resultSet.getString("viechle_no"));

     }
 
 }
 private void loadEmployeeName()throws SQLException, ClassNotFoundException{
 
     connection = DatabaseConnection.getConnection();
     sql = "SELECT * FROM employee where designation=? or designation=? and leave_status=? ";
     statement = connection.prepareStatement(sql);
     statement.setString(1, "Driver");
     statement.setString(2, "Conductor");
     statement.setInt(3, 0);
    
     resultSet = statement.executeQuery();
     comboEmpName.getItems().clear();
     while (resultSet.next()) {

         comboEmpName.getItems().addAll(resultSet.getString("name"));

     }
 
 
 }

    @FXML
    private void refresh(ActionEvent event) throws SQLException, ClassNotFoundException {
        loadDataIntoTable();
        searchingHandler();
        loadViechleNo();
        loadEmployeeName();
    }
    
}
