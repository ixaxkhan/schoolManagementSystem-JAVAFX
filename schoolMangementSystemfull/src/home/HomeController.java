
package home;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;

import javafx.scene.layout.AnchorPane;



public class HomeController implements Initializable{
    //loading fxml through this variables

    AnchorPane   accountView ;
    AnchorPane attendenceView;
    AnchorPane employeeView;
    AnchorPane expenseView ;
    AnchorPane  transportView;
    AnchorPane studentView;
    AnchorPane inventoryView;
    AnchorPane reportView;
    AnchorPane massageView;
  
    @FXML
    private AnchorPane center;
    @FXML
    private Button btnStudent;
    @FXML
    private Button btnAttendence;
    @FXML
    private Button btnEmployee;
    @FXML
    private Button btnAccount;
    @FXML
    private Button btnTransport;
    @FXML
    private Button btnExpenses;
    @FXML
    private Button btnInventory;
    @FXML
    private Button btnReport;
    @FXML
    private Button btnMassage;

    @FXML
    private void closeWindow(ActionEvent event) {
        System.exit(0);
    }

    @FXML
    private void studentViewLoade(ActionEvent event) {
        
        Scene scene = btnStudent.getScene();
        AnchorPane lookup = (AnchorPane) scene.lookup("#center");
        lookup.getChildren().clear();
        lookup.getChildren().add(studentView);
    }

    @FXML
    private void attendenceViewLoade(ActionEvent event) {
        Scene scene = btnAttendence.getScene();
        AnchorPane lookup = (AnchorPane) scene.lookup("#center");
        lookup.getChildren().clear();
        lookup.getChildren().add(attendenceView);
    }

    @FXML
    private void employeeViewLoade(ActionEvent event) {
        Scene scene = btnEmployee.getScene();
        AnchorPane lookup = (AnchorPane) scene.lookup("#center");
        lookup.getChildren().clear();
        lookup.getChildren().add(employeeView);
    }

    @FXML
    private void AccountViewLoade(ActionEvent event) {
        Scene scene = btnAccount.getScene();
        AnchorPane lookup = (AnchorPane) scene.lookup("#center");
        lookup.getChildren().clear();
        lookup.getChildren().add(accountView);
    }

    @FXML
    private void transportViewLoade(ActionEvent event) {
        Scene scene = btnTransport.getScene();
        AnchorPane lookup = (AnchorPane) scene.lookup("#center");
        lookup.getChildren().clear();
        lookup.getChildren().add(transportView);
    }

    @FXML
    private void expenseViewLoade(ActionEvent event) {
        Scene scene = btnExpenses.getScene();
        AnchorPane lookup = (AnchorPane) scene.lookup("#center");
        lookup.getChildren().clear();
        lookup.getChildren().add(expenseView);
    }

    @FXML
    private void InventoryViewLoade(ActionEvent event) {
        Scene scene = btnInventory.getScene();
        AnchorPane lookup = (AnchorPane) scene.lookup("#center");
        lookup.getChildren().clear();
        lookup.getChildren().add(inventoryView);
    }

    @FXML
    private void reportViewLoade(ActionEvent event) {
        Scene scene = btnReport.getScene();
        AnchorPane lookup = (AnchorPane) scene.lookup("#center");
        lookup.getChildren().clear();
        lookup.getChildren().add(reportView);
    }

    @FXML
    private void massageViewLoade(ActionEvent event) {
        Scene scene = btnMassage.getScene();
        AnchorPane lookup = (AnchorPane) scene.lookup("#center");
        lookup.getChildren().clear();
        lookup.getChildren().add(massageView);
        
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
       
        try {
            accountView = (AnchorPane) FXMLLoader.load(getClass().getResource("/account/accountView.fxml"));
            attendenceView = (AnchorPane) FXMLLoader.load(getClass().getResource("/attendence/attendenceView.fxml"));
            employeeView = (AnchorPane) FXMLLoader.load(getClass().getResource("/employee/employeeView.fxml"));
            expenseView = (AnchorPane) FXMLLoader.load(getClass().getResource("/expense/expenseView.fxml"));
            studentView = (AnchorPane) FXMLLoader.load(getClass().getResource("/student/studentView.fxml"));
            transportView = (AnchorPane) FXMLLoader.load(getClass().getResource("/transport/transportView.fxml"));
            inventoryView = (AnchorPane) FXMLLoader.load(getClass().getResource("/expense/expenseView.fxml"));
            reportView = (AnchorPane) FXMLLoader.load(getClass().getResource("/student/studentView.fxml"));
            massageView = (AnchorPane) FXMLLoader.load(getClass().getResource("/transport/transportView.fxml"));

        } catch (IOException ex) {
            Logger.getLogger(HomeController.class.getName()).log(Level.SEVERE, null, ex);
        }
      
    }

   
    
    
    
}
