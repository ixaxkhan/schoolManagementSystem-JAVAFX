/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package inventory;

import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXDatePicker;
import com.jfoenix.controls.JFXTextField;
import databaseconnection.DatabaseConnection;
import java.net.URL;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Duration;
import org.controlsfx.control.Notifications;
import student.Registration.formController;

/**
 * FXML Controller class
 *
 * @author khan
 */
public class ProductSaleUpdateViewController implements Initializable {
    
    @FXML
    private JFXComboBox<String> comboClass;

    @FXML
    private JFXTextField txfUnitPrice;
    @FXML
    private JFXTextField txfPaidAmount;
    @FXML
    private Label labRemainAmount;
    @FXML
    private JFXDatePicker txfDate;
    @FXML
    private JFXComboBox<String> comboSection;
    @FXML
    private JFXComboBox<Integer> comboStudentRegNo;
    @FXML
    private JFXComboBox<String> comboProductName;
    @FXML
    private JFXTextField txfProductQuantity;
    @FXML
    private JFXTextField txfTotalPrice;
    @FXML
    private JFXComboBox<String> comboStatus;
    
    //************** for database connection***********
    Connection connection = null;
    PreparedStatement statement = null;
    ResultSet resultSet = null;
    String sql = null;
 

    // **************for class and section observable list*******************
    ObservableList<String> classesNames = FXCollections.observableArrayList();
    ObservableList<String> sectionName = FXCollections.observableArrayList();
    ObservableList<Integer> studentRegNo = FXCollections.observableArrayList();

    @Override
    public void initialize(URL url, ResourceBundle rb) {
       int classid=0,sectionid=0;
       String classNames=null,sectionNames=null;
       
        try {
            showClasses();
            loadProductNames();
            loadSectionsName();
            loadSectionsName();
            loadSectionsStudentRegNo();
        } catch (ClassNotFoundException | SQLException ex) {
            Logger.getLogger(ProductSaleUpdateViewController.class.getName()).log(Level.SEVERE, null, ex);
        }

        comboStatus.getItems().addAll("Paid", "Not Full Paid", "Not Paid");
        isNotFullPaid();
        calculateTotalPrice();
        calculateRemainAmount();
        try {
             connection = DatabaseConnection.getConnection();
            sql = "select * from student where reg_id=?";
            statement=connection.prepareStatement(sql);
            statement.setInt(1,ProductSaleViewController.date.getStuID());
            resultSet=  statement.executeQuery();
          if(resultSet.next() ){
          classid=resultSet.getInt("class_id");
          sectionid=resultSet.getInt("section_id");
          
          }
            
          sql="select * from sections where id=?";
          statement=connection.prepareStatement(sql);
          statement.setInt(1, sectionid);
          resultSet=statement.executeQuery();
                  
          if(resultSet.next()){
          sectionNames=resultSet.getString("name");
          System.out.println(sectionNames);
          }
            sql = "select * from class where id=?";
            statement = connection.prepareStatement(sql);
            statement.setInt(1, sectionid);
            resultSet=statement.executeQuery();
            if (resultSet.next()) {
                classNames = resultSet.getString("name");
                System.out.println(classNames);
            }
           // System.out.println(classNames+sectionNames);

        } catch (SQLException | ClassNotFoundException ex) {
            Logger.getLogger(ProductSaleUpdateViewController.class.getName()).log(Level.SEVERE, null, ex);
        }
      
        comboClass.setValue(classNames);
        txfUnitPrice.setText(""+ProductSaleViewController.date.getUnitPrice());
        txfPaidAmount.setText(""+ProductSaleViewController.date.getPaidAmount());
        labRemainAmount.setText(""+ProductSaleViewController.date.getDues());
        txfDate.setValue(ProductSaleViewController.date.getDate().toLocalDate());
        comboSection.setValue(sectionNames);
        comboStudentRegNo.setValue(ProductSaleViewController.date.getStuID());
        comboProductName.setValue(""+ProductSaleViewController.date.getProductName());
        txfProductQuantity.setText(""+ProductSaleViewController.date.getQuantity());;
        txfTotalPrice.setText(""+ProductSaleViewController.date.getTotalPrice());
        comboStatus.setValue(""+ProductSaleViewController.date.getStatus());
    
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
    @FXML
    private void update(ActionEvent event)throws ClassNotFoundException, SQLException {
       
        
        int product_id = 0;
        int currentQuantity = 0,oldQuantity=0, newQuantity = 0;
         Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmation Dialog");
        alert.setHeaderText(null);
        alert.setContentText("Are you soure to update sale record?");
        alert.initModality(Modality.APPLICATION_MODAL);
        Optional<ButtonType> action = alert.showAndWait();
        if (action.get() == ButtonType.OK) {
       
        
            connection = DatabaseConnection.getConnection();

            //for sale quantity before update
            sql = "select * from sale_product where id=?";
            statement = connection.prepareStatement(sql);
            statement.setInt(1, ProductSaleViewController.date.getSaleId());
            resultSet = statement.executeQuery();
            if (resultSet.next()) {

                oldQuantity = resultSet.getInt("quantity");
                product_id = resultSet.getInt("product_id");
            }
            //current quantity of product in stock
            sql = "select quantity from product_stock where product_id=?";
            statement = connection.prepareStatement(sql);
            statement.setInt(1, product_id);
            resultSet = statement.executeQuery();
            if (resultSet.next()) {

                currentQuantity = resultSet.getInt("quantity");

            }
            // update the stock of product 
            newQuantity = oldQuantity + currentQuantity;
            sql = "update product_stock set quantity=? where product_id=?";
            statement = connection.prepareStatement(sql);
            statement.setInt(1, newQuantity);
            statement.setInt(2, product_id);
            statement.execute();
            //get the product id which is selected in update dialog
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

                        sql = "update sale_product set date=?,dues=?,paid_amount=?,product_id=?,quantity=?,status=?,student_id=?,total_price=?,unit_price=? where id=?";
                        statement = connection.prepareStatement(sql);
                        statement.setDate(1, Date.valueOf(txfDate.getValue()));
                        statement.setDouble(2, Double.parseDouble(labRemainAmount.getText()));
                        statement.setDouble(3, Double.parseDouble(txfPaidAmount.getText()));
                        statement.setInt(4, product_id);
                        statement.setInt(5, Integer.parseInt(txfProductQuantity.getText()));
                        statement.setString(6, comboStatus.getValue());
                        statement.setInt(7, comboStudentRegNo.getValue());
                        statement.setDouble(8, Double.parseDouble(txfTotalPrice.getText()));
                        statement.setDouble(9, Double.parseDouble(txfUnitPrice.getText()));
                        statement.setInt(10, ProductSaleViewController.date.getSaleId());
                        statement.execute();
                        showNotifications("Sale Product Updation", "Sale recorde update sucessfully");
                    } else if (comboStatus.getValue().equals("Not Paid")) {
                        sql = "update sale_product set date=?,dues=?,paid_amount=?,product_id=?,quantity=?,status=?,student_id=?,total_price=?,unit_price=? where id=?";
                        statement = connection.prepareStatement(sql);
                        statement.setDate(1, Date.valueOf(txfDate.getValue()));
                        statement.setString(2, null);
                        statement.setString(3, null);
                        statement.setInt(4, product_id);
                        statement.setInt(5, Integer.parseInt(txfProductQuantity.getText()));
                        statement.setString(6, comboStatus.getValue());
                        statement.setInt(7, comboStudentRegNo.getValue());
                        statement.setDouble(8, Double.parseDouble(txfTotalPrice.getText()));
                        statement.setDouble(9, Double.parseDouble(txfUnitPrice.getText()));
                        statement.setInt(10, ProductSaleViewController.date.getSaleId());
                        statement.execute();
                        showNotifications("Sale Product Updation", "Sale recorde update sucessfully");
                    } else {
                        sql = "update sale_product set date=?,dues=?,paid_amount=?,product_id=?,quantity=?,status=?,student_id=?,total_price=?,unit_price=? where id=?";
                        statement = connection.prepareStatement(sql);
                        statement.setDate(1, Date.valueOf(txfDate.getValue()));
                        statement.setString(2, null);
                        statement.setString(3, null);
                        statement.setInt(4, product_id);
                        statement.setInt(5, Integer.parseInt(txfProductQuantity.getText()));
                        statement.setString(6, comboStatus.getValue());
                        statement.setInt(7, comboStudentRegNo.getValue());
                        statement.setDouble(8, Double.parseDouble(txfTotalPrice.getText()));
                        statement.setDouble(9, Double.parseDouble(txfUnitPrice.getText()));
                        statement.setInt(10, ProductSaleViewController.date.getSaleId());
                        statement.execute();
                        showNotifications("Sale Product Updation", "Sale recorde update sucessfully");
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

    @FXML
    private void closeWindow(ActionEvent event) {
        
        final Node source = (Node) event.getSource();
        Stage stage = (Stage) source.getScene().getWindow();
        stage.close();
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

        comboStudentRegNo.getItems().clear();
        comboStudentRegNo.setItems(studentRegNo);
    }

    private void loadProductNames() throws ClassNotFoundException, SQLException {

        connection = DatabaseConnection.getConnection();
        sql = "select * from add_product";
        statement = connection.prepareStatement(sql);
        resultSet = statement.executeQuery();
        comboProductName.getItems().clear();
        while (resultSet.next()) {

            comboProductName.getItems().add(resultSet.getString("name"));

        }

    }

    private void isNotFullPaid() {

        comboStatus.valueProperty().addListener((observable, oldValue, newValue) -> {

            if (newValue.equals("Not Full Paid")) {
              
                txfPaidAmount.setVisible(true);
                labRemainAmount.setVisible(true);

            } else {

                
                txfPaidAmount.setVisible(false);
                labRemainAmount.setVisible(false);
                labRemainAmount.setText("");
                txfPaidAmount.setText("");
            }
        });
    }

    private void calculateTotalPrice() {

        txfUnitPrice.textProperty().addListener((observable, oldValue, newValue) -> {

            txfTotalPrice.setText("" + Integer.parseInt(txfProductQuantity.getText()) * Integer.parseInt(newValue));

        });

    }

    private void calculateRemainAmount() {

        txfPaidAmount.textProperty().addListener((observable, oldValue, newValue) -> {
            double remainBalance = Double.parseDouble(txfTotalPrice.getText()) - Double.parseDouble(newValue);
            labRemainAmount.setText("" + remainBalance);
        });

    }
}
