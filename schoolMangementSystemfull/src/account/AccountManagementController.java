/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package account;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
import databaseconnection.DatabaseConnection;
import static employee.jjjController.data;
import employee.jjjViewModal;
import inventory.ProductAddViewModal;
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
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.effect.BoxBlur;
import javafx.scene.input.MouseEvent;
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
import student.Registration.StudentModel;

/**
 * FXML Controller class
 *
 * @author khan
 */
public class AccountManagementController implements Initializable {

   
    //************** for database connection***********
    Connection connection = null;
    PreparedStatement statement = null;
    ResultSet resultSet = null;
    String sql = null;
    
   

    @Override
    public void initialize(URL url, ResourceBundle rb) {
     //*******************************************PORTION FOR FEE COLLECTION **************************************
     
        tbToutionSlip.setCellValueFactory(new PropertyValueFactory<>("ToutionSlip"));
        tbToutionRegNo.setCellValueFactory(new PropertyValueFactory<>("ToutionRegNo"));
        tbToutionName.setCellValueFactory(new PropertyValueFactory<>("ToutionName"));
        tbToutionFName.setCellValueFactory(new PropertyValueFactory<>("ToutionFName"));
        tbToutionDate.setCellValueFactory(new PropertyValueFactory<>("ToutionDate"));
        tbToutionFeeType.setCellValueFactory(new PropertyValueFactory<>("ToutionFeeType"));
        tbToutionStatus.setCellValueFactory(new PropertyValueFactory<>("ToutionStatus"));
        tbToutionTotalFee.setCellValueFactory(new PropertyValueFactory<>("ToutionTotalFee"));
        tbToutionPaidFee.setCellValueFactory(new PropertyValueFactory<>("ToutionPaidFee"));
        tbToutionDues.setCellValueFactory(new PropertyValueFactory<>("ToutionDues"));
        try {
            loadFeecollectionToTable();
        } catch (ClassNotFoundException | SQLException ex) {
            Logger.getLogger(AccountManagementController.class.getName()).log(Level.SEVERE, null, ex);
        }
        addUpdateToTable(tbToutionUpdate,"Update");
        addDeleteToTable(tbToutionDelete,"Delete");
        addViewToTable(tbToutionView,"View");
        searchingHandler();
     //*******************************************PORTION FOR EMPLOYEE SALARY **************************************
        tbEmpSlip.setCellValueFactory(new PropertyValueFactory<>("EmpSlip"));
        tbEmpRegNo.setCellValueFactory(new PropertyValueFactory<>("EmpRegNo"));
        tbEmpName.setCellValueFactory(new PropertyValueFactory<>("EmpName"));
        tbEmpFName.setCellValueFactory(new PropertyValueFactory<>("EmpFName"));
        tbEmpDate.setCellValueFactory(new PropertyValueFactory<>("EmpDate"));
        tbEmpFeeType.setCellValueFactory(new PropertyValueFactory<>("EmpFeeType"));
        tbEmpStatus.setCellValueFactory(new PropertyValueFactory<>("EmpStatus"));
        tbEmpTotalFee.setCellValueFactory(new PropertyValueFactory<>("EmpTotalFee"));
        tbEmpPaidFee.setCellValueFactory(new PropertyValueFactory<>("EmpPaidFee"));
        tbEmpDues.setCellValueFactory(new PropertyValueFactory<>("EmpDues"));
        addUpdateToEmpTable(tbEmpUpdate,"Update");
        addDeleteToEmpTable(tbEmpDelete,"Delete");
        addViewToEmpTable(tbEmpView,"View");
        
        try {
            loadDataIntoEmpTable();
            searchingHandlerForEmp();
        } catch (ClassNotFoundException | SQLException ex) {
            Logger.getLogger(AccountManagementController.class.getName()).log(Level.SEVERE, null, ex);
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
    
    //*******************************************PORTION FOR FEE COLLECTION **************************************
   
    @FXML
    private TableView<AccountFeeCollectionModal> toutionTable;
    @FXML
    private TableColumn<AccountFeeCollectionModal, Integer> tbToutionSlip;
    @FXML
    private TableColumn<AccountFeeCollectionModal, Integer> tbToutionRegNo;
    @FXML
    private TableColumn<AccountFeeCollectionModal, String> tbToutionName;
    @FXML
    private TableColumn<AccountFeeCollectionModal, String> tbToutionFName;
    @FXML
    private TableColumn<AccountFeeCollectionModal, Date> tbToutionDate;
    @FXML
    private TableColumn<AccountFeeCollectionModal, String> tbToutionFeeType;
    @FXML
    private TableColumn<AccountFeeCollectionModal, String> tbToutionStatus;
    @FXML
    private TableColumn<AccountFeeCollectionModal, Double> tbToutionTotalFee;
    @FXML
    private TableColumn<AccountFeeCollectionModal, Double> tbToutionPaidFee;
    @FXML
    private TableColumn<AccountFeeCollectionModal, Double> tbToutionDues;
    @FXML
    private TableColumn<AccountFeeCollectionModal, Void> tbToutionUpdate;
    @FXML
    private TableColumn<AccountFeeCollectionModal, Void> tbToutionDelete;
    @FXML
    private TableColumn<AccountFeeCollectionModal, Void> tbToutionView;
    @FXML
    private JFXTextField ToutionSearch;
    @FXML
    private void ToutionAdd(ActionEvent event) throws IOException, ClassNotFoundException, SQLException {
      Node node  = (Node)event.getSource();
        Stage primaryStage=(Stage)node.getScene().getWindow();
        Parent root = FXMLLoader.load(getClass().getResource("feeCollectionView.fxml"));
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
        loadFeecollectionToTable();
        toutionTable.refresh();
        searchingHandler();
    }

    @FXML
    private void ToutionPrint(MouseEvent event) {
    }
    
    private void loadFeecollectionToTable() throws ClassNotFoundException, SQLException{
   // int ToutionSlip, int ToutionRegNo, String ToutionName, String ToutionFName, Date ToutionDate, String ToutionFeeType, String ToutionStatus, double ToutionTotalFee, double ToutionPaidFee, double ToutionDues
        ObservableList<AccountFeeCollectionModal> studentFee = FXCollections.observableArrayList();
        connection = DatabaseConnection.getConnection();
        sql="select * from student,student_fee,fee_type where student.reg_id= student_fee.student_id and student_fee.fee_type_id= fee_type.id";
        statement = connection.prepareStatement(sql);
        resultSet=statement.executeQuery();
        studentFee.clear();
        while(resultSet.next()){
        
        studentFee.add(new AccountFeeCollectionModal(resultSet.getInt("student_fee.id"), resultSet.getInt("student.REG_ID"),resultSet.getString("student.name"),resultSet.getString("student.FATHER_NAME"),resultSet.getDate("STUDENT_FEE.date"),resultSet.getString("FEE_TYPE.FEE_CATEGORY"),resultSet.getString("student_fee.status"),resultSet.getDouble("student_fee.AMOUNT") ,resultSet.getDouble("student_fee.PAID_AMOUNT")  ,resultSet.getDouble("STUDENT_FEE.DUES")  ) );
        
        
        }
        toutionTable.setItems(studentFee);
     
    }
    @FXML
    private void toutionTableRefresh(ActionEvent event) throws ClassNotFoundException, SQLException{
        loadFeecollectionToTable();
        searchingHandler();
    }
    private void searchingHandler() {
        // 1. Wrap the ObservableList in a FilteredList (initially display all data).
        FilteredList<AccountFeeCollectionModal> filteredData = new FilteredList<>(toutionTable.getItems(), p -> true);

        // 2. Set the filter Predicate whenever the filter changes.
        ToutionSearch.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredData.setPredicate((Predicate<? super AccountFeeCollectionModal>) student -> {
                // If filter text is empty, display all persons.
                if (newValue == null || newValue.isEmpty()) {
                    return true;
                }

                // Compare first name and last name of every person with filter text.
                String lowerCaseFilter = newValue.toLowerCase();

                if (student.getToutionName().toLowerCase().contains(lowerCaseFilter)) {
                    return true; // Filter matches ID.

                } else if (student.getToutionFeeType().toLowerCase().contains(lowerCaseFilter)) {
                    return true; // Filter matches name.
                }
                return false; // Does not match.
            });
        });

        // 3. Wrap the FilteredList in a SortedList. 
        SortedList<AccountFeeCollectionModal> sortedData = new SortedList<>(filteredData);

        // 4. Bind the SortedList comparator to the TableView comparator.
        sortedData.comparatorProperty().bind(toutionTable.comparatorProperty());

        // 5. Add sorted (and filtered) data to the table.
        toutionTable.setItems(sortedData);

    }
    static AccountFeeCollectionModal date;

    private void addUpdateToTable(TableColumn column, String name) {

        Callback<TableColumn<AccountFeeCollectionModal, Void>, TableCell<AccountFeeCollectionModal, Void>> cellFactory = new Callback<TableColumn<AccountFeeCollectionModal, Void>, TableCell<AccountFeeCollectionModal, Void>>() {
            @Override
            public TableCell<AccountFeeCollectionModal, Void> call(final TableColumn<AccountFeeCollectionModal, Void> param) {
                final TableCell<AccountFeeCollectionModal, Void> cell = new TableCell<AccountFeeCollectionModal, Void>() {

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
                                root = FXMLLoader.load(getClass().getResource("monthlyWiseFeeCollectionUpdateView.fxml"));
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

        Callback<TableColumn<AccountFeeCollectionModal, Void>, TableCell<AccountFeeCollectionModal, Void>> cellFactory = new Callback<TableColumn<AccountFeeCollectionModal, Void>, TableCell<AccountFeeCollectionModal, Void>>() {
            @Override
            public TableCell<AccountFeeCollectionModal, Void> call(final TableColumn<AccountFeeCollectionModal, Void> param) {
                final TableCell<AccountFeeCollectionModal, Void> cell = new TableCell<AccountFeeCollectionModal, Void>() {

                    JFXButton btn = new JFXButton(name);

                    {
                        btn.setStyle("-fx-background-color: #e02418;-fx-text-fill:white; ");
                        btn.setOnAction((ActionEvent event) -> {

                            date = getTableView().getItems().get(getIndex());

                            try {
                                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                                alert.setTitle("Confirmation Dialog");
                                alert.setHeaderText(null);
                                alert.setContentText("Are you sure to delete student fee record ?");
                                alert.initModality(Modality.APPLICATION_MODAL);
                                Optional<ButtonType> action = alert.showAndWait();
                                if (action.get() == ButtonType.OK) {
                                    connection = DatabaseConnection.getConnection();
                                    sql = "delete from student_fee where id=? ";
                                    statement = connection.prepareStatement(sql);
                                    statement.setInt(1, date.getToutionSlip());
                                    statement.execute();
                                    showNotifications("Student Fee Record Deletion ", "Student fee record delete sucessfully");
                                    loadFeecollectionToTable();
                                    searchingHandler();

                                }
                            } catch (ClassNotFoundException | SQLException ex) {
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

        Callback<TableColumn<AccountFeeCollectionModal, Void>, TableCell<AccountFeeCollectionModal, Void>> cellFactory = new Callback<TableColumn<AccountFeeCollectionModal, Void>, TableCell<AccountFeeCollectionModal, Void>>() {
            @Override
            public TableCell<AccountFeeCollectionModal, Void> call(final TableColumn<AccountFeeCollectionModal, Void> param) {
                final TableCell<AccountFeeCollectionModal, Void> cell = new TableCell<AccountFeeCollectionModal, Void>() {

                    JFXButton btn = new JFXButton(name);

                    {
                        btn.setStyle("-fx-background-color:#43b8c2;-fx-text-fill:white; ");
                        btn.setOnAction((ActionEvent event) -> {
                           int class_id=0,section_id=0,fee_type_id=0,student_id=0;
                           String class_name=null,section_name=null,fee_type_name=null,student_name=null;
                            date = getTableView().getItems().get(getIndex());

                            try {
                                connection = DatabaseConnection.getConnection();
                                sql = "select * from student_fee where id=? ";
                                statement = connection.prepareStatement(sql); 
                                statement.setInt(1, date.getToutionSlip());
                                resultSet=statement.executeQuery();
                                if(resultSet.next()){
                                    student_id=resultSet.getInt("student_id");
                                    fee_type_id = resultSet.getInt("FEE_TYPE_ID");
                                    section_id = resultSet.getInt("section_id");
                                    class_id = resultSet.getInt("class_id");
                                
                                }
                                
                                sql = "select * from student where reg_id=? ";
                                statement = connection.prepareStatement(sql);
                                statement.setInt(1, student_id);
                                resultSet = statement.executeQuery();
                                if (resultSet.next()) {
                                
                                    student_name=resultSet.getString("name");
                                
                                }
                                sql = "select * from FEE_TYPE where id=? ";
                                statement = connection.prepareStatement(sql);
                                statement.setInt(1, fee_type_id);
                                resultSet = statement.executeQuery();
                                if (resultSet.next()) {

                                    fee_type_name = resultSet.getString("FEE_CATEGORY");

                                }
                                sql = "select * from class where id=? ";
                                statement = connection.prepareStatement(sql);
                                statement.setInt(1, class_id);
                                resultSet = statement.executeQuery();
                                if (resultSet.next()) {

                                    class_name = resultSet.getString("name");

                                }
                                sql = "select * from sections where id=? ";
                                statement = connection.prepareStatement(sql);
                                statement.setInt(1, section_id);
                                resultSet = statement.executeQuery();
                                if (resultSet.next()) {

                                    section_name = resultSet.getString("name");

                                }
                                
                                Map parameters = new HashMap();

                                parameters.put("Student Name", student_name);
                                parameters.put("fee_type",fee_type_name);
                                parameters.put("slip_no", date.getToutionSlip());
                                parameters.put("slip_id", date.getToutionSlip());
                                parameters.put("class_name", class_name);
                                parameters.put("section_ name", section_name);
                                File file = new File("src/account/feeSlip.jrxml");
                                JasperDesign jasperDesign = JRXmlLoader.load("C:\\reports\\feeSlip.jrxml");
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
//*******************************************PORTION FOR EMPLOYEE SALARY **************************************
    @FXML
    private TableView<AccountEmployeeSalaryModal> empTable;
    @FXML
    private TableColumn<AccountEmployeeSalaryModal, Integer> tbEmpSlip;
    @FXML
    private TableColumn<AccountEmployeeSalaryModal, Integer> tbEmpRegNo;
    @FXML
    private TableColumn<AccountEmployeeSalaryModal, String> tbEmpName;
    @FXML
    private TableColumn<AccountEmployeeSalaryModal, String> tbEmpFName;
    @FXML
    private TableColumn<AccountEmployeeSalaryModal, Date> tbEmpDate;
    @FXML
    private TableColumn<AccountEmployeeSalaryModal, String> tbEmpFeeType;
    @FXML
    private TableColumn<AccountEmployeeSalaryModal, String> tbEmpStatus;
    @FXML
    private TableColumn<AccountEmployeeSalaryModal, Double> tbEmpTotalFee;
    @FXML
    private TableColumn<AccountEmployeeSalaryModal, Double> tbEmpPaidFee;
    @FXML
    private TableColumn<AccountEmployeeSalaryModal, Double> tbEmpDues;
    @FXML
    private TableColumn<AccountEmployeeSalaryModal, Void> tbEmpUpdate;
    @FXML
    private TableColumn<AccountEmployeeSalaryModal, Void> tbEmpDelete;
    @FXML
    private TableColumn<AccountEmployeeSalaryModal, Void> tbEmpView;
    @FXML
    private JFXTextField txfEmpSearch;
    
    @FXML
    private void empAdd(ActionEvent event) throws IOException {
        
        Node node = (Node) event.getSource();
        Stage primaryStage = (Stage) node.getScene().getWindow();
        Parent root = FXMLLoader.load(getClass().getResource("SalayCollectionView.fxml"));
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
      
    }

    @FXML
    private void empPrint(MouseEvent event) {
    }
    @FXML
    private void empTableRefresh(ActionEvent event) throws ClassNotFoundException, SQLException {
        loadDataIntoEmpTable();
        searchingHandlerForEmp();
    }
   
    private void loadDataIntoEmpTable() throws ClassNotFoundException, SQLException{
        ObservableList<AccountEmployeeSalaryModal> empSalarys = FXCollections.observableArrayList();
       connection = DatabaseConnection.getConnection();
       sql=" SELECT * FROM EMPLOYEE_SALARY ,employee where employee_salary.employee_id = employee.reg_no";
       statement=connection.prepareStatement(sql);
       resultSet=statement.executeQuery();
       while(resultSet.next()){
      // int EmpSlip, int EmpRegNo, String EmpName, String EmpFName, Date EmpDate, String EmpFeeType, String EmpStatus, double EmpTotalFee, double EmpPaidFee) {
        
       empSalarys.add(new AccountEmployeeSalaryModal( resultSet.getInt("EMPLOYEE_SALARY.id"),resultSet.getInt("EMPLOYEE.reg_no"),resultSet.getString("EMPLOYEE.name") , resultSet.getString("EMPLOYEE.FATHER_NAME"),resultSet.getDate("EMPLOYEE_SALARY.date") ,resultSet.getString("EMPLOYEE_SALARY.FEE_TYPE"),resultSet.getString("EMPLOYEE_SALARY.status"),resultSet.getDouble("EMPLOYEE_SALARY.amount"),resultSet.getDouble("EMPLOYEE_SALARY.paid_amount") ,resultSet.getDouble("EMPLOYEE_SALARY.REMAIN_AMOUNT")  ));
       
       }
    empTable.setItems(empSalarys);
    }
    
    private void searchingHandlerForEmp() {
        // 1. Wrap the ObservableList in a FilteredList (initially display all data).
        FilteredList<AccountEmployeeSalaryModal> filteredData = new FilteredList<>(empTable.getItems(), p -> true);

        // 2. Set the filter Predicate whenever the filter changes.
        txfEmpSearch.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredData.setPredicate((Predicate<? super AccountEmployeeSalaryModal>) student -> {
                // If filter text is empty, display all persons.
                if (newValue == null || newValue.isEmpty()) {
                    return true;
                }

                // Compare first name and last name of every person with filter text.
                String lowerCaseFilter = newValue.toLowerCase();

                if (student.getEmpFeeType().toLowerCase().contains(lowerCaseFilter)) {
                    return true; // Filter matches ID.

                } else if (student.getEmpName().toLowerCase().contains(lowerCaseFilter)) {
                    return true; // Filter matches name.
                }
                return false; // Does not match.
            });
        });

        // 3. Wrap the FilteredList in a SortedList. 
        SortedList<AccountEmployeeSalaryModal> sortedData = new SortedList<>(filteredData);

        // 4. Bind the SortedList comparator to the TableView comparator.
        sortedData.comparatorProperty().bind(empTable.comparatorProperty());

        // 5. Add sorted (and filtered) data to the table.
        empTable.setItems(sortedData);

    }
    static AccountEmployeeSalaryModal empObject;

    private void addUpdateToEmpTable(TableColumn column, String name) {

        Callback<TableColumn<AccountEmployeeSalaryModal, Void>, TableCell<AccountEmployeeSalaryModal, Void>> cellFactory = new Callback<TableColumn<AccountEmployeeSalaryModal, Void>, TableCell<AccountEmployeeSalaryModal, Void>>() {
            @Override
            public TableCell<AccountEmployeeSalaryModal, Void> call(final TableColumn<AccountEmployeeSalaryModal, Void> param) {
                final TableCell<AccountEmployeeSalaryModal, Void> cell = new TableCell<AccountEmployeeSalaryModal, Void>() {

                    JFXButton btn = new JFXButton(name);

                    {
                        btn.setStyle("-fx-background-color: #1d62d1;-fx-text-fill:white; ");
                        btn.setOnAction((ActionEvent event) -> {
                            final Node source = (Node) event.getSource();
                            final Stage primaryStage = (Stage) source.getScene().getWindow();
                            empObject = getTableView().getItems().get(getIndex());
                            Parent root;
                            try {
                                //  Stage stage=new Stage();
                                root = FXMLLoader.load(getClass().getResource("giveSalaryUpdateView.fxml"));
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

    private void addDeleteToEmpTable(TableColumn column, String name) {

        Callback<TableColumn<AccountEmployeeSalaryModal, Void>, TableCell<AccountEmployeeSalaryModal, Void>> cellFactory = new Callback<TableColumn<AccountEmployeeSalaryModal, Void>, TableCell<AccountEmployeeSalaryModal, Void>>() {
            @Override
            public TableCell<AccountEmployeeSalaryModal, Void> call(final TableColumn<AccountEmployeeSalaryModal, Void> param) {
                final TableCell<AccountEmployeeSalaryModal, Void> cell = new TableCell<AccountEmployeeSalaryModal, Void>() {

                    JFXButton btn = new JFXButton(name);

                    {
                        btn.setStyle("-fx-background-color: #e02418;-fx-text-fill:white; ");
                        btn.setOnAction((ActionEvent event) -> {

                            empObject = getTableView().getItems().get(getIndex());

                            try {
                                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                                alert.setTitle("Confirmation Dialog");
                                alert.setHeaderText(null);
                                alert.setContentText("Are you sure to delete employee amount record ?");
                                alert.initModality(Modality.APPLICATION_MODAL);
                                Optional<ButtonType> action = alert.showAndWait();
                                if (action.get() == ButtonType.OK) {
                                    connection = DatabaseConnection.getConnection();
                                    sql = "delete from EMPLOYEE_SALARY where id=? ";
                                    statement = connection.prepareStatement(sql);
                                    statement.setInt(1, empObject.getEmpSlip());
                                    statement.execute();
                                    showNotifications(" Employee Amount Record ", "Employee amount record delete sucessfully");
                                    loadDataIntoEmpTable();
                                    searchingHandlerForEmp();

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

    private void addViewToEmpTable(TableColumn column, String name) {

        Callback<TableColumn<AccountEmployeeSalaryModal, Void>, TableCell<AccountEmployeeSalaryModal, Void>> cellFactory = new Callback<TableColumn<AccountEmployeeSalaryModal, Void>, TableCell<AccountEmployeeSalaryModal, Void>>() {
            @Override
            public TableCell<AccountEmployeeSalaryModal, Void> call(final TableColumn<AccountEmployeeSalaryModal, Void> param) {
                final TableCell<AccountEmployeeSalaryModal, Void> cell = new TableCell<AccountEmployeeSalaryModal, Void>() {

                    JFXButton btn = new JFXButton(name);

                    {
                        btn.setStyle("-fx-background-color: #43b8c2;-fx-text-fill:white; ");
                        btn.setOnAction((ActionEvent event) -> {

                            empObject = getTableView().getItems().get(getIndex());

                            try {
                                String Employee_name = null;
                                connection = DatabaseConnection.getConnection();
                                sql = "select * from employee where reg_no=?";
                                statement = connection.prepareStatement(sql);
                                statement.setInt(1,empObject.getEmpRegNo() );
                                resultSet = statement.executeQuery();

                                if (resultSet.next()) {
                                    Employee_name = resultSet.getString("name");

                                }
                                Map parameters = new HashMap();

                                parameters.put("salary_slip_id", empObject.getEmpSlip());
                                parameters.put("employee_name", Employee_name);

                                File file = new File("src/account/salarySlip2.jrxml");
                                JasperDesign jasperDesign = JRXmlLoader.load("C:\\reports\\salarySlip2.jrxml");
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
}
