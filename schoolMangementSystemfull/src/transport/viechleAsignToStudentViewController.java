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
import student.Registration.StudentModel;
import student.Registration.formController;

/**
 * FXML Controller class
 *
 * @author khan
 */
public class viechleAsignToStudentViewController implements Initializable {
    @FXML
    private TableColumn<viechleAsignToStudentViewModal, Integer> tbID;
    @FXML
    private TableColumn<viechleAsignToStudentViewModal, String> tbStuName;
    @FXML
    private TableColumn<viechleAsignToStudentViewModal, Integer> tbStuRegNo;
    @FXML
    private TableColumn<viechleAsignToStudentViewModal, String> tbStuAddress;
    @FXML
    private TableColumn<viechleAsignToStudentViewModal, String> tbViechleName;
    @FXML
    private TableColumn<viechleAsignToStudentViewModal, Void> tbUpdate;
    @FXML
    private TableColumn<viechleAsignToStudentViewModal, Void> tbDelete;
    @FXML
    private ComboBox<String> comboClass;
    @FXML
    private ComboBox<String> comboSection;
    @FXML
    private ComboBox<Integer> comboRegNo;
    @FXML
    private ComboBox<String> comboViechleNo;
    @FXML
    private TextField txfSearchBox;

    //************** for database connection***********
    Connection connection = null;
    PreparedStatement statement = null;
    ResultSet resultSet = null;
    String sql = null;
      // **************for class and section observable list*******************

    ObservableList<String> classesNames = FXCollections.observableArrayList();
    ObservableList<String> sectionName = FXCollections.observableArrayList();
    ObservableList<Integer> studentRegNo = FXCollections.observableArrayList();
    @FXML
    private TableView<viechleAsignToStudentViewModal> table;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        try {
            showClasses();
            loadSectionsName();
            loadSectionsName();
            loadSectionsStudentRegNo();
            loadViechleNo();
            loadDataIntoTable();
             searchingHandler();
        } catch (ClassNotFoundException | SQLException ex) {
            Logger.getLogger(viechleAsignToStudentViewController.class.getName()).log(Level.SEVERE, null, ex);
        }
        tbID.setCellValueFactory(new PropertyValueFactory<>("ID"));
        tbStuName.setCellValueFactory(new PropertyValueFactory<>("StuName"));
        tbStuRegNo.setCellValueFactory(new PropertyValueFactory<>("StuRegNo"));
        tbStuAddress.setCellValueFactory(new PropertyValueFactory<>("StuAddress"));
        tbViechleName.setCellValueFactory(new PropertyValueFactory<>("ViechleName"));
        addUpdateToTable(tbUpdate,"Update");
        addDeleteToTable(tbDelete,"Delete");
       
        
        
    }    

    @FXML
    private void saveAssignment(ActionEvent event) throws ClassNotFoundException, SQLException {
       String studentViechleId=null;
       int viechle_id=0;
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmation Dialog");
        alert.setHeaderText(null);
        alert.setContentText("Are you soure to save viechle assignment to student?");
        alert.initModality(Modality.APPLICATION_MODAL);
        Optional<ButtonType> action = alert.showAndWait();
        if (action.get() == ButtonType.OK) {
            connection = DatabaseConnection.getConnection();
            sql = "SELECT * FROM student where reg_id=?";
            statement = connection.prepareStatement(sql);
            statement.setInt(1, comboRegNo.getValue());
            resultSet = statement.executeQuery();

            if (resultSet.next()) {
                studentViechleId = resultSet.getString("viechle_id");
            }
            sql = "SELECT * FROM vehicle where viechle_no=?";
            statement = connection.prepareStatement(sql);
            statement.setString(1, comboViechleNo.getValue());
            resultSet = statement.executeQuery();

            if (resultSet.next()) {
                viechle_id = resultSet.getInt("id");
            }

            if (studentViechleId != null) {

                Alert alert2 = new Alert(Alert.AlertType.WARNING);
                alert2.setTitle("Warring Dialog");
                alert2.setHeaderText(null);
                alert2.setContentText("Student already registerd with  viechle !");
                alert2.initModality(Modality.APPLICATION_MODAL);
                alert2.show();

            } else {

                sql = "update student set viechle_id=? where reg_id=?";
                statement = connection.prepareStatement(sql);
                statement.setInt(1, viechle_id);
                statement.setInt(2, comboRegNo.getValue());
                statement.execute();
                showNotifications("Viechle Assignment", "Viechle assign sucessfully");
                loadDataIntoTable();
                clearFields();

            }
        }
    }
   private void clearFields(){
   
     comboClass.setValue("");
     comboSection.setValue("");
    
    comboViechleNo.setValue("");
   
   
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
    public void loadDataIntoTable() throws ClassNotFoundException, SQLException {
        ObservableList<viechleAsignToStudentViewModal> students = FXCollections.observableArrayList();
        connection = DatabaseConnection.getConnection();
        sql = "SELECT * FROM student,vehicle where student.viechle_id=vehicle.id";
        statement = connection.prepareStatement(sql);
        resultSet = statement.executeQuery();

        while (resultSet.next()) {

            students.add(new viechleAsignToStudentViewModal(resultSet.getInt("vehicle.id"), resultSet.getString("student.name"), resultSet.getInt("student.reg_id"), resultSet.getString("student.address"), resultSet.getString("vehicle.name")));

        }

        table.setItems(students);

    }

    private void searchingHandler() {
        // 1. Wrap the ObservableList in a FilteredList (initially display all data).
        FilteredList<viechleAsignToStudentViewModal> filteredData = new FilteredList<>(table.getItems(), p -> true);

        // 2. Set the filter Predicate whenever the filter changes.
        txfSearchBox.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredData.setPredicate((Predicate<? super viechleAsignToStudentViewModal>) student -> {
                // If filter text is empty, display all persons.
                if (newValue == null || newValue.isEmpty()) {
                    return true;
                }

                // Compare first name and last name of every person with filter text.
                String lowerCaseFilter = newValue.toLowerCase();

                if (student.getStuName().toLowerCase().contains(lowerCaseFilter)) {
                    return true; // Filter matches ID.

                } else if (student.getViechleName().toLowerCase().contains(lowerCaseFilter)) {
                    return true; // Filter matches name.
                }
                return false; // Does not match.
            });
        });

        // 3. Wrap the FilteredList in a SortedList. 
        SortedList<viechleAsignToStudentViewModal> sortedData = new SortedList<>(filteredData);

        // 4. Bind the SortedList comparator to the TableView comparator.
        sortedData.comparatorProperty().bind(table.comparatorProperty());

        // 5. Add sorted (and filtered) data to the table.
        table.setItems(sortedData);

    }
    //for loading classes name into combobox
    private void showClasses() throws ClassNotFoundException, SQLException{
       
        
        connection=DatabaseConnection.getConnection();
        sql="select * from class";
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
    private void loadSectionsName(){
     
        comboClass.valueProperty().addListener((observable, oldValue, newValue) -> {
        String classPrimryKey=null;
            try {
                connection = DatabaseConnection.getConnection();
                sql = "select id from class where name= ?";
                statement = connection.prepareStatement(sql);
                statement.setString(1, comboClass.getValue());
                resultSet=statement.executeQuery();
               
                if(resultSet.next()){
                
                classPrimryKey=resultSet.getString("id");
                
                }
                sql="select * from sections where class_id=?";
                statement = connection.prepareStatement(sql);
                statement.setString(1, classPrimryKey);
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
   
    private void loadSectionsStudentRegNo(){

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
                sql = "select * from student where section_id=? and leave_status=?";
                statement = connection.prepareStatement(sql);
                statement.setInt(1, sectionPrimryKey);
                statement.setInt(2, 0);
                resultSet = statement.executeQuery();
                studentRegNo.clear();
                while (resultSet.next()) {

                    studentRegNo.add(resultSet.getInt("reg_id"));

                }

            } catch (ClassNotFoundException | SQLException ex) {
                Logger.getLogger(formController.class.getName()).log(Level.SEVERE, null, ex);
            }

        });

        comboRegNo.getItems().clear();
        comboRegNo.setItems(studentRegNo);
    }
    private void loadViechleNo() throws SQLException, ClassNotFoundException{
        connection = DatabaseConnection.getConnection();
        sql = "select * from vehicle ";
        statement = connection.prepareStatement(sql);
        resultSet = statement.executeQuery();
        comboViechleNo.getItems().clear();
        while (resultSet.next()) {

            comboViechleNo.getItems().add(resultSet.getString("viechle_no"));

        }
    
    }
    
    static viechleAsignToStudentViewModal date;

    private void addUpdateToTable(TableColumn column, String name) {

        Callback<TableColumn<viechleAsignToStudentViewModal, Void>, TableCell<viechleAsignToStudentViewModal, Void>> cellFactory = new Callback<TableColumn<viechleAsignToStudentViewModal, Void>, TableCell<viechleAsignToStudentViewModal, Void>>() {
            @Override
            public TableCell<viechleAsignToStudentViewModal, Void> call(final TableColumn<viechleAsignToStudentViewModal, Void> param) {
                final TableCell<viechleAsignToStudentViewModal, Void> cell = new TableCell<viechleAsignToStudentViewModal, Void>() {

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
                                root = FXMLLoader.load(getClass().getResource("viechleAsignToStudentViewUpdate.fxml"));
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

        Callback<TableColumn<viechleAsignToStudentViewModal, Void>, TableCell<viechleAsignToStudentViewModal, Void>> cellFactory = new Callback<TableColumn<viechleAsignToStudentViewModal, Void>, TableCell<viechleAsignToStudentViewModal, Void>>() {
            @Override
            public TableCell<viechleAsignToStudentViewModal, Void> call(final TableColumn<viechleAsignToStudentViewModal, Void> param) {
                final TableCell<viechleAsignToStudentViewModal, Void> cell = new TableCell<viechleAsignToStudentViewModal, Void>() {

                    JFXButton btn = new JFXButton(name);

                    {
                        btn.setStyle("-fx-background-color: #e02418;-fx-text-fill:white; ");
                        btn.setOnAction((ActionEvent event) -> {

                            date = getTableView().getItems().get(getIndex());

                            try {
                                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                                alert.setTitle("Confirmation Dialog");
                                alert.setHeaderText(null);
                                alert.setContentText("Are you soure to unassign viechle from student?");
                                alert.initModality(Modality.APPLICATION_MODAL);
                                Optional<ButtonType> action = alert.showAndWait();
                                if (action.get() == ButtonType.OK) {
                                    connection = DatabaseConnection.getConnection();
                                    sql = "update student set viechle_id=? where reg_id=? ";
                                    statement = connection.prepareStatement(sql);
                                    statement.setString(1, null);
                                    statement.setInt(2, date.getStuRegNo());
                                    statement.execute();
                                    showNotifications(" Unassignment of Viechle ", "Viechle is unassign successfully from student");
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

    @FXML
    private void refresh(ActionEvent event) throws ClassNotFoundException, SQLException {
        
        loadViechleNo();
        loadDataIntoTable();
        searchingHandler();
    }
}
