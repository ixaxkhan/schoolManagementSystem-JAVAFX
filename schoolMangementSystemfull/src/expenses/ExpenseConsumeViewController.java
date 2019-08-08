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
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
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



/**
 * FXML Controller class
 *
 * @author khan
 */
public class ExpenseConsumeViewController implements Initializable {
    @FXML
    private DatePicker txfDate;
    @FXML
    private ComboBox<String> comboExpenseCategory;
    @FXML
    private TextField txfExpenseAmount;
    @FXML
    private TableColumn<ExpenseConsumeViewModal, Integer> tbExpenseConsumeID;
    @FXML
    private TableColumn<ExpenseConsumeViewModal, String> tbCategoryName;
    @FXML
    private TableColumn<ExpenseConsumeViewModal, Double> tbExpenseAmount;
    @FXML
    private TableColumn<ExpenseConsumeViewModal, Date> tbDate;
    @FXML
    private TableColumn<ExpenseConsumeViewModal, Void> tbDelet;
    @FXML
    private TableView<ExpenseConsumeViewModal> table;
    @FXML
    private TableColumn<ExpenseConsumeViewModal, Void> tbUpdate;
    @FXML
    private TableColumn<ExpenseConsumeViewModal, Void> tbView;

    //************** for database connection***********
    Connection connection = null;
    PreparedStatement statement = null;
    ResultSet resultSet = null;
    String sql = null;
    @FXML
    private TextField txfSearchBox;
    @FXML
    private TableColumn<ExpenseConsumeViewModal, String> tbDetails;
    @FXML
    private TextArea Details;

    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
       tbExpenseConsumeID.setCellValueFactory(new PropertyValueFactory<>("ExpenseConsumeID"));
       tbCategoryName.setCellValueFactory(new PropertyValueFactory<>("CategoryName"));
        tbExpenseAmount.setCellValueFactory(new PropertyValueFactory<>("ExpenseAmount"));
        tbDate.setCellValueFactory(new PropertyValueFactory<>("date"));
        tbDetails.setCellValueFactory(new PropertyValueFactory<>("Details"));
        try {
            loadCategory();
            loadDataIntoTable();
            searchingHandler();
            addDeleteToTable(tbDelet,"Delete");
            addUpdateToTable(tbUpdate,"Update");
            addViewToTable(tbView,"View");
            refreshCategories();
            refreshCategories();
        } catch (ClassNotFoundException | SQLException ex) {
            Logger.getLogger(ExpenseConsumeViewController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }    

    @FXML
    private void saveExpense(ActionEvent event)throws SQLException, ClassNotFoundException {
        int expenseTypePrim=0;
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmation Dialog");
        alert.setHeaderText(null);
        alert.setContentText("Are you soure to add expense category?");
        alert.initModality(Modality.APPLICATION_MODAL);
        Optional<ButtonType> action = alert.showAndWait();
        if (action.get() == ButtonType.OK) {
            connection = DatabaseConnection.getConnection();
            sql = "select id from expense_type where category=?";
            statement = connection.prepareStatement(sql);
            statement.setString(1, comboExpenseCategory.getValue());
            resultSet = statement.executeQuery();
            if (resultSet.next()) {
                expenseTypePrim = resultSet.getInt("id");

            }

            sql = "insert into expense_consume(amount,date,expense_id,Details) values(?,?,?,?)";
            statement = connection.prepareStatement(sql);
            statement.setDouble(1, Double.parseDouble(txfExpenseAmount.getText()));
            statement.setDate(2, Date.valueOf(txfDate.getValue()));
            statement.setInt(3, expenseTypePrim);
            statement.setString(4,Details.getText());
            statement.execute();
            showNotifications("Expense Consumption","Expense consumption add sucessfully");
            loadDataIntoTable();
            searchingHandler();
        }
    }
    private ObservableList list = FXCollections.observableArrayList();

    private void loadDataIntoTable() throws SQLException, ClassNotFoundException {
        connection = DatabaseConnection.getConnection();
        sql = "SELECT * FROM expense_consume ,expense_type where expense_consume.expense_id=expense_type.id";
        statement = connection.prepareStatement(sql);
        resultSet = statement.executeQuery();
        list.clear();
       
        while (resultSet.next()) {

            list.addAll(new ExpenseConsumeViewModal(resultSet.getInt("expense_consume.id"), resultSet.getString("expense_type.category"), resultSet.getDouble("expense_consume.amount"),resultSet.getDate("expense_consume.date"),resultSet.getString("Details")));

        }
        table.setItems(list);
    }
    private void searchingHandler() {
        // 1. Wrap the ObservableList in a FilteredList (initially display all data).
        FilteredList<ExpenseConsumeViewModal> filteredData = new FilteredList<>(table.getItems(), p -> true);

        // 2. Set the filter Predicate whenever the filter changes.
        txfSearchBox.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredData.setPredicate((Predicate<? super ExpenseConsumeViewModal>) student -> {
                // If filter text is empty, display all persons.
                if (newValue == null || newValue.isEmpty()) {
                    return true;
                }

                // Compare first name and last name of every person with filter text.
                String lowerCaseFilter = newValue.toLowerCase();

                if (student.getCategoryName().toLowerCase().contains(lowerCaseFilter)) {
                    return true; // Filter matches ID.

                } else if (student.getCategoryName().toLowerCase().contains(lowerCaseFilter)) {
                    return true; // Filter matches name.
                }
                return false; // Does not match.
            });
        });

        // 3. Wrap the FilteredList in a SortedList. 
        SortedList<ExpenseConsumeViewModal> sortedData = new SortedList<>(filteredData);

        // 4. Bind the SortedList comparator to the TableView comparator.
        sortedData.comparatorProperty().bind(table.comparatorProperty());

        // 5. Add sorted (and filtered) data to the table.
        table.setItems(sortedData);

    }
    private ObservableList categoryList = FXCollections.observableArrayList();
    
    
    private void loadCategory() throws ClassNotFoundException, SQLException{
        connection = DatabaseConnection.getConnection();
        sql="select * from expense_type";
        statement = connection.prepareStatement(sql);
        resultSet = statement.executeQuery();
        categoryList.clear();
        while (resultSet.next()) {

            categoryList.addAll(resultSet.getString("category"));

        }
        
        comboExpenseCategory.setItems(categoryList);
    }
    static ExpenseConsumeViewModal date;

    private void addUpdateToTable(TableColumn column, String name) {

        Callback<TableColumn<ExpenseConsumeViewModal, Void>, TableCell<ExpenseConsumeViewModal, Void>> cellFactory = new Callback<TableColumn<ExpenseConsumeViewModal, Void>, TableCell<ExpenseConsumeViewModal, Void>>() {
            @Override
            public TableCell<ExpenseConsumeViewModal, Void> call(final TableColumn<ExpenseConsumeViewModal, Void> param) {
                final TableCell<ExpenseConsumeViewModal, Void> cell = new TableCell<ExpenseConsumeViewModal, Void>() {

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
                                root = FXMLLoader.load(getClass().getResource("consumeUpdateView.fxml"));
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
                                loadDataIntoTable();
                                searchingHandler();
                            } catch (IOException ex) {
                                Logger.getLogger(StudentController.class.getName()).log(Level.SEVERE, null, ex);
                            } catch (SQLException | ClassNotFoundException ex) {
                                Logger.getLogger(ExpenseConsumeViewController.class.getName()).log(Level.SEVERE, null, ex);
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

        Callback<TableColumn<ExpenseConsumeViewModal, Void>, TableCell<ExpenseConsumeViewModal, Void>> cellFactory = new Callback<TableColumn<ExpenseConsumeViewModal, Void>, TableCell<ExpenseConsumeViewModal, Void>>() {
            @Override
            public TableCell<ExpenseConsumeViewModal, Void> call(final TableColumn<ExpenseConsumeViewModal, Void> param) {
                final TableCell<ExpenseConsumeViewModal, Void> cell = new TableCell<ExpenseConsumeViewModal, Void>() {

                    JFXButton btn = new JFXButton(name);

                    {
                        btn.setStyle("-fx-background-color:#43b8c2;-fx-text-fill:white; ");
                        btn.setOnAction((ActionEvent event) -> {

                            date = getTableView().getItems().get(getIndex());

                            try {
                                connection = DatabaseConnection.getConnection();
                                Map parameters = new HashMap();

                                parameters.put("CONSUME_ID", date.getExpenseConsumeID());
                               

                                File file = new File("src/expenses/expenseConsumptionReport.jrxml");
                                JasperDesign jasperDesign = JRXmlLoader.load("C:\\reports\\expenseConsumptionReport.jrxml");
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
    private void addDeleteToTable(TableColumn column, String name) {

        Callback<TableColumn<ExpenseConsumeViewModal, Void>, TableCell<ExpenseConsumeViewModal, Void>> cellFactory = new Callback<TableColumn<ExpenseConsumeViewModal, Void>, TableCell<ExpenseConsumeViewModal, Void>>() {
            @Override
            public TableCell<ExpenseConsumeViewModal, Void> call(final TableColumn<ExpenseConsumeViewModal, Void> param) {
                final TableCell<ExpenseConsumeViewModal, Void> cell = new TableCell<ExpenseConsumeViewModal, Void>() {

                    JFXButton btn = new JFXButton(name);

                    {
                        btn.setStyle("-fx-background-color: #e02418;-fx-text-fill:white; ");
                        btn.setOnAction((ActionEvent event) -> {
                            
                            date = getTableView().getItems().get(getIndex());
                            
                            try {
                                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                                alert.setTitle("Confirmation Dialog");
                                alert.setHeaderText(null);
                                alert.setContentText("Are you soure to delete expense consume?");
                                alert.initModality(Modality.APPLICATION_MODAL);
                                Optional<ButtonType> action = alert.showAndWait();
                                if (action.get() == ButtonType.OK) {
                                    connection = DatabaseConnection.getConnection();
                                    sql = "delete  from expense_consume where id=? ";
                                    statement = connection.prepareStatement(sql);
                                    statement.setInt(1, date.getExpenseConsumeID());
                                    statement.execute();
                                    showNotifications("Expense Consume", "Expense consume delete sucessfull");
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
    private void refreshCategories(){
   txfDate.valueProperty().addListener((observable, oldValue, newValue) -> {
       try {
           connection = DatabaseConnection.getConnection();
           sql = "select * from expense_type";
           statement = connection.prepareStatement(sql);
           resultSet = statement.executeQuery();
           categoryList.clear();
           while (resultSet.next()) {

               categoryList.addAll(resultSet.getString("category"));

           }

           comboExpenseCategory.setItems(categoryList);
       } catch (ClassNotFoundException | SQLException ex) {
           Logger.getLogger(ExpenseConsumeViewController.class.getName()).log(Level.SEVERE, null, ex);
       }
  
   
   
   });
   
    
    }

    @FXML
    private void refresh(ActionEvent event) throws SQLException, ClassNotFoundException {
        loadDataIntoTable();
        searchingHandler();
        loadCategory();
    }
}
