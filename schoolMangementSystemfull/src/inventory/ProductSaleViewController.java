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
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
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
import student.Registration.formController;

/**
 * FXML Controller class
 *
 * @author khan
 */
public class ProductSaleViewController implements Initializable {
    @FXML
    private ComboBox<String> comboClass;
    @FXML
    private Label labPaidAmount;
    @FXML
    private TextField txfUnitPrice;
    @FXML
    private TextField txfPaidAmount;
    @FXML
    private Label labRemainAmount;
    @FXML
    private DatePicker txfDate;
    @FXML
    private ComboBox<String> comboSection;
    @FXML
    private ComboBox<Integer> comboStudentRegNo;
    @FXML
    private ComboBox<String> comboProductName;
    @FXML
    private TextField txfProductQuantity;
    @FXML
    private TextField txfTotalPrice;
    @FXML
    private ComboBox<String> comboStatus;
    @FXML
    private TableView<ProductSaleViewModal> table;
    @FXML
    private TableColumn<ProductSaleViewModal, Double> tbUnitPrice;
    @FXML
    private TableColumn<ProductSaleViewModal, Double> tbPaidAmount;
    @FXML
    private TableColumn<ProductSaleViewModal, Double> tbDues;
    @FXML
    private TableColumn<ProductSaleViewModal, Integer> tbSaleId;
    @FXML
    private TableColumn<ProductSaleViewModal, Integer> tbStuID;
    @FXML
    private TableColumn<ProductSaleViewModal, String> tbProductName;
    @FXML
    private TableColumn<ProductSaleViewModal, Integer> tbQuantity;
    @FXML
    private TableColumn<ProductSaleViewModal, Double> tbTotalPrice;
    @FXML
    private TableColumn<ProductSaleViewModal, String> tbStatus;
    @FXML
    private TableColumn<ProductSaleViewModal, Date> tbDate;
    @FXML
    private TableColumn<ProductSaleViewModal, Void> tbUpdate;
    @FXML
    private TableColumn<ProductSaleViewModal, Void> tbDelete;
    @FXML
    private TableColumn<ProductSaleViewModal, Void> tbView;
    //************** for database connection***********
    Connection connection = null;
    PreparedStatement statement = null;
    ResultSet resultSet = null;
    String sql = null;
    @FXML
    private TextField txfSearchBox;
   
    // **************for class and section observable list*******************
    ObservableList<String> classesNames = FXCollections.observableArrayList();
    ObservableList<String> sectionName = FXCollections.observableArrayList();
    ObservableList<Integer> studentRegNo = FXCollections.observableArrayList();

    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
        labPaidAmount.setVisible(false);
        txfPaidAmount.setVisible(false);
        labRemainAmount.setVisible(false);
        txfTotalPrice.setEditable(false);
        tbSaleId.setCellValueFactory(new PropertyValueFactory<>("SaleId"));
        tbStuID.setCellValueFactory(new PropertyValueFactory<>("StuID"));
        tbUnitPrice.setCellValueFactory(new PropertyValueFactory<>("UnitPrice"));
        tbDate.setCellValueFactory(new PropertyValueFactory<>("Date"));
        tbPaidAmount.setCellValueFactory(new PropertyValueFactory<>("PaidAmount"));
        tbDues.setCellValueFactory(new PropertyValueFactory<>("Dues"));
        tbProductName.setCellValueFactory(new PropertyValueFactory<>("ProductName"));
        tbQuantity.setCellValueFactory(new PropertyValueFactory<>("Quantity"));
        tbTotalPrice.setCellValueFactory(new PropertyValueFactory<>("PaidAmount"));
        tbStatus.setCellValueFactory(new PropertyValueFactory<>("Status"));
        try {
            loadDataIntoTable();
            searchingHandler();
            showClasses();
            loadProductNames(); loadSectionsName();
            loadSectionsName();
            loadSectionsStudentRegNo();
            loadProductNames();
            comboStatus.getItems().addAll("Paid","Not Full Paid","Not Paid");
           
        } catch (ClassNotFoundException | SQLException ex) {
            Logger.getLogger(ProductSaleViewController.class.getName()).log(Level.SEVERE, null, ex);
        }
        addUpdateToTable(tbUpdate,"Update");
        addDeleteToTable(tbDelete,"Delete");
        addViewToTable(tbView,"View");
        isNotFullPaid();
        calculateTotalPrice();
        calculateRemainAmount();
    }    

    @FXML
    private void saveSale(ActionEvent event) throws ClassNotFoundException, SQLException {
        int product_id=0;
        int currentQuantity=0,newQuantity=0;
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmation Dialog");
        alert.setHeaderText(null);
        alert.setContentText("Are you soure to add sale record?");
        alert.initModality(Modality.APPLICATION_MODAL);
        Optional<ButtonType> action = alert.showAndWait();
        if (action.get() == ButtonType.OK) {
            connection = DatabaseConnection.getConnection();
            sql = "select * from add_product where name=?";
            statement = connection.prepareStatement(sql);
            statement.setString(1, comboProductName.getValue());
            resultSet = statement.executeQuery();

            if (resultSet.next()) {

                product_id = resultSet.getInt("id");

            }
            sql = "select quantity from product_stock where product_id=?";
            statement = connection.prepareStatement(sql);
            statement.setInt(1, product_id);
            resultSet = statement.executeQuery();
            if (resultSet.next()) {

                if (resultSet.getInt("quantity") < Integer.parseInt(txfProductQuantity.getText())) {

                    Alert alert2 = new Alert(Alert.AlertType.WARNING);
                    alert2.setTitle("Warring Dialog");
                    alert2.setHeaderText(null);
                    alert2.setContentText("oot of stock current stock is=" + resultSet.getInt("quantity"));
                    alert2.initModality(Modality.APPLICATION_MODAL);
                    alert2.show();
        //out of stock

                } else {

                    sql = "select * from product_stock  where product_id=?";
                    statement = connection.prepareStatement(sql);
                    statement.setInt(1, product_id);
                    resultSet = statement.executeQuery();
                    if (resultSet.next()) {

                        currentQuantity = resultSet.getInt("quantity");
                    }
                    newQuantity = currentQuantity - Integer.parseInt(txfProductQuantity.getText());

                    sql = "update product_stock set quantity=? where product_id=?";
                    statement = connection.prepareStatement(sql);
                    statement.setInt(1, newQuantity);
                    statement.setInt(2, product_id);
                    statement.execute();
                    if (comboStatus.getValue().equals("Not Full Paid")) {

                        sql = "insert into sale_product values( ?,?,?,?,?,?,?,?,?,?)";
                        statement = connection.prepareStatement(sql);
                        statement.setString(1, null);
                        statement.setInt(2, Integer.parseInt(txfProductQuantity.getText()));
                        statement.setDouble(3, Double.parseDouble(txfUnitPrice.getText()));
                        statement.setDouble(4, Double.parseDouble(txfTotalPrice.getText()));
                        statement.setString(5, comboStatus.getValue());
                        statement.setDouble(6, Double.parseDouble(txfPaidAmount.getText()));
                        statement.setDouble(7, Double.parseDouble(labRemainAmount.getText()));
                        statement.setDate(8, Date.valueOf(txfDate.getValue()));
                        statement.setInt(9, comboStudentRegNo.getValue());
                        statement.setInt(10, product_id);
                        statement.execute();
                        loadDataIntoTable();
                        showNotifications("Product Saling", "Product sale sucessfully");
                        searchingHandler();
                    } else if (comboStatus.getValue().equals("Not Paid")) {

                        sql = "insert into sale_product values( ?,?,?,?,?,?,?,?,?,?)";
                        statement = connection.prepareStatement(sql);
                        statement.setString(1, null);
                        statement.setInt(2, Integer.parseInt(txfProductQuantity.getText()));
                        statement.setDouble(3, Double.parseDouble(txfUnitPrice.getText()));
                        statement.setDouble(4, Double.parseDouble(txfTotalPrice.getText()));
                        statement.setString(5, comboStatus.getValue());
                        statement.setString(6, null);
                        statement.setString(7, null);
                        statement.setDate(8, Date.valueOf(txfDate.getValue()));
                        statement.setInt(9, comboStudentRegNo.getValue());
                        statement.setInt(10, product_id);
                        statement.execute();
                        loadDataIntoTable();
                        showNotifications("Product Saling", "Product sale sucessfully");
                        searchingHandler();
                    } else {

                        sql = "insert into sale_product values( ?,?,?,?,?,?,?,?,?,?)";
                        statement = connection.prepareStatement(sql);
                        statement.setString(1, null);
                        statement.setInt(2, Integer.parseInt(txfProductQuantity.getText()));
                        statement.setDouble(3, Double.parseDouble(txfUnitPrice.getText()));
                        statement.setDouble(4, Double.parseDouble(txfTotalPrice.getText()));
                        statement.setString(5, comboStatus.getValue());
                        statement.setString(6, null);
                        statement.setString(7, null);
                        statement.setDate(8, Date.valueOf(txfDate.getValue()));
                        statement.setInt(9, comboStudentRegNo.getValue());
                        statement.setInt(10, product_id);
                        statement.execute();
                        loadDataIntoTable();
                        showNotifications("Product Saling", "Product sale sucessfully");
                        searchingHandler();
                    }

                }

            } else {

                Alert alert2 = new Alert(Alert.AlertType.WARNING);
                alert2.setTitle("Warring Dialog");
                alert2.setHeaderText(null);
                alert2.setContentText("Stock does not exist!");
                alert2.initModality(Modality.APPLICATION_MODAL);
                alert2.show();
        //stock does not present 

            }
        
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
        ObservableList<ProductSaleViewModal> product = FXCollections.observableArrayList();
        connection = DatabaseConnection.getConnection();
        sql = "SELECT * FROM  sale_product,add_product,student where sale_product.student_id=student.reg_id AND sale_product.product_id=add_product.id";
        statement = connection.prepareStatement(sql);
        resultSet = statement.executeQuery();
        while (resultSet.next()) {
//double UnitPrice, double PaidAmount, double Dues, int SaleId, int StuID, String ProductName, int Quantity, double TotalPrice, String Status
            product.add(new ProductSaleViewModal(resultSet.getDouble("sale_product.unit_price"), resultSet.getDouble("sale_product.paid_amount"), resultSet.getDouble("sale_product.dues"), resultSet.getInt("sale_product.id"), resultSet.getInt("sale_product.student_id"), resultSet.getString("add_product.name"),resultSet.getInt("sale_product.quantity"), resultSet.getDouble("sale_product.total_price") ,resultSet.getString("sale_product.status"),resultSet.getDate("sale_product.date") ));

        }

        table.setItems(product);

    }

    private void searchingHandler() {
        // 1. Wrap the ObservableList in a FilteredList (initially display all data).
        FilteredList<ProductSaleViewModal> filteredData = new FilteredList<>(table.getItems(), p -> true);

        // 2. Set the filter Predicate whenever the filter changes.
        txfSearchBox.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredData.setPredicate((Predicate<? super ProductSaleViewModal>) student -> {
                // If filter text is empty, display all persons.
                if (newValue == null || newValue.isEmpty()) {
                    return true;
                }

                // Compare first name and last name of every person with filter text.
                String lowerCaseFilter = newValue.toLowerCase();

                if (student.getProductName().toLowerCase().contains(lowerCaseFilter)) {
                    return true; // Filter matches ID.

                } else if (student.getProductName().toLowerCase().contains(lowerCaseFilter)) {
                    return true; // Filter matches name.
                }
                return false; // Does not match.
            });
        });

        // 3. Wrap the FilteredList in a SortedList. 
        SortedList<ProductSaleViewModal> sortedData = new SortedList<>(filteredData);

        // 4. Bind the SortedList comparator to the TableView comparator.
        sortedData.comparatorProperty().bind(table.comparatorProperty());

        // 5. Add sorted (and filtered) data to the table.
        table.setItems(sortedData);

    }
    static ProductSaleViewModal date;

    private void addUpdateToTable(TableColumn column, String name) {

        Callback<TableColumn<ProductSaleViewModal, Void>, TableCell<ProductSaleViewModal, Void>> cellFactory = new Callback<TableColumn<ProductSaleViewModal, Void>, TableCell<ProductSaleViewModal, Void>>() {
            @Override
            public TableCell<ProductSaleViewModal, Void> call(final TableColumn<ProductSaleViewModal, Void> param) {
                final TableCell<ProductSaleViewModal, Void> cell = new TableCell<ProductSaleViewModal, Void>() {

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
                                root = FXMLLoader.load(getClass().getResource("productSaleUpdateView.fxml"));
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

    private void addDeleteToTable(TableColumn column, String name) {

        Callback<TableColumn<ProductSaleViewModal, Void>, TableCell<ProductSaleViewModal, Void>> cellFactory = new Callback<TableColumn<ProductSaleViewModal, Void>, TableCell<ProductSaleViewModal, Void>>() {
            @Override
            public TableCell<ProductSaleViewModal, Void> call(final TableColumn<ProductSaleViewModal, Void> param) {
                final TableCell<ProductSaleViewModal, Void> cell = new TableCell<ProductSaleViewModal, Void>() {

                    JFXButton btn = new JFXButton(name);

                    {
                        btn.setStyle("-fx-background-color: #e02418;-fx-text-fill:white; ");
                        btn.setOnAction((ActionEvent event) -> {

                            date = getTableView().getItems().get(getIndex());

                            try {
                                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                                alert.setTitle("Confirmation Dialog");
                                alert.setHeaderText(null);
                                alert.setContentText("Are you sure to delete sale record ?");
                                alert.initModality(Modality.APPLICATION_MODAL);
                                Optional<ButtonType> action = alert.showAndWait();
                                if (action.get() == ButtonType.OK) {
                                    connection = DatabaseConnection.getConnection();
                                    sql = "delete from sale_product where id=? ";
                                    statement = connection.prepareStatement(sql);
                                    statement.setInt(1, date.getSaleId());
                                    statement.execute();
                                    showNotifications(" Sale Deletion ", "Sale record delete sucessfully");
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

        Callback<TableColumn<ProductSaleViewModal, Void>, TableCell<ProductSaleViewModal, Void>> cellFactory = new Callback<TableColumn<ProductSaleViewModal, Void>, TableCell<ProductSaleViewModal, Void>>() {
            @Override
            public TableCell<ProductSaleViewModal, Void> call(final TableColumn<ProductSaleViewModal, Void> param) {
                final TableCell<ProductSaleViewModal, Void> cell = new TableCell<ProductSaleViewModal, Void>() {

                    JFXButton btn = new JFXButton(name);

                    {
                        btn.setStyle("-fx-background-color: #43b8c2;-fx-text-fill:white; ");
                        btn.setOnAction((ActionEvent event) -> {

                            date = getTableView().getItems().get(getIndex());

                            try {
                                
                                Map parameters = new HashMap();
                                connection = DatabaseConnection.getConnection();
                                parameters.put("sale_product_id",  date.getSaleId());
                                //File file = new File("src/employee/IndividualEmployeeRecords.jrxml");
                                JasperDesign jasperDesign;
                                jasperDesign = JRXmlLoader.load("C:\\reports\\product_sale_Detail.jrxml");
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
            String classPrimryKey = null;
            try {
                connection = DatabaseConnection.getConnection();
                sql = "select id from class where name= ?";
                statement = connection.prepareStatement(sql);
                statement.setString(1, newValue);
                resultSet = statement.executeQuery();

                if (resultSet.next()) {

                    classPrimryKey = resultSet.getString("id");

                }
                sql = "select * from sections where class_id=?";
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
    private void loadSectionsStudentRegNo() {

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
                studentRegNo.clear();
                while (resultSet.next()) {

                    studentRegNo.add(resultSet.getInt("reg_id"));

                }

            } catch (ClassNotFoundException | SQLException ex) {
                Logger.getLogger(formController.class.getName()).log(Level.SEVERE, null, ex);
            }

        });

        comboStudentRegNo.getItems().clear();
        comboStudentRegNo.setItems(studentRegNo);
    }
    
    private void loadProductNames() throws ClassNotFoundException, SQLException{
    
        connection = DatabaseConnection.getConnection();
        sql = "select * from add_product";
        statement = connection.prepareStatement(sql);
        resultSet=statement.executeQuery();
        comboProductName.getItems().clear();
        while(resultSet.next()){
        
        comboProductName.getItems().add(resultSet.getString("name"));
        
        }
    
    }
    private void isNotFullPaid(){
    
    comboStatus.valueProperty().addListener((observable, oldValue, newValue) -> {
    
    if(newValue.equals("Not Full Paid")){
     labPaidAmount.setVisible(true);
     txfPaidAmount.setVisible(true);
     labRemainAmount.setVisible(true);
    
    }else{
    
        labPaidAmount.setVisible(false);
        txfPaidAmount.setVisible(false);
        labRemainAmount.setVisible(false);
        labRemainAmount.setText("");
        txfPaidAmount.setText("");
    }
    });
    }
    private void calculateTotalPrice(){
    
        txfUnitPrice.textProperty().addListener((observable, oldValue, newValue) -> {

            txfTotalPrice.setText("" + Integer.parseInt(txfProductQuantity.getText()) * Integer.parseInt(newValue));

        });
    
    }
    private void calculateRemainAmount(){
    
        txfPaidAmount.textProperty().addListener((observable, oldValue, newValue) -> {
         double remainBalance=Double.parseDouble(txfTotalPrice.getText())-Double.parseDouble(newValue);
          labRemainAmount.setText(""+remainBalance);
        });
    
    
    }

    @FXML
    private void refresh(ActionEvent event) throws ClassNotFoundException, SQLException {
        loadDataIntoTable();
        searchingHandler();
        loadProductNames();
    }
}
