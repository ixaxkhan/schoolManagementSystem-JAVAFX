/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package student.Registration;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXCheckBox;
import databaseconnection.DatabaseConnection;
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TreeTableColumn;
import javafx.scene.control.cell.PropertyValueFactory;

import javafx.util.Callback;
import javafx.util.Duration;
import org.controlsfx.control.Notifications;


/**
 * FXML Controller class
 *
 * @author khan
 */
public class StudentPromotViewController implements Initializable {
    @FXML
    private ComboBox<String> comboExistingClass;
    @FXML
    private ComboBox<String> comboExistingSection;
    @FXML
    private ComboBox<String> comboPromotClass;
    @FXML
    private ComboBox<String> comboPromotSection;
    @FXML
    private JFXCheckBox checkBoxAlumi;
    @FXML
    private TableColumn<PromotionViewModal, Integer> tbRegNo;
    @FXML
    private TableColumn<PromotionViewModal, String> tbName;
    @FXML
    private TableColumn<PromotionViewModal, String> tbFName;
    @FXML
    private TableColumn<PromotionViewModal, String> tbGender;
    @FXML
    private TableColumn<PromotionViewModal, Boolean> action;
    @FXML
    private TableView<PromotionViewModal> table;
    //************** for database connection***********
    Connection connection = null;
    PreparedStatement statement = null;
    ResultSet resultSet = null;
    String sql = null;
    // **************for class and section observable list*******************
    ObservableList<String> classesNames = FXCollections.observableArrayList();
    ObservableList<String>existingSectionName = FXCollections.observableArrayList();
    
    
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        try {
            
            loadsPromotClass();
            loadsExistingClass();
            loadsExistingSections();
            loadsPromotSections();
            loadsExistingSections();
            loadsPromotSections();
            loadingStudentIntoTable();
            
        } catch (SQLException | ClassNotFoundException ex) {
            Logger.getLogger(StudentPromotViewController.class.getName()).log(Level.SEVERE, null, ex);
        }
        tbRegNo.setCellValueFactory(new PropertyValueFactory<>("regNo"));
        tbName.setCellValueFactory(new PropertyValueFactory<>("name"));
       tbFName.setCellValueFactory(new PropertyValueFactory<>("fatherName"));
        tbGender.setCellValueFactory(new PropertyValueFactory<>("gender"));
        action.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<PromotionViewModal, Boolean>, ObservableValue<Boolean>>() {

            @Override
            public ObservableValue<Boolean> call(TableColumn.CellDataFeatures<PromotionViewModal, Boolean> p) {
                return new SimpleBooleanProperty(p.getValue().isPromot());
            }
        });
        action.setCellFactory(new Callback<TableColumn<PromotionViewModal, Boolean>, TableCell<PromotionViewModal, Boolean>>() {

            @Override
            public TableCell<PromotionViewModal, Boolean> call(TableColumn<PromotionViewModal, Boolean> p) {
                return new CheckBoxCell(table);
            }

        });
      
    }    
    //Define the button cell

    @FXML
    private void refresh(ActionEvent event) throws SQLException, ClassNotFoundException {
      
    
    }

    private class CheckBoxCell extends TableCell<PromotionViewModal, Boolean> {

        final JFXCheckBox cellCheckBox = new JFXCheckBox();

        CheckBoxCell(final TableView tblView) {

            cellCheckBox.setOnAction(new EventHandler<ActionEvent>() {

                @Override
                public void handle(ActionEvent t) {
                    int present = 0;
                    int selectdIndex = getTableRow().getIndex();
                    if (table.getItems().get(selectdIndex).isPromot()) {

                        table.getItems().get(selectdIndex).setPromot(false);
                    } else {
                        table.getItems().get(selectdIndex).setPromot(true);

                    }
                  
                }
            });
        }

        //Display button if the row is not empty
        @Override
        protected void updateItem(Boolean t, boolean empty) {
            super.updateItem(t, empty);
            if (!empty) {
                cellCheckBox.setPrefSize(28, 20);
                setGraphic(cellCheckBox);
                if (t.booleanValue()) {
                    cellCheckBox.setSelected(true);
                } else {
                    cellCheckBox.setSelected(false);
                }
            } else {
                setGraphic(null);
            }
        }
    }

    @FXML
    private void UpdateHandler(ActionEvent event) throws ClassNotFoundException, SQLException {
        int classPrimary=0,sectionPrimary=0;
        connection = DatabaseConnection.getConnection();
        
        if(checkBoxAlumi.isSelected()){
            for (PromotionViewModal promot : table.getItems()) {

                if (promot.isPromot()) {

                   
                   
                    sql = "update student set section_id=? ,class_id=?, ALUMI_STATUS=? where reg_id=?";
                    statement = connection.prepareStatement(sql);
                    statement.setString(1, null);
                    statement.setString(2,null );
                    statement.setInt(3, 1);
                    statement.setInt(4, promot.getRegNo());
                    statement.execute();

                }

            }
        
         showNotifications("Students Alumi","Students alumination done sucessfully ");
        }else{
            for (PromotionViewModal promot : table.getItems()) {

                if (promot.isPromot()) {

                    sql = "select id from class where name=?";
                    statement = connection.prepareStatement(sql);
                    statement.setString(1, comboPromotClass.getValue());
                    resultSet = statement.executeQuery();
                    if (resultSet.next()) {

                        classPrimary = resultSet.getInt("id");

                    }
                    sql = "select id from sections where name=?";
                    statement = connection.prepareStatement(sql);
                    statement.setString(1, comboPromotSection.getValue());
                    resultSet = statement.executeQuery();
                    if (resultSet.next()) {

                        sectionPrimary = resultSet.getInt("id");

                    }
                    sql = "update student set section_id=? ,class_id=? where reg_id=?";
                    statement = connection.prepareStatement(sql);
                    statement.setInt(1, sectionPrimary);
                    statement.setInt(2, classPrimary);
                    statement.setInt(3, promot.getRegNo());
                    statement.execute();

                }

            }
            showNotifications("Students Promotion","Students promotion done sucessfully ");
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
    private void addViewButtonToTable(TableColumn column, String name) {
       final JFXCheckBox checkbox = new JFXCheckBox (name);
        Callback<TableColumn<StudentLeaveModal, Boolean>, TableCell<StudentLeaveModal, Boolean>> cellFactory = new Callback<TableColumn<StudentLeaveModal, Boolean>, TableCell<StudentLeaveModal, Boolean>>() {
            @Override
            public TableCell<StudentLeaveModal, Boolean> call(final TableColumn<StudentLeaveModal, Boolean> param) {
                final TableCell<StudentLeaveModal, Boolean> cell = new TableCell<StudentLeaveModal, Boolean>() {

                  

                   

                    @Override
                    public void updateItem(Boolean item, boolean empty) {
                        super.updateItem(item, empty);
                        //setGraphic(btn);
                        if (empty) {
                            setGraphic(null);
                        } else {
                            setGraphic(checkbox);
                            if (item.booleanValue()) {
                                checkbox .setSelected(true);
                                
                            } else {
                                checkbox .setSelected(false);
                                
                            }
                        }
                    }
                };
                return cell;
            }
            
        };
      
        
        column.setCellFactory(cellFactory);

    }
    private void loadsExistingClass() throws SQLException, ClassNotFoundException{
        connection = DatabaseConnection.getConnection();
        sql = "select * from class ";
        statement = connection.prepareStatement(sql);
        
        resultSet = statement.executeQuery();
       
        while (resultSet.next()) {

           comboExistingClass.getItems().add(resultSet.getString("name"));

        }
   
    }
    private void loadsPromotClass() throws SQLException, ClassNotFoundException {
        connection = DatabaseConnection.getConnection();
        sql = "select * from class ";
        statement = connection.prepareStatement(sql);
        resultSet = statement.executeQuery();
        comboPromotClass.getItems().clear();
        while (resultSet.next()) {

             comboPromotClass.getItems().add(resultSet.getString("name"));

        }
   
   

    }
    private void loadsExistingSections() throws SQLException, ClassNotFoundException {
        
        
         comboExistingClass.valueProperty().addListener((observable, oldValue, newValue) -> {
          int classPrimaryKey=0;
               
            try {
               // loadsExistingSections();
                connection = DatabaseConnection.getConnection();
                sql = "select * from class where name=?";
                statement = connection.prepareStatement(sql);
                statement.setString(1, newValue);
                resultSet = statement.executeQuery();
                if (resultSet.next()) {
                    
                    classPrimaryKey= resultSet.getInt("id");
                    
                }
                sql = "select * from sections where class_id=?";
                statement = connection.prepareStatement(sql);
                statement.setInt(1, classPrimaryKey);
                resultSet = statement.executeQuery();
                existingSectionName.clear();
              
                while (resultSet.next()) {

                   existingSectionName.add(resultSet.getString("name"));

                }
            } catch (SQLException | ClassNotFoundException ex) {
                Logger.getLogger(StudentPromotViewController.class.getName()).log(Level.SEVERE, null, ex);
            }
          
         });
        comboExistingSection.getItems().clear();
          comboExistingSection.setItems( existingSectionName);
    }
    private void loadsPromotSections() throws SQLException, ClassNotFoundException {

        comboPromotClass.valueProperty().addListener((observable, oldValue, newValue) -> {
            int classPrimaryKey = 0;

            try {
                connection = DatabaseConnection.getConnection();
                sql = "select * from class where name=?";
                statement = connection.prepareStatement(sql);
                statement.setString(1, newValue);
                resultSet = statement.executeQuery();
                if (resultSet.next()) {

                    classPrimaryKey = resultSet.getInt("id");

                }
                sql = "select * from sections where class_id=?";
                statement = connection.prepareStatement(sql);
                statement.setInt(1, classPrimaryKey);
                resultSet = statement.executeQuery();
                comboPromotSection.getItems().clear();
                while (resultSet.next()) {

                    comboPromotSection.getItems().add(resultSet.getString("name"));

                }
            } catch (SQLException | ClassNotFoundException ex) {
                Logger.getLogger(StudentPromotViewController.class.getName()).log(Level.SEVERE, null, ex);
            }

        });

    }
  private void  loadingStudentIntoTable(){
     
      comboExistingSection.valueProperty().addListener((observable, oldValue, newValue) -> {
          try {
              int sectionPrimarykey = 0;
              connection = DatabaseConnection.getConnection();
               sql="select id from sections where name =? ";
               statement = connection.prepareStatement(sql);
               statement.setString(1, newValue);
              
               resultSet=statement.executeQuery();
               if(resultSet.next()){
               
               sectionPrimarykey=resultSet.getInt("id");
               
               }
               
               sql = "select * from student where section_id=? and leave_status=?";
               statement = connection.prepareStatement(sql);
              statement.setInt(1,  sectionPrimarykey);
               statement.setInt(2, 0);
              resultSet = statement.executeQuery();
              table.getItems().clear();
              while (resultSet.next()) {

                  table.getItems().add(new PromotionViewModal(resultSet.getInt("reg_id"),resultSet.getString("name"),resultSet.getString("father_name"),resultSet.getString("gender"),true));
                 
              }
          } catch (ClassNotFoundException | SQLException ex) {
              Logger.getLogger(StudentPromotViewController.class.getName()).log(Level.SEVERE, null, ex);
          }
        
      
      
      
      
      });
  
  
  }
    
}
