/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package inventory;

import com.jfoenix.controls.JFXButton;
import databaseconnection.DatabaseConnection;
import static employee.EmployeeLeaveViewController.data;
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
import transport.viechleAsignToStudentViewModal;



/**
 * FXML Controller class
 *
 * @author khan
 */
public class ProductAddViewController implements Initializable {
    @FXML
    private DatePicker txfDate;
    @FXML
    private TextField txfMin;
    @FXML
    private TextArea txfDescription;
    @FXML
    private TextField txfProductName;
    @FXML
    private TextField txfMax;
    
    @FXML
    private TableView<ProductAddViewModal> table;
    @FXML
    private TableColumn<ProductAddViewModal, Integer> tbProductID;
    @FXML
    private TableColumn<ProductAddViewModal, String> tbProductName;
    
    @FXML
    private TableColumn<ProductAddViewModal, String> tbDescription;
    @FXML
    private TableColumn<ProductAddViewModal, Date> tbDate;
 
    @FXML
    private TableColumn<ProductAddViewModal, Integer> tbMin;
    @FXML
    private TableColumn<ProductAddViewModal, Integer> tbMax;
    @FXML
    private TableColumn<ProductAddViewModal, Void> tbUpate;
    @FXML
    private TableColumn<ProductAddViewModal, Void> tbDelete;
    @FXML
    private TableColumn<ProductAddViewModal, Void> tbView;
    
    //************** for database connection***********
    Connection connection = null;
    PreparedStatement statement = null;
    ResultSet resultSet = null;
    String sql = null;
    @FXML
    private TextField txfSearchBox;

   
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
       
        
        tbProductID.setCellValueFactory(new PropertyValueFactory<>("ProductID"));
        tbProductName.setCellValueFactory(new PropertyValueFactory<>("ProductName"));
        tbDescription.setCellValueFactory(new PropertyValueFactory<>("Description"));
        tbDate.setCellValueFactory(new PropertyValueFactory<>("Date"));
        tbMin.setCellValueFactory(new PropertyValueFactory<>("tbMin")); 
        tbMax.setCellValueFactory(new PropertyValueFactory<>("tbMax")); 
        try {
            loadDataIntoTable();
            searchingHandler();
        } catch (ClassNotFoundException | SQLException ex) {
            Logger.getLogger(ProductAddViewController.class.getName()).log(Level.SEVERE, null, ex);
        }
        addUpdateToTable(tbUpate,"Update");
        addDeleteToTable(tbDelete,"Delete");
        addViewToTable(tbView,"View");
    }    

    @FXML
    private void save(ActionEvent event) throws ClassNotFoundException, SQLException {
         Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmation Dialog");
        alert.setHeaderText(null);
        alert.setContentText("Are you soure to add product category?");
        alert.initModality(Modality.APPLICATION_MODAL);
        Optional<ButtonType> action = alert.showAndWait();
        if (action.get() == ButtonType.OK) {
            connection = DatabaseConnection.getConnection();
            sql = "insert into add_product(name,min,mix,date,description) values(?,?,?,?,?)";
            statement = connection.prepareStatement(sql);
            statement.setString(1, txfProductName.getText());
            statement.setInt(2, Integer.parseInt(txfMin.getText()));
            statement.setInt(3, Integer.parseInt(txfMax.getText()));
            statement.setDate(4, Date.valueOf(txfDate.getValue()));
            statement.setString(5, txfDescription.getText());
            statement.execute();
            showNotifications("Product Addition", "Product add sucessfully");
            loadDataIntoTable();
        }
        
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

    public void loadDataIntoTable() throws ClassNotFoundException, SQLException {
        ObservableList<ProductAddViewModal> product = FXCollections.observableArrayList();
        connection = DatabaseConnection.getConnection();
        sql = "SELECT * FROM add_product";
        statement = connection.prepareStatement(sql);
        resultSet = statement.executeQuery();

        while (resultSet.next()) {

            product.add(new ProductAddViewModal(resultSet.getInt("id"), resultSet.getString("name"), resultSet.getString("description"), resultSet.getDate("date"), resultSet.getInt("min"),resultSet.getInt("mix")));

        }

        table.setItems(product);

    }

    private void searchingHandler() {
        // 1. Wrap the ObservableList in a FilteredList (initially display all data).
        FilteredList<ProductAddViewModal> filteredData = new FilteredList<>(table.getItems(), p -> true);

        // 2. Set the filter Predicate whenever the filter changes.
        txfSearchBox.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredData.setPredicate((Predicate<? super ProductAddViewModal>) student -> {
                // If filter text is empty, display all persons.
                if (newValue == null || newValue.isEmpty()) {
                    return true;
                }

                // Compare first name and last name of every person with filter text.
                String lowerCaseFilter = newValue.toLowerCase();

                if (student.getProductName().toLowerCase().contains(lowerCaseFilter)) {
                    return true; // Filter matches ID.

                } else if (student.getDescription().toLowerCase().contains(lowerCaseFilter)) {
                    return true; // Filter matches name.
                }
                return false; // Does not match.
            });
        });

        // 3. Wrap the FilteredList in a SortedList. 
        SortedList<ProductAddViewModal> sortedData = new SortedList<>(filteredData);

        // 4. Bind the SortedList comparator to the TableView comparator.
        sortedData.comparatorProperty().bind(table.comparatorProperty());

        // 5. Add sorted (and filtered) data to the table.
        table.setItems(sortedData);

    }
    static ProductAddViewModal date;

    private void addUpdateToTable(TableColumn column, String name) {

        Callback<TableColumn<ProductAddViewModal, Void>, TableCell<ProductAddViewModal, Void>> cellFactory = new Callback<TableColumn<ProductAddViewModal, Void>, TableCell<ProductAddViewModal, Void>>() {
            @Override
            public TableCell<ProductAddViewModal, Void> call(final TableColumn<ProductAddViewModal, Void> param) {
                final TableCell<ProductAddViewModal, Void> cell = new TableCell<ProductAddViewModal, Void>() {

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
                                root = FXMLLoader.load(getClass().getResource("addProductViewUpdate.fxml"));
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

        Callback<TableColumn<ProductAddViewModal, Void>, TableCell<ProductAddViewModal, Void>> cellFactory = new Callback<TableColumn<ProductAddViewModal, Void>, TableCell<ProductAddViewModal, Void>>() {
            @Override
            public TableCell<ProductAddViewModal, Void> call(final TableColumn<ProductAddViewModal, Void> param) {
                final TableCell<ProductAddViewModal, Void> cell = new TableCell<ProductAddViewModal, Void>() {

                    JFXButton btn = new JFXButton(name);

                    {
                        btn.setStyle("-fx-background-color: #e02418;-fx-text-fill:white; ");
                        btn.setOnAction((ActionEvent event) -> {

                            date = getTableView().getItems().get(getIndex());

                            try {
                                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                                alert.setTitle("Confirmation Dialog");
                                alert.setHeaderText(null);
                                alert.setContentText("Are you sure to delete product ?");
                                alert.initModality(Modality.APPLICATION_MODAL);
                                Optional<ButtonType> action = alert.showAndWait();
                                if (action.get() == ButtonType.OK) {
                                    connection = DatabaseConnection.getConnection();
                                    sql = "delete from add_product where id=? ";
                                    statement = connection.prepareStatement(sql);
                                    statement.setInt(1, date.getProductID());
                                    statement.execute();
                                    showNotifications(" Product Deletion ", "Prodcut delete sucessfully");
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
    private void addViewToTable(TableColumn column, String name) {

        Callback<TableColumn<ProductAddViewModal, Void>, TableCell<ProductAddViewModal, Void>> cellFactory = new Callback<TableColumn<ProductAddViewModal, Void>, TableCell<ProductAddViewModal, Void>>() {
            @Override
            public TableCell<ProductAddViewModal, Void> call(final TableColumn<ProductAddViewModal, Void> param) {
                final TableCell<ProductAddViewModal, Void> cell = new TableCell<ProductAddViewModal, Void>() {

                    JFXButton btn = new JFXButton(name);

                    {
                        btn.setStyle("-fx-background-color: #43b8c2;-fx-text-fill:white; ");
                        btn.setOnAction((ActionEvent event) -> {

                            date = getTableView().getItems().get(getIndex());

                            try {
                                
                                Map parameters = new HashMap();
                                connection = DatabaseConnection.getConnection();
                                parameters.put("add_product_id", date.getProductID());
                                File file = new File("src/employee/IndividualEmployeeRecords.jrxml");
                                JasperDesign jasperDesign;
                                jasperDesign = JRXmlLoader.load("C:\\reports\\product_add_detail.jrxml");
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
        loadDataIntoTable();
        searchingHandler();
    }
}
