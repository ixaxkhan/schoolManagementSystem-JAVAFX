
package login;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import databaseconnection.DatabaseConnection;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import javafx.geometry.Pos;
import javafx.util.Duration;
import org.controlsfx.control.Notifications;


public class LoginController  implements Initializable{
    
    Connection connection = null;
    PreparedStatement statement = null;
    ResultSet resultSet = null;
     
    String sql = null;
     Stage stage;
  
    @FXML
    private ComboBox<String> comboRole;
    @FXML
    private TextField txfUserName;
    @FXML
    private Button loginbtn;
    @FXML
    private PasswordField txfPassword;
    public static AdminInfo adminInformation;

    @FXML
    private void loginToSystem(ActionEvent event) throws IOException, ClassNotFoundException, SQLException {
        String userName=null,password=null;
        int ID=0;
         connection = DatabaseConnection.getConnection();
        
          
        if (comboRole.getValue().equals("Principle")) {
            
            sql="SELECT * FROM SUPER_ADMIN where user_name=? and password=?";
            statement=connection.prepareStatement(sql);
            statement.setString(1,  txfUserName.getText());
            statement.setString(2,  txfPassword.getText());
            resultSet=statement.executeQuery();
            if(resultSet.next()){
            
            userName=resultSet.getString("USER_NAME");
            password= resultSet.getString("PASSWORD");
            
            
            }
            if (userName.equals(txfUserName.getText()) && password.equals(txfPassword.getText())) {
                Parent root;
                root = FXMLLoader.load(getClass().getResource("/login/superAdmin/superAdminView.fxml"));
                final Node source = (Node) event.getSource();
                 stage = (Stage) source.getScene().getWindow();
                
                Scene sceen = new Scene(root);
              
                stage.setScene(sceen);
                Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
                stage.setWidth(screenSize.getWidth());
                stage.setHeight(screenSize.getHeight() - 30);
                stage.show();
            } else {
                if ((txfPassword.getText().equals("")) && (txfUserName.getText().equals(""))) {
                    Notifications.create()
                            .text("Please enter password ans userName")
                            .hideAfter(Duration.seconds(3))
                            .darkStyle()
                            .hideCloseButton()
                            .position(Pos.TOP_LEFT)
                            .showInformation();

                } else {
                    Notifications.create()
                            
                            .text("password ans userName Wrong")
                            .hideAfter(Duration.seconds(3))
                            .darkStyle()
                            .hideCloseButton()
                            .position(Pos.TOP_LEFT)
                            .showError();

                }
            }

        } else {
            
            sql = "SELECT * FROM ADMIN where  user_name=?  and password=?";
            statement=connection.prepareStatement(sql);
            statement.setString(1, txfUserName.getText());
            statement.setString(2, txfPassword.getText());
            resultSet = statement.executeQuery();
            if (resultSet.next()) {

                userName = resultSet.getString("USER_NAME");
                password = resultSet.getString("PASSWORD");
                

            }
            if (userName.equals(txfUserName.getText()) && password.equals(txfPassword.getText())) {
                Parent root;
                root = FXMLLoader.load(getClass().getResource("/home/dashbord.fxml"));
                final Node source = (Node) event.getSource();
                stage = (Stage) source.getScene().getWindow();
               
                Scene sceen = new Scene(root);

                stage.setScene(sceen);
                Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
                stage.setWidth(screenSize.getWidth());
                stage.setHeight(screenSize.getHeight() - 30);
                stage.show();

            } else {
                if ((txfPassword.getText().equals("")) && (txfUserName.getText().equals(""))) {
                    
                    Notifications.create()
                            
                            .text("Please enter password ans userName")
                            .hideAfter(Duration.seconds(3))
                            .darkStyle()
                            .hideCloseButton()
                            .position(Pos.TOP_LEFT)
                            .showError();

                } else {
                   
                    Notifications.create()
                            .text("Wrong password and userName")
                            .hideAfter(Duration.seconds(3))
                            .darkStyle()
                            .hideCloseButton()
                            .position(Pos.TOP_LEFT)
                            .showError();

                }
            } 
            
      
        }
    }
    
    @Override
    public void initialize(URL location, ResourceBundle resources) {

        comboRole.getItems().addAll("Principle", "Cleark");

    }
        
        
    }

    

