
package Report;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.effect.BoxBlur;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

/**
 * FXML Controller class
 *
 * @author khan
 */
public class ReportManagementController implements Initializable {

   
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
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

    @FXML
    private void studentReports(ActionEvent event) throws IOException {
        loadReportFXML(event, "/Report/inventoryReports/studentReportView22.fxml");
       
    }

    @FXML
    private void attendenceReports(ActionEvent event) throws IOException{
        loadReportFXML(event,"/Report/attendenceReports/attendenceReportView.fxml");
    }

    @FXML
    private void inventoryReports(ActionEvent event) throws IOException{
        loadReportFXML(event,"/Report/inventoryReports/inventoryReportView.fxml");
    }

    @FXML
    private void employeeReports(ActionEvent event)throws IOException {
        loadReportFXML(event,"/Report/employeeReports/employeeReportView.fxml");
    }

    @FXML
    private void transportsReports(ActionEvent event) throws IOException{
        loadReportFXML(event,"/Report/expenseReports/tranportReportView.fxml");
    }

    @FXML
    private void expensesReports(ActionEvent event)throws IOException {
        loadReportFXML(event,"/Report/expenseReports/expenseReportView.fxml");
    }

    @FXML
    private void accountReports(ActionEvent event)throws IOException {
        loadReportFXML(event,"/Report/accountReports/accountReportView.fxml");
    }
    private void loadReportFXML(ActionEvent event,String path) throws IOException{
    
        final Node source = (Node) event.getSource();
        final Stage primaryStage = (Stage) source.getScene().getWindow();
        Parent root = (Parent)FXMLLoader.load(getClass().getResource(path));
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
}
