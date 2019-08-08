
package expenses;

import databaseconnection.DatabaseConnection;
import java.net.URL;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
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
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.stage.Modality;
import javafx.util.Duration;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.view.JasperViewer;
import org.controlsfx.control.Notifications;


public class ExpenseAddViewController implements Initializable{
     //************** for database connection***********
    Connection connection = null;
    PreparedStatement statement = null;
    ResultSet resultSet = null;
    String sql = null;
    //add portion variables
    @FXML
    private ComboBox<String> ComboExpenseType;
    @FXML
    private DatePicker txfDate;
    @FXML
    private TextField txfExpenseAmount;
    @FXML
    private CheckBox checkOperation;
    @FXML
    private TextField txfExpensePrimaryID;
    @FXML
    private Label labelID;
    
    //search portion variables
    @FXML
    private DatePicker txfDateFrom;
    @FXML
    private DatePicker txfDateTo;
    @FXML
    private ComboBox<String> comboExpenseTypeSearch;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        labelID.setVisible(false);
        txfExpensePrimaryID.setVisible(false);
        checkOperation.selectedProperty().addListener(new ChangeListener<Boolean>() {
            public void changed(ObservableValue<? extends Boolean> ov,
                    Boolean old_val, Boolean new_val) {
                if (new_val) {
                    labelID.setVisible(true);
                    txfExpensePrimaryID.setVisible(true);

                } else {

                    labelID.setVisible(false);
                    txfExpensePrimaryID.setVisible(false);
                }
            }
        });
        //******************************** LOADING DATA INTO FIELD WHEN DATA ENTRY INTO ID FIELDS*************************
        txfExpensePrimaryID.textProperty().addListener((observable, oldValue, newValue) -> {

            try {
                connection = DatabaseConnection.getConnection();
                sql = "SELECT 	EXPENSE_AMOUNT,DATE,EXPENSE_CATEGORY_PRIM FROM EXPENSE_RECORDS WHERE ID =?";
                statement = connection.prepareStatement(sql);
                statement.setString(1, newValue);
                resultSet = statement.executeQuery();
                if (resultSet.next()) {
                    txfExpenseAmount.setText(""+resultSet.getDouble("EXPENSE_AMOUNT"));
                    String prim= resultSet.getString("EXPENSE_RECORDS.EXPENSE_CATEGORY_PRIM");
                    txfDate.setValue(resultSet.getDate("DATE").toLocalDate());
                    
                   sql="SELECT * FROM EXPENSE_CATEGORY WHERE EXPENSE_CATEGORY.ID=? ";
                   statement = connection.prepareStatement(sql);
                   statement.setString(1, prim);
                   resultSet=statement.executeQuery();
                   if(resultSet.next()){
                   ComboExpenseType.setValue(resultSet.getString("EXPENSE_CATEGORY.EXPENSE_TYPE"));
                   }
                    
                } else {

                    ComboExpenseType.setValue(null);
                    txfDate.setValue(null);
                    txfExpenseAmount.setText("");
                  
                    

                }
            } catch (ClassNotFoundException | SQLException ex) {
                Logger.getLogger(expenseCategoryViewController.class.getName()).log(Level.SEVERE, null, ex);
            }

        });
        
        try {
            loadExpenseType(ComboExpenseType) ;
            loadExpenseType(comboExpenseTypeSearch) ;
            
            
            
        } catch (ClassNotFoundException | SQLException ex) {
            Logger.getLogger(ExpenseAddViewController.class.getName()).log(Level.SEVERE, null, ex);
        }
    
    }
   //ADD EXPENSES METHODS
    @FXML
    private void saveExpenses(ActionEvent event) throws ClassNotFoundException, SQLException {
        String expenseCategoryPrimaryKey = null;
        connection = DatabaseConnection.getConnection();
        sql="SELECT * FROM  EXPENSE_CATEGORY WHERE EXPENSE_TYPE =?";
        statement = connection.prepareStatement(sql);
        statement.setString(1,ComboExpenseType.getValue());
        resultSet=statement.executeQuery();
        if(resultSet.next()){
        
        expenseCategoryPrimaryKey=resultSet.getString("ID");
        }
         Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmation Dialog");
        alert.setHeaderText(null);
        alert.setContentText("Are you soure to add expense ?");
        alert.initModality(Modality.APPLICATION_MODAL);
        Optional<ButtonType> action = alert.showAndWait();
        if (action.get() == ButtonType.OK) {
            sql = "INSERT INTO  EXPENSE_RECORDS (EXPENSE_AMOUNT,DATE,EXPENSE_CATEGORY_PRIM  )VALUES(?,?,?)";
            statement = connection.prepareStatement(sql);
            statement.setDouble(1, Double.parseDouble(txfExpenseAmount.getText()));
            statement.setDate(2, Date.valueOf(txfDate.getValue()));
            statement.setString(3, expenseCategoryPrimaryKey);
            statement.execute();
            showNotifications("Expense Add ", "Expense add sucessfully");
            resetFields();
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
    private void updateExpenses(ActionEvent event) throws SQLException, ClassNotFoundException {
        String expenseCategoryPrimaryKey = null;
        connection = DatabaseConnection.getConnection();
        sql = "SELECT * FROM  EXPENSE_CATEGORY WHERE EXPENSE_TYPE =?";
        statement = connection.prepareStatement(sql);
        statement.setString(1, ComboExpenseType.getValue());
        resultSet = statement.executeQuery();
        if (resultSet.next()) {

            expenseCategoryPrimaryKey = resultSet.getString("ID");
        }
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmation Dialog");
        alert.setHeaderText(null);
        alert.setContentText("Are you soure to update expense ?");
        alert.initModality(Modality.APPLICATION_MODAL);
        Optional<ButtonType> action = alert.showAndWait();
        if (action.get() == ButtonType.OK) {
            sql = "UPDATE   EXPENSE_RECORDS SET EXPENSE_AMOUNT=?,DATE=?,EXPENSE_CATEGORY_PRIM=? WHERE ID=?";
            statement = connection.prepareStatement(sql);
            statement.setDouble(1, Double.parseDouble(txfExpenseAmount.getText()));
            statement.setDate(2, Date.valueOf(txfDate.getValue()));
            statement.setString(3, expenseCategoryPrimaryKey);
            statement.setString(4, txfExpensePrimaryID.getText());
            statement.execute();
            showNotifications("Expense Update ", "Expense update sucessfully");
            resetFields();
        }
        
        
    }

    @FXML
    private void deleteExpenses(ActionEvent event) throws ClassNotFoundException, SQLException {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmation Dialog");
        alert.setHeaderText(null);
        alert.setContentText("Are you soure to delete expense ?");
        alert.initModality(Modality.APPLICATION_MODAL);
        Optional<ButtonType> action = alert.showAndWait();
        if (action.get() == ButtonType.OK) {
            connection = DatabaseConnection.getConnection();
            sql = "DELETE FROM   EXPENSE_RECORDS   WHERE ID=?";
            statement.setString(1, txfExpensePrimaryID.getText());
            statement.execute();
            showNotifications("Expense Delete ", "Expense delete sucessfully");
            resetFields();
        }
    }

    @FXML
    private void resetFields(ActionEvent event) {
        resetFields();
    }
    private void resetFields(){
    
        ComboExpenseType.setValue(null);
        txfDate.setValue(null);
        txfExpenseAmount.setText("");
        checkOperation.setSelected(false);
        txfExpensePrimaryID.setText("");

    }

    @FXML
    private void viewExpenses(ActionEvent event) throws ClassNotFoundException, JRException {
        String sourceFileName = "F:\\schoolManagementSystem\\src\\expense\\expenseAdd.jrxml";

        String sourceFileNam2 = "F:\\schoolManagementSystem\\src\\expense\\expenseAdd.jasper";
        connection = DatabaseConnection.getConnection();
        Map parameters = new HashMap();
        JasperCompileManager.compileReportToFile(sourceFileName, sourceFileNam2);
        JasperPrint jprint = (JasperPrint) JasperFillManager.fillReport(sourceFileNam2, parameters, connection);
        JasperExportManager.exportReportToPdfFile(jprint, sourceFileNam2);
        JasperViewer jv = new JasperViewer(jprint, false);
        jv.viewReport(jprint, false);
    }
// SEARCH PORTION METHODS
    @FXML
    private void viewSearch(ActionEvent event) throws ClassNotFoundException, JRException, SQLException {
        String expenseCategoryPrim=null;
        String sourceFileName = "F:\\schoolManagementSystem\\src\\expense\\expenseSearch.jrxml";

        String sourceFileNam2 = "F:\\schoolManagementSystem\\src\\expense\\expenseSearch.jasper";
        connection = DatabaseConnection.getConnection();
        sql = "SELECT * FROM  EXPENSE_CATEGORY WHERE EXPENSE_TYPE =?";
        statement = connection.prepareStatement(sql);
        statement.setString(1,comboExpenseTypeSearch.getValue() );
        resultSet=statement.executeQuery();
        if(resultSet.next()){
        
            expenseCategoryPrim=resultSet.getString("ID");
        }
        Map parameters = new HashMap();
        parameters.put("FROM", Date.valueOf(txfDateFrom.getValue()));
        parameters.put("TO", Date.valueOf(txfDateTo.getValue()));
        parameters.put("EXPENSECATEGORYPRIM",  expenseCategoryPrim);
        JasperCompileManager.compileReportToFile(sourceFileName, sourceFileNam2);
        JasperPrint jprint = (JasperPrint) JasperFillManager.fillReport(sourceFileNam2, parameters, connection);
        JasperExportManager.exportReportToPdfFile(jprint, sourceFileNam2);
        JasperViewer jv = new JasperViewer(jprint, false);
        jv.viewReport(jprint, false);
        
    }

    @FXML
    private void resetSearchFields(ActionEvent event) {
    }
    
    private void loadExpenseType(ComboBox ComboExpenseType) throws ClassNotFoundException, SQLException{
        ObservableList <String> expenseType= FXCollections.observableArrayList();
        connection = DatabaseConnection.getConnection();
        sql = "SELECT * FROM EXPENSE_CATEGORY";
        statement = connection.prepareStatement(sql);
        resultSet= statement.executeQuery();
        expenseType.clear();
        while(resultSet.next()){
        
        expenseType.add(resultSet.getString("EXPENSE_TYPE"));
        
        }
        
        ComboExpenseType.getItems().clear();
        ComboExpenseType.setItems(expenseType);
    
    }

    @FXML
    private void loadeExpensesType(ActionEvent event) {
      
        try {
            loadExpenseType(ComboExpenseType);
            loadExpenseType(comboExpenseTypeSearch);

        } catch (ClassNotFoundException | SQLException ex) {
            Logger.getLogger(ExpenseAddViewController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}

