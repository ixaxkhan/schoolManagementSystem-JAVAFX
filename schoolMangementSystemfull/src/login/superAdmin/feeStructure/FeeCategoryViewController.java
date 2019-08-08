/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package login.superAdmin.feeStructure;

import com.jfoenix.controls.JFXCheckBox;
import com.jfoenix.controls.JFXTextField;
import databaseconnection.DatabaseConnection;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
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
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.xml.JRXmlLoader;
import net.sf.jasperreports.view.JasperViewer;
import org.controlsfx.control.Notifications;

/**
 * FXML Controller class
 *
 * @author khan
 */
public class FeeCategoryViewController implements Initializable {
    @FXML
    private JFXTextField categoryName;
    @FXML
    private JFXCheckBox checkBox;
    @FXML
    private JFXTextField categoryID;
    //************** for database connection***********
    Connection connection = null;
    PreparedStatement statement = null;
    ResultSet resultSet = null;
    String sql = null;
      
    @Override
    public void initialize(URL url, ResourceBundle rb) {
         categoryID.setVisible(false);
         loadCategory();
         abledisableTXFID();
         
    }    

    @FXML
    private void save(ActionEvent event) throws ClassNotFoundException, SQLException {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmation Dialog");
        alert.setHeaderText(null);
        alert.setContentText("Are you soure to save fee category??");
        alert.initModality(Modality.APPLICATION_MODAL);
        Optional<ButtonType> action = alert.showAndWait();
        if (action.get() == ButtonType.OK) {
        connection = DatabaseConnection.getConnection();
        sql = "INSERT INTO fee_type(fee_category) VALUES(?) ";
        statement = connection.prepareStatement(sql);
        statement.setString(1,categoryName.getText() );
        statement.execute();
        showNotifications("Fee Category","Fee category add sucessfully");
        categoryName.setText("");
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
    private void update(ActionEvent event) throws ClassNotFoundException, SQLException  {
       
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmation Dialog");
        alert.setHeaderText(null);
        alert.setContentText("Are you soure to upate fee category?");
        alert.initModality(Modality.APPLICATION_MODAL);
        Optional<ButtonType> action = alert.showAndWait();
        if (action.get() == ButtonType.OK) {
            connection = DatabaseConnection.getConnection();
            sql = "update fee_type fee_category=? where id=? ";
            statement = connection.prepareStatement(sql);
            statement.setString(1, categoryName.getText());
            statement.setString(2, categoryID.getText());
            statement.execute();
            showNotifications("Fee Category", "Fee category update sucessfully");
            categoryName.setText("");
        }
    }

    @FXML
    private void delete(ActionEvent event) throws ClassNotFoundException, SQLException {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmation Dialog");
        alert.setHeaderText(null);
        alert.setContentText("Are you soure to delete fee category?");
        alert.initModality(Modality.APPLICATION_MODAL);
        Optional<ButtonType> action = alert.showAndWait();
        if (action.get() == ButtonType.OK) {
            connection = DatabaseConnection.getConnection();
            sql = "delete from fee_type  where id=? ";
            statement = connection.prepareStatement(sql);
            statement.setString(1, categoryID.getText());
            statement.execute();
            showNotifications("Fee Category", "Fee category delete sucessfully");
            categoryName.setText("");
        }
    }

    @FXML
    private void view(ActionEvent event) throws JRException, ClassNotFoundException {
        connection = DatabaseConnection.getConnection();
        
        File file = new File("src/login/superAdmin/feeStructure/Fee_type.jrxml");
        JasperDesign jasperDesign = JRXmlLoader.load("C:\\reports\\Fee_type.jrxml");
        JasperReport jasperReport = JasperCompileManager.compileReport(jasperDesign);
        JasperPrint JasperPrint = (JasperPrint) JasperFillManager.fillReport(jasperReport, null, connection);
        JasperViewer jv = new JasperViewer(JasperPrint, false);
        JasperViewer.viewReport(JasperPrint, false);
    }
    
    
    
    private void loadCategory(){
        categoryID.textProperty().addListener((observable, oldValue, newValue) -> {
        
            
            try {
                connection = DatabaseConnection.getConnection();
                sql = "select * from fee_type where id=? ";
                statement = connection.prepareStatement(sql);
                statement.setInt(1, Integer.parseInt(newValue));
                resultSet=statement.executeQuery();
                if(resultSet.next()){
                
                    categoryName.setText(resultSet.getString("fee_category"));
                
                
                }else{
                
                    categoryName.setText("");
                
                }
                
                
                
                
            } catch (SQLException | ClassNotFoundException ex) {
                Logger.getLogger(FeeCategoryViewController.class.getName()).log(Level.SEVERE, null, ex);
            }
            
        
        
        
        });
    
    
    }
    private void abledisableTXFID(){
    
        checkBox.selectedProperty().addListener(new ChangeListener<Boolean>() {
            public void changed(ObservableValue<? extends Boolean> ov,
                    Boolean old_val, Boolean new_val) {
                if (new_val) {
                    categoryID.setVisible(true);

                } else {

                    categoryID.setVisible(false);
                    categoryID.setText("");
                }
            }
        });
    
    
    }
    
    
    @FXML
    private void closeWindow(ActionEvent event) {
        final Node source = (Node) event.getSource();
        Stage stage = (Stage) source.getScene().getWindow();
        stage.close();
    }

    @FXML
    private void addTotionFeeToClass(ActionEvent event) throws IOException {
        Parent root;
        root = FXMLLoader.load(getClass().getResource("classToutionFeeView.fxml"));
        final Node source = (Node) event.getSource();
        final Stage stage2 = (Stage) source.getScene().getWindow();
        stage2.close();
        Scene sceen = new Scene(root);
        Stage stage= new Stage();
        stage.setScene(sceen);
        stage.show();
    }
    
}
