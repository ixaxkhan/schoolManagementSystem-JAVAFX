
package databaseconnection;


import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import javafx.scene.control.Alert;
import javafx.stage.Modality;

 

public class DatabaseConnection {
    
    private static Connection connection =null;
    private static Statement statement =null;
    private static ResultSet resultSet =null;
   //  static JdbcConnectionPool cp;
    public static Connection getConnection() throws ClassNotFoundException{
    
    try{
      
    Class.forName("org.h2.Driver");
   
   connection= DriverManager.getConnection("jdbc:h2:C:/reports/db/schoolManagement","root","");
       
    }catch(SQLException e){
         Alert alert= new Alert(Alert.AlertType.WARNING);
                alert.setContentText("connection to database is fill");
               alert.initModality(Modality.APPLICATION_MODAL);
              
                alert.show();
              e.printStackTrace();
    
    
    }
    return connection;
    }
    public static void closeConnecton(){
    
        try {
            connection.close();
        } catch (SQLException ex) {
             ex.printStackTrace();
    
        
        }
    
    }
     public static void closeStatement(){
    
        try {
            statement.close();
        } catch (SQLException ex) {
             ex.printStackTrace();
    
        
        }
    
    }
      public static void closeResultSet(){
    
        try {
           resultSet.close();
        } catch (SQLException ex) {
             ex.printStackTrace();
    
        
        }
    
    }
}
