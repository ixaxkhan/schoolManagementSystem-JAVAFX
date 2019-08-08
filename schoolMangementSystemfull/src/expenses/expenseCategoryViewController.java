
package expenses;

import databaseconnection.DatabaseConnection;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Modality;
import javafx.util.Duration;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.view.JasperViewer;
import org.controlsfx.control.Notifications;

public class expenseCategoryViewController implements Initializable {
    //************** for database connection***********
    Connection connection = null;
    PreparedStatement statement = null;
    ResultSet resultSet = null;
    String sql = null;
    @FXML
    private TextField expenseType;
    @FXML
    private CheckBox checkExpenseCategory;
    @FXML
    private TextField txfCategoryID;
    @FXML
    private Label expenseID;
   

    
    
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        txfCategoryID.setVisible(false);
        expenseID.setVisible(false);
        checkExpenseCategory.selectedProperty().addListener(new ChangeListener<Boolean>() {
            public void changed(ObservableValue<? extends Boolean> ov,
                    Boolean old_val, Boolean new_val) {
                if (new_val) {
                    expenseID.setVisible(true);
                    txfCategoryID.setVisible(true);

                } else {

                    txfCategoryID.setVisible(false);
                    expenseID.setVisible(false);
                }
            }
        });
        //******************************** LOADING DATA INTO FIELD WHEN DATA ENTRY INTO ID FIELDS*************************
         txfCategoryID.textProperty().addListener((observable, oldValue, newValue) -> {
        
            try {
                connection = DatabaseConnection.getConnection();
                sql = "SELECT * FROM EXPENSE_CATEGORY WHERE ID =?";
                statement = connection.prepareStatement(sql);
                statement.setString(1, newValue);
                resultSet = statement.executeQuery();
                if (resultSet.next()) {
                    expenseType.setText(resultSet.getString("EXPENSE_TYPE"));
                }else{
                
                    expenseType.setText("");
                  
                }
            } catch (ClassNotFoundException | SQLException ex) {
                Logger.getLogger(expenseCategoryViewController.class.getName()).log(Level.SEVERE, null, ex);
            }
           
        });
        
       
        
        
    
    }

    @FXML
    private void addExpenseType(ActionEvent event) throws ClassNotFoundException, SQLException {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmation Dialog");
        alert.setHeaderText(null);
        alert.setContentText("Are you soure to add expense category?");
        alert.initModality(Modality.APPLICATION_MODAL);
        Optional<ButtonType> action = alert.showAndWait();
        if (action.get() == ButtonType.OK) {
            connection = DatabaseConnection.getConnection();
            sql = "INSERT INTO EXPENSE_CATEGORY (EXPENSE_TYPE) VALUES(?)";
            statement = connection.prepareStatement(sql);
            statement.setString(1, expenseType.getText());
            statement.execute();
            showNotifications("Expense Category", "Expense Category add sucessfully");
            clearFields();
            
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
    @FXML
    private void updateExpenseType(ActionEvent event) throws ClassNotFoundException, SQLException {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmation Dialog");
        alert.setHeaderText(null);
        alert.setContentText("Are you soure to update expense Category?");
        alert.initModality(Modality.APPLICATION_MODAL);
        Optional<ButtonType> action = alert.showAndWait();
        if (action.get() == ButtonType.OK) {
            connection = DatabaseConnection.getConnection();
            sql = "UPDATE EXPENSE_CATEGORY SET EXPENSE_TYPE=? WHERE ID=?";
            statement = connection.prepareStatement(sql);
            statement.setString(1, expenseType.getText());
            statement.setString(2, txfCategoryID.getText());
            statement.execute();
            showNotifications("Expense Category", "Expense Category update  sucessfully");
            clearFields();
            
        }
    }

    @FXML
    private void deleteExpenseType(ActionEvent event) throws ClassNotFoundException, SQLException {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmation Dialog");
        alert.setHeaderText(null);
        alert.setContentText("Are you soure to delete expense Category?");
        alert.initModality(Modality.APPLICATION_MODAL);
        Optional<ButtonType> action = alert.showAndWait();
        if (action.get() == ButtonType.OK) {
            connection = DatabaseConnection.getConnection();
            sql = "DELETE FROM EXPENSE_CATEGORY  WHERE ID=?";
            statement = connection.prepareStatement(sql);
            statement.setString(1, txfCategoryID.getText());
            statement.execute();
            showNotifications("Expense Category", "Expense Category delete  sucessfully");
            clearFields();
            
        }
    }

    @FXML
    private void clearExpenseType(ActionEvent event) {
        clearFields();
    }
    private void clearFields(){
    
    expenseType.setText("");
   checkExpenseCategory.setSelected(false);
    txfCategoryID.setText("");
  }

    @FXML
    private void viewExpenseType(ActionEvent event) throws ClassNotFoundException, JRException {
        String sourceFileName = "F:\\schoolManagementSystem\\src\\expense\\expenseCategory.jrxml";

        String sourceFileNam2 = "F:\\schoolManagementSystem\\src\\expense\\expenseCategory.jasper";
        connection = DatabaseConnection.getConnection();
        Map parameters = new HashMap();
        JasperCompileManager.compileReportToFile(sourceFileName, sourceFileNam2);
        JasperPrint jprint = (JasperPrint) JasperFillManager.fillReport(sourceFileNam2, parameters, connection);
        JasperExportManager.exportReportToPdfFile(jprint, sourceFileNam2);
        JasperViewer jv = new JasperViewer(jprint, false);
        jv.viewReport(jprint, false);
    }

    
}
