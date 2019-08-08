/* #43b8c2
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package student.Registration;

import com.jfoenix.controls.JFXButton;
import databaseconnection.DatabaseConnection;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
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
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.xml.JRXmlLoader;
import net.sf.jasperreports.view.JasperViewer;
import org.controlsfx.control.Notifications;

/**
 *
 * @author khan
 */
public class StudentController  implements Initializable {
    @FXML
    private TableColumn<StudentModel, String> tbStuReg;
    @FXML
    private TableColumn<StudentModel, String> tbStuName;
    @FXML
    private TableColumn<StudentModel, String> tbFatherName;
    @FXML
    private TableColumn<StudentModel, String> tbGender;
    @FXML
    private TableColumn<StudentModel, String> tbDateBith;
    @FXML
    private TableColumn<StudentModel, String> tbPhoneNumber;
    @FXML
    private TableColumn<StudentModel, String> tbEmail;
    @FXML
    private TableColumn<StudentModel, String> tbAddress;
    @FXML
    private TableColumn<StudentModel, Void> tbUpdate;
    @FXML
    private TableColumn<StudentModel, Void> tbDelete;
    @FXML
    private  TableView<StudentModel> table;
    @FXML
    private TextField txfSearchBox;
    @FXML
    private TableColumn<StudentModel, Void> tbView;

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
   public static StudentModel data;

    public static StudentModel getData() {
        return data;
    }
   
    private void addButtonToTable(TableColumn column,String name) {

        Callback<TableColumn<StudentModel, Void>, TableCell<StudentModel, Void>> cellFactory = new Callback<TableColumn<StudentModel, Void>, TableCell<StudentModel, Void>>() {
            @Override
            public TableCell<StudentModel, Void> call(final TableColumn<StudentModel, Void> param) {
                final TableCell<StudentModel, Void> cell = new TableCell<StudentModel, Void>() {

                    private final JFXButton btn = new JFXButton(name);
                    

                    {
                         btn.setStyle("-fx-background-color: #1d62d1;-fx-text-fill:white; ");
                        btn.setOnAction((ActionEvent event) -> {
                            final Node source = (Node) event.getSource();
                            final Stage primaryStage = (Stage) source.getScene().getWindow();
                            data = getTableView().getItems().get(getIndex());
                            Parent root;
                            try {
                                //  Stage stage=new Stage();
                                root = FXMLLoader.load(getClass().getResource("updateView.fxml"));
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
    //************** for database connection***********
    Connection connection = null;
    PreparedStatement statement = null;
    ResultSet resultSet = null;
     String sql = null;
    private void addButtonToTable2(TableColumn column, String name) {

        Callback<TableColumn<StudentModel, Void>, TableCell<StudentModel, Void>> cellFactory = new Callback<TableColumn<StudentModel, Void>, TableCell<StudentModel, Void>>() {
            @Override
            public TableCell<StudentModel, Void> call(final TableColumn<StudentModel, Void> param) {
                final TableCell<StudentModel, Void> cell = new TableCell<StudentModel, Void>() {

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
                                    sql = "delete  from student where reg_id=?";
                                    statement = connection.prepareStatement(sql);
                                    statement.setInt(1, data.getRegNo());
                                    statement.execute();
                                    showNotifications("Student Record","Student delete successfully");
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

        Callback<TableColumn<StudentModel, Void>, TableCell<StudentModel, Void>> cellFactory = new Callback<TableColumn<StudentModel, Void>, TableCell<StudentModel, Void>>() {
            @Override
            public TableCell<StudentModel, Void> call(final TableColumn<StudentModel, Void> param) {
                final TableCell<StudentModel, Void> cell = new TableCell<StudentModel, Void>() {

                    JFXButton btn = new JFXButton(name);

                    {
                        btn.setStyle("-fx-background-color:#43b8c2;-fx-text-fill:white; ");
                        btn.setOnAction((ActionEvent event) -> {

                            data = getTableView().getItems().get(getIndex());

                            try {
                                 int class_id=0,section_id=0;
                                 String class_name=null,section_name=null;
                          
                                    connection = DatabaseConnection.getConnection();
                                    sql = "select * from student where reg_id=?";
                                    statement = connection.prepareStatement(sql);
                                    statement.setInt(1, data.getRegNo());
                                    resultSet=statement.executeQuery();
                                    if(resultSet.next()){
                                    class_id=resultSet.getInt("class_id");
                                    section_id=resultSet.getInt("section_id");
                                    
                                    }
                                    sql="select * from sections where id=?";
                                    statement = connection.prepareStatement(sql);
                                    statement.setInt(1, section_id);
                                    resultSet=statement.executeQuery();
                                    if(resultSet.next()){
                                    
                                    section_name=resultSet.getString("name");
                                    }
                                    sql = "select * from class where id=?";
                                statement = connection.prepareStatement(sql);
                                statement.setInt(1, class_id);
                                resultSet = statement.executeQuery();
                                if (resultSet.next()) {

                                    class_name = resultSet.getString("name");
                                }
                                 Map parameters = new HashMap();
                               
                                parameters.put("class_name", class_name);
                                parameters.put("Section_name", section_name);
                                parameters.put("student_id", data.getRegNo());
                                //File file = new File("src/student/Registration/IndividualStudentRecord.jrxml");
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
    @Override
    public void initialize(URL location, ResourceBundle resources) {
       
        tbStuReg.setCellValueFactory(new PropertyValueFactory<>("regNo"));
        tbStuName.setCellValueFactory(new PropertyValueFactory<>("name"));
        tbFatherName.setCellValueFactory(new PropertyValueFactory<>("fatherName"));
        tbGender.setCellValueFactory(new PropertyValueFactory<>("gender"));
        tbPhoneNumber.setCellValueFactory(new PropertyValueFactory<>("phone"));
        tbEmail.setCellValueFactory(new PropertyValueFactory<>("email"));
        tbAddress.setCellValueFactory(new PropertyValueFactory<>("address"));
        tbDateBith.setCellValueFactory(new PropertyValueFactory<>("date"));
        try {
            loadDataIntoTable();
            searchingHandler();
        } catch (ClassNotFoundException | SQLException ex) {
            Logger.getLogger(StudentController.class.getName()).log(Level.SEVERE, null, ex);
        }
      
        addButtonToTable(tbUpdate,"Update");
        addButtonToTable2(tbDelete,"Delete");
        addViewToTable(tbView,"View");
    }
    public  void loadDataIntoTable() throws ClassNotFoundException, SQLException{
     ObservableList<StudentModel> students = FXCollections.observableArrayList();
        connection = DatabaseConnection.getConnection();
        sql = "SELECT * FROM student";
        statement = connection.prepareStatement(sql);
       resultSet=statement.executeQuery();
       
        while (resultSet.next()) {
            
//int regNo, String name, String fatherName, String gender, String email, String phone, String address
            students.add(new StudentModel(resultSet.getInt("reg_id"),resultSet.getString("name"),resultSet.getString("father_name"),resultSet.getString("gender"),resultSet.getString("email"),resultSet.getString("phone"),resultSet.getString("address"),resultSet.getDate("date_brith")));

        }

        table.setItems(students);
    
    }

    private void searchingHandler() {
        // 1. Wrap the ObservableList in a FilteredList (initially display all data).
        FilteredList<StudentModel> filteredData = new FilteredList<>(table.getItems(), p -> true);

        // 2. Set the filter Predicate whenever the filter changes.
        txfSearchBox.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredData.setPredicate((Predicate<? super StudentModel>) student -> {
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
        SortedList<StudentModel> sortedData = new SortedList<>(filteredData);

        // 4. Bind the SortedList comparator to the TableView comparator.
        sortedData.comparatorProperty().bind(table.comparatorProperty());

        // 5. Add sorted (and filtered) data to the table.
        table.setItems(sortedData);

    } 

    @FXML
    private void represhTable(ActionEvent event) throws ClassNotFoundException, SQLException {
        
        loadDataIntoTable();
        searchingHandler();
    }
}
