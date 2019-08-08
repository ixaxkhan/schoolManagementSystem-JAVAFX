/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package expenses;

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
import javafx.scene.control.TextArea;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Duration;
import org.controlsfx.control.Notifications;

/**
 * FXML Controller class
 *
 * @author khan
 */
public class ConsumeUpdateViewController implements Initializable {
    @FXML
    private JFXDatePicker txfDate;
    @FXML
    private JFXTextField txfAmount;
    @FXML
    private JFXComboBox<String> comboExpenseCategory;
  //************** for database connection***********
    Connection connection = null;
    PreparedStatement statement = null;
    ResultSet resultSet = null;
    String sql = null;
   
    @FXML
    private TextArea txfDetails;
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        try {
            loadCategory();
            comboExpenseCategory.setValue(ExpenseConsumeViewController.date.getCategoryName());
            txfDate.setValue(ExpenseConsumeViewController.date.getDate().toLocalDate());
            txfAmount.setText(""+ExpenseConsumeViewController.date.getExpenseAmount());
            txfDetails.setText(ExpenseConsumeViewController.date.getDetails());
        } catch (ClassNotFoundException | SQLException ex) {
            Logger.getLogger(ConsumeUpdateViewController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }    

    @FXML
    private void update(ActionEvent event) throws SQLException, ClassNotFoundException {
        int expenseTypePrim = 0;
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
            sql = "update expense_consume set amount=?,date=?,expense_id=?,Details=? where id=?";
            statement = connection.prepareStatement(sql);
            statement.setDouble(1, Double.parseDouble(txfAmount.getText()));
            statement.setDate(2, Date.valueOf(txfDate.getValue()));
            statement.setInt(3, expenseTypePrim);
            statement.setString(4, txfDetails.getText());
            statement.setInt(5, ExpenseConsumeViewController.date.getExpenseConsumeID());
            statement.execute();
            showNotifications("Consumption Updation","Consumption update sucessfully");
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
    @FXML
    private void closeWindow(ActionEvent event) {
        
        final Node source = (Node) event.getSource();
        Stage stage = (Stage) source.getScene().getWindow();
        stage.close();
    }
    private ObservableList categoryList = FXCollections.observableArrayList();

    private void loadCategory() throws ClassNotFoundException, SQLException {
        connection = DatabaseConnection.getConnection();
        sql = "select * from expense_type";
        statement = connection.prepareStatement(sql);
        resultSet = statement.executeQuery();
        categoryList.clear();
        while (resultSet.next()) {

            categoryList.addAll(resultSet.getString("category"));

        }

        comboExpenseCategory.setItems(categoryList);
    }
}
