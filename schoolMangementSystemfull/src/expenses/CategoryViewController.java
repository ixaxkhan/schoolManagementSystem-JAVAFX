/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package expenses;

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
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.DatePicker;
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
import student.Registration.StudentController;
import static student.Registration.StudentController.data;
import student.Registration.StudentModel;


/**
 * FXML Controller class
 *
 * @author khan
 */
public class CategoryViewController implements Initializable {
    
    @FXML
    private DatePicker txfDate;
    @FXML
    private TextField txfCategory;
    @FXML
    private TableColumn<CategoryViewModal, Integer> tbExpenseID;
    @FXML
    private TableColumn<CategoryViewModal, String> tbExpenseName;
    @FXML
    private TableColumn<CategoryViewModal, Date> tbDate;
    @FXML
    private TableColumn<CategoryViewModal, Void> tbUpdate;
    @FXML
    private TableColumn<CategoryViewModal, Void> tbDelete;
    @FXML
    private TableColumn<CategoryViewModal, Void> tbView;
    //************** for database connection***********
    Connection connection = null;
    PreparedStatement statement = null;
    ResultSet resultSet = null;
    String sql = null;
    @FXML
    private TableView<CategoryViewModal> table;
    @FXML
    private TextField txfSearchField;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        tbExpenseID.setCellValueFactory(new PropertyValueFactory<>("tbExpenseID"));
        tbExpenseName.setCellValueFactory(new PropertyValueFactory<>("tbExpenseName"));
        tbDate.setCellValueFactory(new PropertyValueFactory<>("tbDate"));
        try {
            loadDataIntoTable();
            searchingHandler();
            addUpdateToTable(tbUpdate,"Update");
            addDeleteToTable(tbDelete,"Date");
            addViewToTable(tbView,"View");
        } catch (SQLException | ClassNotFoundException ex) {
            Logger.getLogger(CategoryViewController.class.getName()).log(Level.SEVERE, null, ex);
        }
        
       
    }    
   
    @FXML
    private void save(ActionEvent event) throws SQLException, ClassNotFoundException {
        
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmation Dialog");
        alert.setHeaderText(null);
        alert.setContentText("Are you soure to add expense category?");
        alert.initModality(Modality.APPLICATION_MODAL);
        Optional<ButtonType> action = alert.showAndWait();
        if (action.get() == ButtonType.OK) {
            connection = DatabaseConnection.getConnection();
            sql = "INSERT INTO expense_type (category,date) VALUES(?,?)";
            statement = connection.prepareStatement(sql);
            statement.setString(1, txfCategory.getText());
            statement.setDate(2, Date.valueOf(txfDate.getValue()));
            statement.execute();
            showNotifications("Expense Category", "Expense Category add sucessfully");
            clearFields();
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
    private void clearFields(){
    
     txfDate.setValue(null);
    txfCategory.setText("");
    
    }
    static CategoryViewModal date;
    private void addUpdateToTable(TableColumn column, String name) {

        Callback<TableColumn<CategoryViewModal, Void>, TableCell<CategoryViewModal, Void>> cellFactory = new Callback<TableColumn<CategoryViewModal, Void>, TableCell<CategoryViewModal, Void>>() {
            @Override
            public TableCell<CategoryViewModal, Void> call(final TableColumn<CategoryViewModal, Void> param) {
                final TableCell<CategoryViewModal, Void> cell = new TableCell<CategoryViewModal, Void>() {

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
                                root = FXMLLoader.load(getClass().getResource("categoryUpdateView.fxml"));
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
 private ObservableList list =FXCollections.observableArrayList();
   private void loadDataIntoTable()throws SQLException, ClassNotFoundException {
       connection = DatabaseConnection.getConnection();
       sql = "select * from expense_type";
       statement = connection.prepareStatement(sql);
       resultSet=statement.executeQuery();
      list.clear();
      while(resultSet.next()){
      
      list.addAll( new CategoryViewModal(resultSet.getInt("id") ,resultSet.getString("category") ,resultSet.getDate("date")  ) );
              
      
      }
   table.setItems(list);
   } 
   
    private void addDeleteToTable(TableColumn column, String name) {

        Callback<TableColumn<CategoryViewModal, Void>, TableCell<CategoryViewModal, Void>> cellFactory = new Callback<TableColumn<CategoryViewModal, Void>, TableCell<CategoryViewModal, Void>>() {
            @Override
            public TableCell<CategoryViewModal, Void> call(final TableColumn<CategoryViewModal, Void> param) {
                final TableCell<CategoryViewModal, Void> cell = new TableCell<CategoryViewModal, Void>() {

                    JFXButton btn = new JFXButton(name);

                    {  
                        btn.setStyle("-fx-background-color: #e02418;-fx-text-fill:white; ");
                        btn.setOnAction((ActionEvent event) -> {
                            date = getTableView().getItems().get(getIndex());
                                final Node source = (Node) event.getSource();
                            final Stage primaryStage = (Stage) source.getScene().getWindow();
                              date = getTableView().getItems().get(getIndex());
                            Parent root;
                            try {
                                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                                alert.setTitle("Confirmation Dialog");
                                alert.setHeaderText(null);
                                alert.setContentText("Are you soure to delete Expense Category?");
                                alert.initModality(Modality.APPLICATION_MODAL);
                                Optional<ButtonType> action = alert.showAndWait();
                                if (action.get() == ButtonType.OK) {
                                    connection = DatabaseConnection.getConnection();
                                    sql = "delete  from expense_type where id=? ";
                                    statement = connection.prepareStatement(sql);
                                    statement.setInt(1, date.getTbExpenseID());
                                    statement.execute();
                                    showNotifications("Expense Category ", "Expense Category delete sucessfull");
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

        Callback<TableColumn<CategoryViewModal, Void>, TableCell<CategoryViewModal, Void>> cellFactory = new Callback<TableColumn<CategoryViewModal, Void>, TableCell<CategoryViewModal, Void>>() {
            @Override
            public TableCell<CategoryViewModal, Void> call(final TableColumn<CategoryViewModal, Void> param) {
                final TableCell<CategoryViewModal, Void> cell = new TableCell<CategoryViewModal, Void>() {

                    JFXButton btn = new JFXButton(name);

                    {
                        btn.setStyle("-fx-background-color:#43b8c2;-fx-text-fill:white; ");
                        btn.setOnAction((ActionEvent event) -> {

                            date = getTableView().getItems().get(getIndex());

                            try {

                                connection = DatabaseConnection.getConnection();
                                Map parameters = new HashMap();

                                parameters.put("category_id", date.getTbExpenseID());

                                File file = new File("src/expenses/expenseCategoriesReport.jrxml");
                                JasperDesign jasperDesign = JRXmlLoader.load("C:\\reports\\expenseCategoriesReport.jrxml");
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

    private void searchingHandler() {
        // 1. Wrap the ObservableList in a FilteredList (initially display all data).
        FilteredList<CategoryViewModal> filteredData = new FilteredList<>(table.getItems(), p -> true);

        // 2. Set the filter Predicate whenever the filter changes.
        txfSearchField.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredData.setPredicate((Predicate<? super CategoryViewModal>) student -> {
                // If filter text is empty, display all persons.
                if (newValue == null || newValue.isEmpty()) {
                    return true;
                }

                // Compare first name and last name of every person with filter text.
                String lowerCaseFilter = newValue.toLowerCase();

                if (student.getTbExpenseName().toLowerCase().contains(lowerCaseFilter)) {
                    return true; // Filter matches ID.

                } else if (student.getTbExpenseName().toLowerCase().contains(lowerCaseFilter)) {
                    return true; // Filter matches name.
                }
                return false; // Does not match.
            });
        });

        // 3. Wrap the FilteredList in a SortedList. 
        SortedList<CategoryViewModal> sortedData = new SortedList<>(filteredData);

        // 4. Bind the SortedList comparator to the TableView comparator.
        sortedData.comparatorProperty().bind(table.comparatorProperty());

        // 5. Add sorted (and filtered) data to the table.
        table.setItems(sortedData);

    }

    @FXML
    private void refresh(ActionEvent event) throws SQLException, ClassNotFoundException {
        loadDataIntoTable();
        searchingHandler();
    }
}
