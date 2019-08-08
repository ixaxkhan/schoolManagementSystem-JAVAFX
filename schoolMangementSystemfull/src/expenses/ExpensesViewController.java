
package expenses;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;


public class ExpensesViewController implements Initializable {
    @FXML
    private AnchorPane expenseCategory;
    @FXML
    private AnchorPane expenseAdd;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        try {
              AnchorPane expenseCategory2 = (AnchorPane) FXMLLoader.load(getClass().getResource("/expense/expenseCategoryView.fxml"));
              AnchorPane expenseAdd2 = (AnchorPane) FXMLLoader.load(getClass().getResource("/expense/expenseAddView.fxml"));
              expenseCategory.getChildren().clear();
              expenseCategory.getChildren().add(expenseCategory2);
              expenseAdd.getChildren().clear();
              expenseAdd.getChildren().add(expenseAdd2);
        } catch (IOException ex) {
            Logger.getLogger(ExpensesViewController.class.getName()).log(Level.SEVERE, null, ex);
        }
    
    
    }

    
}
