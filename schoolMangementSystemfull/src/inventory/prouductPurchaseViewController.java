/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package inventory;

import com.jfoenix.controls.JFXButton;
import databaseconnection.DatabaseConnection;
import static inventory.ProductAddViewController.date;
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
import javafx.scene.control.Label;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.TreeTableColumn;
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
public class prouductPurchaseViewController implements Initializable {
    @FXML
    private DatePicker txfDate;
    @FXML
    private ComboBox<Integer> comboEmployeeID;
    @FXML
    private TextField txfFrom;
    @FXML
    private TextField txfBillNo;
    @FXML
    private ComboBox<String> comboProductName;
    @FXML
    private TextField txfProductQuantity;
    @FXML
    private TextField txfUnitPrice;
    @FXML
    private TextField txfTotalPrice;

    @FXML
    private TableView<ProductPurchaseViewModal> table;
    @FXML
    private TableColumn<ProductPurchaseViewModal, Integer> tbPurCaseID;

    @FXML
    private TableColumn<ProductPurchaseViewModal, String> tbFrom;
    @FXML
    private TableColumn<ProductPurchaseViewModal, String> tbBillNo;
    @FXML
    private TableColumn<ProductPurchaseViewModal, String> tbProductName;
    @FXML
    private TableColumn<ProductPurchaseViewModal, Integer> tbProductQuantity;
    @FXML
    private TableColumn<ProductPurchaseViewModal, Double> tbUnitPrice;
    @FXML
    private TableColumn<ProductPurchaseViewModal, Double> tbTotalPrice;
    @FXML
    private TableColumn<ProductPurchaseViewModal, Date> tbDate;
    @FXML
    private TableColumn<ProductPurchaseViewModal, Void> tbUpdate;
    @FXML
    private TableColumn<ProductPurchaseViewModal, Void> tbDelete;
    @FXML
    private TableColumn<ProductPurchaseViewModal, Void> tbView;
    @FXML
    private TableColumn<ProductPurchaseViewModal, Integer> tbEmployeeID;

    //************** for database connection***********
    Connection connection = null;
    PreparedStatement statement = null;
    ResultSet resultSet = null;
    String sql = null;
    @FXML
    private TextField txfSearchBox;
    @FXML
    private JFXButton savebtn;
    @FXML
    private Label errorMassage;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
        tbPurCaseID.setCellValueFactory(new PropertyValueFactory<>("PurCaseID"));
        tbProductName.setCellValueFactory(new PropertyValueFactory<>("ProductName"));
        tbFrom.setCellValueFactory(new PropertyValueFactory<>("From"));
        tbDate.setCellValueFactory(new PropertyValueFactory<>("date"));
        tbBillNo.setCellValueFactory(new PropertyValueFactory<>("BillNo"));
        tbProductQuantity.setCellValueFactory(new PropertyValueFactory<>("ProductQuantity"));
        tbUnitPrice.setCellValueFactory(new PropertyValueFactory<>("UnitPrice"));
        tbTotalPrice.setCellValueFactory(new PropertyValueFactory<>("TotalPrice"));
        tbEmployeeID.setCellValueFactory(new PropertyValueFactory<>("EmployeeID"));
        try {
            loadDataIntoTable();
            loadEmployeeRegNo() ;
            loadProductName();
            
            
        } catch (ClassNotFoundException | SQLException ex) {
            Logger.getLogger(prouductPurchaseViewController.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        addUpdateToTable(tbUpdate,"Update");
        addDeleteToTable(tbDelete,"Delete");
        addViewToTable(tbView,"View");
        searchingHandler();
        isValideQuantity();
        calculateTotalPrice();
    }    
    private void searchingHandler() {
        // 1. Wrap the ObservableList in a FilteredList (initially display all data).
        FilteredList<ProductPurchaseViewModal> filteredData = new FilteredList<>(table.getItems(), p -> true);

        // 2. Set the filter Predicate whenever the filter changes.
        txfSearchBox.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredData.setPredicate((Predicate<? super ProductPurchaseViewModal>) student -> {
                // If filter text is empty, display all persons.
                if (newValue == null || newValue.isEmpty()) {
                    return true;
                }

                // Compare first name and last name of every person with filter text.
                String lowerCaseFilter = newValue.toLowerCase();

                if (student.getProductName().toLowerCase().contains(lowerCaseFilter)) {
                    return true; // Filter matches ID.

                } else if (student.getBillNo().toLowerCase().contains(lowerCaseFilter)) {
                    return true; // Filter matches name.
                }
                return false; // Does not match.
            });
        });

        // 3. Wrap the FilteredList in a SortedList. 
        SortedList<ProductPurchaseViewModal> sortedData = new SortedList<>(filteredData);

        // 4. Bind the SortedList comparator to the TableView comparator.
        sortedData.comparatorProperty().bind(table.comparatorProperty());

        // 5. Add sorted (and filtered) data to the table.
        table.setItems(sortedData);

    }
    @FXML
    private void savePurchase(ActionEvent event) throws ClassNotFoundException, SQLException {

       int productPrim=0;
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmation Dialog");
        alert.setHeaderText(null);
        alert.setContentText("Are you soure to purchase product?");
        alert.initModality(Modality.APPLICATION_MODAL);
        Optional<ButtonType> action = alert.showAndWait();
        if (action.get() == ButtonType.OK) {
            connection = DatabaseConnection.getConnection();
            sql = "select * from add_product where name=?";
            statement = connection.prepareStatement(sql);
            statement.setString(1, comboProductName.getValue());
            resultSet = statement.executeQuery();
            if (resultSet.next()) {

                productPrim = resultSet.getInt("id");
            }
            resultSet=null;
            sql = "select * from product_stock where product_id=?";
            statement = connection.prepareStatement(sql);
            statement.setInt(1, productPrim);
            resultSet = statement.executeQuery();
            if(resultSet.next()){
                int oldQuantity=0,newQuantity=0;
                oldQuantity=resultSet.getInt("quantity");
                newQuantity=oldQuantity+Integer.parseInt(txfProductQuantity.getText());
                sql = "update product_stock set quantity=? where product_id=? ";
                statement = connection.prepareStatement(sql);
                statement.setInt(1, newQuantity);
                statement.setInt(2, productPrim);
                statement.execute();
            }else{
            
                sql="insert into product_stock values(?,?,?)" ;
               statement = connection.prepareStatement(sql);
               statement.setString(1, null);
               statement.setInt(2,Integer.parseInt(txfProductQuantity.getText()));
               statement.setInt(3,productPrim);
               statement.execute();
            
            
            }
//            
//            
//              sql="insert into product_stock values(?,?,?)" ;
//               statement = connection.prepareStatement(sql);
//               statement.setString(1, null);
//               statement.setInt(2,Integer.parseInt(txfProductQuantity.getText()));
//               statement.setInt(3,productPrim);
//               statement.execute();
//            
//            
//            }
            sql = "insert into prouduct_purchase(quantity,unit_price,total_price,fromt,bill_no,date,employee_id,product_id)values(?,?,?,?,?,?,?,?)";
            statement = connection.prepareStatement(sql);
            statement.setInt(1, Integer.parseInt(txfProductQuantity.getText()));
            statement.setDouble(2, Double.parseDouble(txfUnitPrice.getText()));
            statement.setDouble(3, Double.parseDouble(txfTotalPrice.getText()));
            statement.setString(4, txfFrom.getText());
            statement.setString(5, txfBillNo.getText());
            statement.setDate(6, Date.valueOf(txfDate.getValue()));
            statement.setInt(7, comboEmployeeID.getValue());
            statement.setInt(8, productPrim);
            statement.execute();
            showNotifications("Product Purchase", "Product purchase sucessfully");
            loadDataIntoTable();
            searchingHandler();
        }
    }
    public void loadDataIntoTable() throws ClassNotFoundException, SQLException {
        ObservableList<ProductPurchaseViewModal> product = FXCollections.observableArrayList();
        connection = DatabaseConnection.getConnection();
        sql = "SELECT * FROM add_product,prouduct_purchase,employee WHERE prouduct_purchase.employee_id=employee.reg_no AND prouduct_purchase.product_id=add_product.id";
        statement = connection.prepareStatement(sql);
        resultSet = statement.executeQuery();
      //  int PurCaseID, int EmployeeID, String From, String BillNo, String ProductName, int ProductQuantity, double UnitPrice, double TotalPrice
        while (resultSet.next()) {

            product.add(new ProductPurchaseViewModal(resultSet.getInt("prouduct_purchase.id"), resultSet.getInt("employee.reg_no"), resultSet.getString("prouduct_purchase.fromt"), resultSet.getString("prouduct_purchase.bill_no"), resultSet.getString("add_product.name"), resultSet.getInt("prouduct_purchase.quantity"),resultSet.getDouble("prouduct_purchase.unit_price"), resultSet.getDouble("prouduct_purchase.total_price"),resultSet.getDate("prouduct_purchase.date")));

        }

        table.setItems(product);

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
   
    static ProductPurchaseViewModal date;

    private void addUpdateToTable(TableColumn column, String name) {

        Callback<TableColumn<ProductPurchaseViewModal, Void>, TableCell<ProductPurchaseViewModal, Void>> cellFactory = new Callback<TableColumn<ProductPurchaseViewModal, Void>, TableCell<ProductPurchaseViewModal, Void>>() {
            @Override
            public TableCell<ProductPurchaseViewModal, Void> call(final TableColumn<ProductPurchaseViewModal, Void> param) {
                final TableCell<ProductPurchaseViewModal, Void> cell = new TableCell<ProductPurchaseViewModal, Void>() {

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

        Callback<TableColumn<ProductPurchaseViewModal, Void>, TableCell<ProductPurchaseViewModal, Void>> cellFactory = new Callback<TableColumn<ProductPurchaseViewModal, Void>, TableCell<ProductPurchaseViewModal, Void>>() {
            @Override
            public TableCell<ProductPurchaseViewModal, Void> call(final TableColumn<ProductPurchaseViewModal, Void> param) {
                final TableCell<ProductPurchaseViewModal, Void> cell = new TableCell<ProductPurchaseViewModal, Void>() {

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
                                    sql = "delete from prouduct_purchase where id=? ";
                                    statement = connection.prepareStatement(sql);
                                    statement.setInt(1, date.getPurCaseID());
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

        Callback<TableColumn<ProductPurchaseViewModal, Void>, TableCell<ProductPurchaseViewModal, Void>> cellFactory = new Callback<TableColumn<ProductPurchaseViewModal, Void>, TableCell<ProductPurchaseViewModal, Void>>() {
            @Override
            public TableCell<ProductPurchaseViewModal, Void> call(final TableColumn<ProductPurchaseViewModal, Void> param) {
                final TableCell<ProductPurchaseViewModal, Void> cell = new TableCell<ProductPurchaseViewModal, Void>() {

                    JFXButton btn = new JFXButton(name);

                    {
                        btn.setStyle("-fx-background-color: #43b8c2;-fx-text-fill:white; ");
                        btn.setOnAction((ActionEvent event) -> {

                            date = getTableView().getItems().get(getIndex());

                            try {
                                
                                
                                Map parameters = new HashMap();
                                connection = DatabaseConnection.getConnection();
                                parameters.put("purchase_product_id", date.getPurCaseID());
                                File file = new File("src/employee/IndividualEmployeeRecords.jrxml");
                                JasperDesign jasperDesign;
                                jasperDesign = JRXmlLoader.load("C:\\reports\\product_purchase_Detail.jrxml");
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
    private void loadEmployeeRegNo() throws ClassNotFoundException, SQLException{
        connection = DatabaseConnection.getConnection();
        sql = "select * from employee";
        statement = connection.prepareStatement(sql);
        resultSet = statement.executeQuery();
        comboEmployeeID.getItems().clear();
        if (resultSet.next()) {

           comboEmployeeID.getItems().add(resultSet.getInt("reg_no")) ;
        }
    
    }
    private void loadProductName() throws ClassNotFoundException, SQLException{
        connection = DatabaseConnection.getConnection();
        sql = "select * from add_product";
        statement = connection.prepareStatement(sql);
        resultSet = statement.executeQuery();
        comboProductName.getItems().clear();
        while (resultSet.next()) {

            comboProductName.getItems().add(resultSet.getString("name"));
        }
    
    }
    
    private void isValideQuantity(){
    
        txfProductQuantity.textProperty().addListener((observable, oldValue, newValue) -> {
        int min=0, max=0;
            try {
                connection = DatabaseConnection.getConnection();
                sql = "select * from add_product where name=?";
                statement = connection.prepareStatement(sql);
                statement.setString(1, comboProductName.getValue());
                resultSet = statement.executeQuery();
                if(resultSet.next()){
                
                min=resultSet.getInt("min");
                max=resultSet.getInt("mix");
                }
                
                if(min<=Integer.parseInt(newValue) &&   max>=Integer.parseInt(newValue) ){
                    
                   savebtn.setVisible(true);
                    errorMassage.setText("");
                }else{
                savebtn.setVisible(false);
                errorMassage.setText("Product quantity must be in specified rang");
                
                }
                

            } catch (ClassNotFoundException | SQLException ex) {
                Logger.getLogger(prouductPurchaseViewController.class.getName()).log(Level.SEVERE, null, ex);
            }
         
        
        });
    
    }
    private void calculateTotalPrice(){
    
        txfTotalPrice.setEditable(false);
        txfUnitPrice.textProperty().addListener((observable, oldValue, newValue) -> {
        
        txfTotalPrice.setText(""+Integer.parseInt(txfProductQuantity.getText())*Integer.parseInt(newValue));
        
        });
    }

    @FXML
    private void refresh(ActionEvent event) throws ClassNotFoundException, SQLException {
        loadDataIntoTable();
        loadEmployeeRegNo();
        loadProductName();
        searchingHandler();
    }
}
