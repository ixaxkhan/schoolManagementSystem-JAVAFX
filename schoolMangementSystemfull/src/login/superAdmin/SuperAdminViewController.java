
package login.superAdmin;

import com.jfoenix.controls.JFXButton;
import databaseconnection.DatabaseConnection;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ToggleGroup;
import javafx.scene.effect.BoxBlur;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.stage.WindowEvent;

/**
 * FXML Controller class
 *
 * @author khan
 */
public class SuperAdminViewController implements Initializable {

    //THIS FOR ADMIN GENDER
    ToggleGroup group;
    @FXML
    private MenuItem logoutbtn;
    @FXML
    private JFXButton homebtn;
    
    AnchorPane homeView,logoutView;
    @FXML
    private AnchorPane center;
    Scene scene;
    Stage primaryStage;
    @Override
    public void initialize(URL url, ResourceBundle rb) {
         //scene = homebtn.getScene();
        
        try {
            homeView = (AnchorPane) FXMLLoader.load(getClass().getResource("/home/dashbord.fxml"));
            logoutView = (AnchorPane) FXMLLoader.load(getClass().getResource("/login/loginView.fxml"));

        } catch (IOException ex) {
            Logger.getLogger(SuperAdminViewController.class.getName()).log(Level.SEVERE, null, ex);
        }
    
    }    

    @FXML
    private void feeStructure(ActionEvent event)throws IOException {
        loadFXML2("/login/superAdmin/feeStructure/feeCategoryView.fxml", event);
    }

    @FXML
    private void backup(ActionEvent event)throws IOException {
        loadFXML2("/login/superAdmin/backup/backupView.fxml", event);
    }

    @FXML
    private void restore(ActionEvent event)throws IOException {
        loadFXML2("/login/superAdmin/backup/restoreView.fxml", event);
    }

    @FXML
    private void addAdmin(ActionEvent event) throws IOException {
        loadFXML2("/login/superAdmin/AddNewAdmin/newAdminView.fxml", event);
    }
   

    @FXML
    private void changeSchoolInfo(ActionEvent event) throws IOException {
        loadFXML2("/login/superAdmin/SchoolInfo/schoolInfoView.fxml",event);
   
    }

    @FXML
    private void StandardandSection(ActionEvent event)throws IOException {
        loadFXML2("/login/superAdmin/standard/standard&sectionView.fxml", event);
    }

    private void loadFXML2(String path,ActionEvent event)throws IOException{
    
        final Node source = (Node) event.getSource();
         primaryStage = (Stage) source.getScene().getWindow();
        //  Stage stage=new Stage();
        Parent root = FXMLLoader.load(getClass().getResource(path));
        final Stage dialog = new Stage(StageStyle.UNDECORATED);
        dialog.initModality(Modality.WINDOW_MODAL);
        dialog.initOwner(primaryStage);

        scene = new Scene(root);
        dialog.setScene(scene);
        BoxBlur bb = new BoxBlur();
        bb.setWidth(5);
        bb.setHeight(5);
        bb.setIterations(3);

        dialog.show();
        primaryStage.setOnHidden(new EventHandler<WindowEvent>() {

            @Override
            public void handle(WindowEvent event) {

                DatabaseConnection.closeConnecton();
            }
        });
    
    }

    @FXML
    private void myProfile(ActionEvent event) throws IOException{
     
        primaryStage = (Stage) center.getScene().getWindow();
       
        Parent root = FXMLLoader.load(getClass().getResource("/login/superAdmin/profileView.fxml"));
        final Stage dialog = new Stage(StageStyle.UNDECORATED);
        dialog.initModality(Modality.WINDOW_MODAL);
        dialog.initOwner(primaryStage);

        scene = new Scene(root);
        dialog.setScene(scene);
        BoxBlur bb = new BoxBlur();
        bb.setWidth(5);
        bb.setHeight(5);
        bb.setIterations(3);

        dialog.show();
  
        
        
    }

    @FXML
    private void logout(ActionEvent event) {
       
       // AnchorPane lookup = (AnchorPane) scene.lookup("#center");
       center.getChildren().clear();
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        center.setPrefWidth(screenSize.getWidth());
        center.setPrefHeight(screenSize.getHeight() - 30);
        logoutView.setPrefWidth( center.getPrefWidth());
        logoutView.setPrefHeight( center.getPrefHeight());
       center.getChildren().add(logoutView);
         DatabaseConnection.closeConnecton();
    }

    @FXML
    private void gotoHome(ActionEvent event) {
      scene = homebtn.getScene();
        AnchorPane lookup = (AnchorPane) scene.lookup("#center");
        lookup.getChildren().clear();
        lookup.getChildren().add(homeView);
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        //stage.setWidth(screenSize.getWidth());
       // stage.setHeight(screenSize.getHeight() - 30);
        lookup.setPrefWidth(screenSize.getWidth());
        lookup.setPrefHeight(screenSize.getHeight() - 30);
        homeView.setPrefWidth(lookup.getPrefWidth());
        homeView.setPrefHeight(lookup.getPrefHeight());
    }
}
