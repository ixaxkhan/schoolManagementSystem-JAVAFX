/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package home;

import databaseconnection.DatabaseConnection;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
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

import javafx.scene.chart.PieChart;
import javafx.scene.control.Label;

import javafx.stage.Stage;
import javafx.stage.WindowEvent;

/**
 * FXML Controller class
 *
 * @author khan
 */
public class DashbordController implements Initializable {
    
    @FXML
    private PieChart pieChart;
    @FXML
    private Label labTotalAmount;
    @FXML
    private Label labTotalDues;
    @FXML
    private Label labTotalStudent;
    @FXML
    private Label labAbsentStudent;
    
      //************** for database connection***********
    Connection connection = null;
    PreparedStatement statement = null;
    ResultSet resultSet = null;
    String sql = null;

    Stage stage;
    @Override
    public void initialize(URL url, ResourceBundle rb) {
       //stage = (Stage) labAbsentStudent.getScene().getWindow();
        try {
            totalStudent();
            totalAbsentStudent();
            totalAmount();
            totalDues();
        } catch (ClassNotFoundException | SQLException ex) {
            Logger.getLogger(DashbordController.class.getName()).log(Level.SEVERE, null, ex);
        }
       
    }          
    private void totalStudent() throws ClassNotFoundException, SQLException{
      int totalStudent=0;
     connection = DatabaseConnection.getConnection();
     sql="select * from student";
     statement=connection.prepareStatement(sql);
    resultSet=statement.executeQuery();
     while(resultSet.next()){
     
     totalStudent++;
     
     }
      PieChart.Data slice1 = new PieChart.Data("Total Student", totalStudent);
       pieChart.getData().add(slice1);
       
    labTotalStudent.setText(""+totalStudent);
    
    }
    private void totalAbsentStudent() throws ClassNotFoundException, SQLException {
        int absentStudent=0;
        java.util.Date myDate = new java.util.Date();
        java.sql.Date sqlDate = new java.sql.Date(myDate.getTime());
        LocalDate toLocalDate = sqlDate.toLocalDate();
       
      
        connection = DatabaseConnection.getConnection();
        sql = "SELECT * FROM ATTENDENCE WHERE DATE =?";
        statement = connection.prepareStatement(sql);
        statement.setDate(1, Date.valueOf(toLocalDate));
        resultSet = statement.executeQuery();
        while (resultSet.next()) {

            if(resultSet.getInt("STATUS")==1){
                absentStudent++;
            
            }

        }
        PieChart.Data slice2 = new PieChart.Data("Absent Students", absentStudent);
        pieChart.getData().add(slice2);
        labAbsentStudent.setText("" + absentStudent);

    }
       private void totalAmount() throws ClassNotFoundException, SQLException {
           connection = DatabaseConnection.getConnection();
           double totalAmount = 0;
           int year = 0;
           java.util.Date myDate = new java.util.Date();
           java.sql.Date sqlDate = new java.sql.Date(myDate.getTime());
           LocalDate toLocalDate = sqlDate.toLocalDate();
           int month = Date.valueOf(toLocalDate).getMonth();
           String date = "" + Date.valueOf(toLocalDate);
           String year2[] = date.split("-");
           year = Integer.parseInt(year2[0]);
           int date2 = Date.valueOf(toLocalDate).getDate();
           sql = "SELECT * FROM STUDENT_FEE WHERE DATE BETWEEN ? AND ? AND STATUS=? or STATUS=?";
           statement = connection.prepareStatement(sql);
           if (month == 12) {
               year++;
               month = 1;
               statement.setDate(1, Date.valueOf(toLocalDate));
               statement.setDate(2, Date.valueOf("" + year + "-" + month + "-" + date2));
               statement.setString(3, "Paid");
               statement.setString(4, "Not Full Paid");
               resultSet = statement.executeQuery();
               while (resultSet.next()) {
                   switch (resultSet.getString("STATUS")) {
                       case "Paid":
                           totalAmount = totalAmount + resultSet.getDouble("AMOUNT");
                           break;
                       case "Not Full Paid":
                           totalAmount=totalAmount+resultSet.getDouble("PAID_AMOUNT");
                           break;
                   }

               }
               PieChart.Data slice3 = new PieChart.Data("Total Amount", totalAmount);
               pieChart.getData().add(slice3);
               labTotalAmount.setText("" + totalAmount);
           } else {

               month = month + 2;
               statement.setDate(1, Date.valueOf(toLocalDate));
               statement.setDate(2, Date.valueOf("" + year + "-" + month + "-" + date2));
               statement.setString(3, "Paid");
               statement.setString(4, "Not Full Paid");
               resultSet = statement.executeQuery();
               while (resultSet.next()) {
                   switch (resultSet.getString("STATUS")) {
                       case "Paid":
                           totalAmount = totalAmount + resultSet.getDouble("AMOUNT");
                           break;
                       case "Not Full Paid":
                           totalAmount = totalAmount + resultSet.getDouble("PAID_AMOUNT");
                           break;
                   }

               }
               PieChart.Data slice3 = new PieChart.Data("Total Amount", totalAmount);
               pieChart.getData().add(slice3);
               labTotalAmount.setText("" + totalAmount);
           }


    }
       
    private void totalDues()throws ClassNotFoundException, SQLException {
        connection = DatabaseConnection.getConnection();
        double totaldues = 0;
        int year = 0;
        java.util.Date myDate = new java.util.Date();
        java.sql.Date sqlDate = new java.sql.Date(myDate.getTime());
        LocalDate toLocalDate = sqlDate.toLocalDate();
        int month = Date.valueOf(toLocalDate).getMonth();
        String date = "" + Date.valueOf(toLocalDate);
        String year2[] = date.split("-");
        year = Integer.parseInt(year2[0]);
        int date2 = Date.valueOf(toLocalDate).getDate();
        sql = "SELECT * FROM STUDENT_FEE WHERE DATE BETWEEN ? AND ? AND STATUS=? or STATUS=?";
        statement = connection.prepareStatement(sql);
        if (month == 12) {
            year++;
            month = 1;
            statement.setDate(1, Date.valueOf(toLocalDate));
            statement.setDate(2, Date.valueOf("" + year + "-" + month + "-" + date2));
            statement.setString(3, "Not Full Paid");
            statement.setString(4, "Not Paid");
            resultSet = statement.executeQuery();
            while (resultSet.next()) {
               
                switch (resultSet.getString("STATUS")) {
                    case "Not Paid":
                        totaldues = totaldues + resultSet.getDouble("AMOUNT");
                        break;
                    case "Not Full Paid":
                        totaldues = totaldues + resultSet.getDouble("dues");
                        break;
                }

            }
            PieChart.Data slice4 = new PieChart.Data("Dues", totaldues);
              pieChart.getData().add(slice4);
            labTotalDues.setText("" + totaldues);
        } else {

            month = month + 2;
            statement.setDate(1, Date.valueOf(toLocalDate));
            statement.setDate(2, Date.valueOf("" + year + "-" + month + "-" + date2));
            statement.setString(3, "Not Full Paid");
            statement.setString(4, "Not Paid");
            resultSet = statement.executeQuery();
            while (resultSet.next()) {
                
                switch (resultSet.getString("STATUS")) {
                    case "Not Paid":
                        totaldues = totaldues + resultSet.getDouble("AMOUNT");
                        break;
                    case "Not Full Paid":
                        totaldues = totaldues + resultSet.getDouble("dues");
                        break;
                }

            }
            PieChart.Data slice4 = new PieChart.Data("Dues", totaldues);
            pieChart.getData().add(slice4);
         labTotalDues.setText("" + totaldues);
        }
 
        
        
    }
    
    @FXML
    private void studentMangement(ActionEvent event) throws IOException {
        
        loadFXML("/student/Registration/studentRegistration.fxml",event );
    }

    @FXML
    private void employeeManagement(ActionEvent event) throws IOException {
        
       loadFXML("/employee/jjj.fxml",event );
    }
     private void loadFXML(String path,ActionEvent event ) throws IOException{
       
         Parent root;
         root = FXMLLoader.load(getClass().getResource(path));
         final Node source = (Node) event.getSource();
          stage = (Stage) source.getScene().getWindow();

         Scene sceen = new Scene(root);

         stage.setScene(sceen);
         Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
         stage.setWidth(screenSize.getWidth());
         stage.setHeight(screenSize.getHeight() - 30);
         stage.show();
         stage.setOnHidden(new EventHandler<WindowEvent>() {

            @Override
            public void handle(WindowEvent event) {

                DatabaseConnection.closeConnecton();
            }
        });
     
     }
    @FXML
    private void AttendenceManagement(ActionEvent event) throws IOException {
        loadFXML("/attendence/attendenceMangement.fxml",event );
       
    }

    @FXML
    private void accountManagement(ActionEvent event) throws IOException {
        loadFXML("/account/accountManagement.fxml",event );
       
    }

    @FXML
    private void expenseManagement(ActionEvent event) throws IOException {
         loadFXML("/expenses/expensesMangement.fxml",event );
        
    }

    @FXML
    private void transportManagement(ActionEvent event) throws IOException {
        loadFXML("/transport/transportManagement.fxml", event);
    }

    @FXML
    private void inventoryMangement(ActionEvent event) throws IOException {
        loadFXML("/inventory/inventoryMangement.fxml", event);
    }

    @FXML
    private void reportMangement(ActionEvent event) throws IOException {
        loadFXML("/Report/reportMangement.fxml", event);
    }

    @FXML
    private void smsManagement(ActionEvent event) throws IOException {
         loadFXML("/SMS/smsManagement.fxml", event);
    }

   

    @FXML
    private void logout(ActionEvent event) throws IOException {
              
              Parent  root = FXMLLoader.load(getClass().getResource("/login/loginView.fxml"));
                
               Stage stage = (Stage) labTotalDues.getScene().getWindow();
               
                Scene sceen = new Scene(root);

                stage.setScene(sceen);
                Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
                stage.setWidth(screenSize.getWidth());
                stage.setHeight(screenSize.getHeight() - 30);
                stage.show();
                DatabaseConnection.closeConnecton();
    }

    
}
