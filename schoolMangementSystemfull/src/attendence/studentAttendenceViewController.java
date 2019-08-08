/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package attendence;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXCheckBox;
import databaseconnection.DatabaseConnection;
import java.net.URL;
import java.sql.Connection;
import java.sql.Date;
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
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.util.Callback;
import javafx.util.Duration;
import org.controlsfx.control.Notifications;


import student.Registration.formController;

/**
 * FXML Controller class
 *
 * @author khan
 */
public class studentAttendenceViewController implements Initializable {
    @FXML
    private DatePicker txfDate;
    @FXML
    private ComboBox<String> comboClass;
    @FXML
    private ComboBox<String> comboSection;
    @FXML
    private Label labTotalStu;
    @FXML
    private Label labPresentStu;
    @FXML
    private Label labAbsenseStu;
    
    @FXML
    private TableView<StudentAttendenceViewModal> table;
    @FXML
    private TableColumn<StudentAttendenceViewModal, Integer> tbRegNo;
    @FXML
    private TableColumn<StudentAttendenceViewModal, String> tbStuName;
    @FXML
    private TableColumn<StudentAttendenceViewModal, String> tbFName;
    @FXML
    private TableColumn<StudentAttendenceViewModal, String> tbGender;
    @FXML
    private TableColumn<StudentAttendenceViewModal,Boolean> tbAction;

    //************** for database connection***********
    Connection connection = null;
    PreparedStatement statement = null;
    ResultSet resultSet = null;
    String sql = null;
   

    // **************for class and section observable list*******************
    ObservableList<String> classesNames = FXCollections.observableArrayList();
    ObservableList<String> sectionName = FXCollections.observableArrayList();
    ObservableList<Integer> studentRegNo = FXCollections.observableArrayList();
    ObservableList<StudentAttendenceViewModal> oldAttendence = FXCollections.observableArrayList();
    ObservableList<StudentAttendenceViewModal> tableDate = FXCollections.observableArrayList();
    @FXML
    private JFXButton savebtn;
    @FXML
    private JFXButton updatebtn;
    @Override
    public void initialize(URL url, ResourceBundle rb) {
         updatebtn.setVisible(false);
        try {
            showClasses();
            loadSectionsName();
            loadSectionsName();
            loadingStudentIntoTable();
            loadStudentDateWise();
        } catch (ClassNotFoundException | SQLException ex) {
            Logger.getLogger(studentAttendenceViewController.class.getName()).log(Level.SEVERE, null, ex);
        }
       
        tbRegNo.setCellValueFactory(new PropertyValueFactory<>("RegNo"));
        tbStuName.setCellValueFactory(new PropertyValueFactory<>("StuName"));
        tbFName.setCellValueFactory(new PropertyValueFactory<>("FName"));
        tbGender.setCellValueFactory(new PropertyValueFactory<>("gender"));
        tbAction.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<StudentAttendenceViewModal, Boolean>, ObservableValue<Boolean>>() {

            @Override
            public ObservableValue<Boolean> call(TableColumn.CellDataFeatures<StudentAttendenceViewModal, Boolean> p) {
                return new SimpleBooleanProperty(p.getValue().isPresent());
            }
        }); 
         tbAction.setCellFactory(new Callback<TableColumn<StudentAttendenceViewModal, Boolean>, TableCell<StudentAttendenceViewModal, Boolean>>() {

            @Override
            public TableCell<StudentAttendenceViewModal, Boolean> call(TableColumn<StudentAttendenceViewModal, Boolean> p) {
                return new CheckBoxCell(table);
            }

        });
        
    }    

    @FXML
    private void saveAttendence(ActionEvent event) throws SQLException, ClassNotFoundException {
        
          int sectionPrimarykey = 0, classPrimryKey=0;
        connection = DatabaseConnection.getConnection();
        sql = "select id from sections where name =? ";
        statement = connection.prepareStatement(sql);
        statement.setString(1, comboSection.getValue());

        resultSet = statement.executeQuery();
        if (resultSet.next()) {

            sectionPrimarykey = resultSet.getInt("id");

        }
        sql = "select id from class where name= ?";
        statement = connection.prepareStatement(sql);
        statement.setString(1, comboClass.getValue());
        resultSet = statement.executeQuery();

        if (resultSet.next()) {

            classPrimryKey = resultSet.getInt("id");

        }
        
        sql="insert into attendence (status,date,student_id,class_id,section_id) values(?,?,?,?,?)";
        statement = connection.prepareStatement(sql); 
        for(StudentAttendenceViewModal attendence : table.getItems()){
            if (attendence.isPresent()) {
                statement.setInt(1, 1);
            } else {

                statement.setInt(1, 0);

            }
            statement.setDate(2,Date.valueOf(txfDate.getValue()));
            statement.setInt(3, attendence.getRegNo());
            statement.setInt(4,  classPrimryKey );
            statement.setInt(5,  sectionPrimarykey);
            statement.addBatch();
            
        
        } 
       statement.executeBatch();
       showNotifications("Attendence Sheet","Attendence record save sucessfully") ;
    }
    
    @FXML
    private void updateAttendence(ActionEvent event) throws ClassNotFoundException, SQLException {
        int sectionPrimarykey = 0, classPrimryKey = 0;
        connection = DatabaseConnection.getConnection();
        sql = "select id from sections where name =? ";
        statement = connection.prepareStatement(sql);
        statement.setString(1, comboSection.getValue());

        resultSet = statement.executeQuery();
        if (resultSet.next()) {

            sectionPrimarykey = resultSet.getInt("id");

        }
        sql = "select id from class where name= ?";
        statement = connection.prepareStatement(sql);
        statement.setString(1, comboClass.getValue());
        resultSet = statement.executeQuery();

        if (resultSet.next()) {

            classPrimryKey = resultSet.getInt("id");

        }
        sql="delete from attendence where date=? and class_id=? and section_id=? ";
        statement = connection.prepareStatement(sql);
        statement.setDate(1, Date.valueOf(txfDate.getValue()));
        statement.setInt(2, classPrimryKey);
        statement.setInt(3,  sectionPrimarykey);
        statement.execute();

        sql = "insert into attendence (status,date,student_id,class_id,section_id) values(?,?,?,?,?)";
        statement = connection.prepareStatement(sql);
        for (StudentAttendenceViewModal attendence : table.getItems()) {
            if (attendence.isPresent()) {
                statement.setInt(1, 1);
            } else {

                statement.setInt(1, 0);

            }
            statement.setDate(2, Date.valueOf(txfDate.getValue()));
            statement.setInt(3, attendence.getRegNo());
            statement.setInt(4, classPrimryKey);
            statement.setInt(5, sectionPrimarykey);
            statement.addBatch();

        }
        statement.executeBatch();
        showNotifications("Attendence Sheet", "Attendence record update sucessfully ");
        
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
    //for loading classes name into combobox
    private void showClasses() throws ClassNotFoundException, SQLException {

        connection = DatabaseConnection.getConnection();
        sql = "select * from class";
        statement = connection.prepareStatement(sql);

        resultSet = statement.executeQuery();
        classesNames.clear();
        while (resultSet.next()) {
            classesNames.add(resultSet.getString("name"));

        }
        comboClass.getItems().clear();
        comboClass.setItems(classesNames);

    }

    // this fuction is used to load respective sections of class
    private void loadSectionsName() {

        comboClass.valueProperty().addListener((observable, oldValue, newValue) -> {
            String classPrimryKey = null;
            try {
                connection = DatabaseConnection.getConnection();
                sql = "select id from class where name= ?";
                statement = connection.prepareStatement(sql);
                statement.setString(1, newValue);
                resultSet = statement.executeQuery();

                if (resultSet.next()) {

                    classPrimryKey = resultSet.getString("id");

                }
                sql = "select * from sections where class_id=?";
                statement = connection.prepareStatement(sql);
                statement.setString(1, classPrimryKey);
                resultSet = statement.executeQuery();
                sectionName.clear();
                while (resultSet.next()) {

                    sectionName.add(resultSet.getString("name"));

                }

            } catch (ClassNotFoundException | SQLException ex) {
                Logger.getLogger(formController.class.getName()).log(Level.SEVERE, null, ex);
            }

        });

        comboSection.getItems().clear();
        comboSection.setItems(sectionName);
    }

    
    
    private void loadingStudentIntoTable() {

        comboSection.valueProperty().addListener((observable, oldValue, newValue) -> {
            try {
                int sectionPrimarykey = 0;
                connection = DatabaseConnection.getConnection();
                sql = "select id from sections where name =? ";
                statement = connection.prepareStatement(sql);
                statement.setString(1, newValue);

                resultSet = statement.executeQuery();
                if (resultSet.next()) {

                    sectionPrimarykey = resultSet.getInt("id");

                }

                sql = "select * from student where section_id=? and leave_status =? and ALUMI_STATUS=?";
                statement = connection.prepareStatement(sql);
                statement.setInt(1, sectionPrimarykey);
                statement.setInt(2, 0);
                statement.setInt(3, 0);
                resultSet = statement.executeQuery();
                 tableDate .clear();
                 int i=1;
                while (resultSet.next()) {
                labTotalStu.setText(""+i++);
                     tableDate .add(new StudentAttendenceViewModal(resultSet.getInt("reg_id"), resultSet.getString("name"), resultSet.getString("father_name"), resultSet.getString("gender"), false));

                }
            } catch (ClassNotFoundException | SQLException ex) {
                Logger.getLogger(studentAttendenceViewController.class.getName()).log(Level.SEVERE, null, ex);
            }

        });
      table.getItems().clear();
      table.setItems(tableDate);
    }
    //Define the button cell

    private class CheckBoxCell extends TableCell<StudentAttendenceViewModal, Boolean> {

        final JFXCheckBox cellCheckBox = new JFXCheckBox();

        CheckBoxCell(final TableView tblView) {

            cellCheckBox.setOnAction(new EventHandler<ActionEvent>() {

                @Override
                public void handle(ActionEvent t) {
                    int present=0;
                    int selectdIndex = getTableRow().getIndex();
                   if(table.getItems().get(selectdIndex).isPresent()){
                   
                   table.getItems().get(selectdIndex).setPresent(false);
                   }else{
                       table.getItems().get(selectdIndex).setPresent(true);
                   
                   }
                   for (StudentAttendenceViewModal attendence : table.getItems()){
                      
                       if(attendence.isPresent()){
                         ++present ;
                       
                       }
                   
                   }
                    labPresentStu.setText(""+ present);
                   present=0;
                   int result=Integer.parseInt(labTotalStu.getText()) - Integer.parseInt( labPresentStu.getText());
                  labAbsenseStu.setText(""+ result );
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
    
  

    
    
    private void loadStudentDateWise(){
    
    txfDate.valueProperty().addListener((observable, oldValue, newValue) -> {
        int classPrimaryKey =0,sectionPrimaryKey=0;
        
        try {
            
            connection = DatabaseConnection.getConnection();
            sql = "select * from class where name=?";
            statement = connection.prepareStatement(sql);
            statement.setString(1, comboClass.getValue());
            resultSet = statement.executeQuery();

            if (resultSet.next()) {
                classPrimaryKey = resultSet.getInt("id");

            }
            sql = "select * from sections where name=?";
            statement = connection.prepareStatement(sql);
            statement.setString(1, comboSection.getValue());
            resultSet = statement.executeQuery();

            if (resultSet.next()) {
                sectionPrimaryKey = resultSet.getInt("id");

            }
            sql="select * from attendence where date=? and section_id=? and class_id=?";
            statement = connection.prepareStatement(sql);
            statement.setDate(1, Date.valueOf(txfDate.getValue()));
            statement.setInt(2,  sectionPrimaryKey);
            statement.setInt(3,  classPrimaryKey);
            resultSet = statement.executeQuery();
            oldAttendence.clear();
            int present=0,absent=0,total=0;
            if(resultSet.next()){
                sql = "select * from attendence where date=? and section_id=? and class_id=?";
                statement = connection.prepareStatement(sql);
                statement.setDate(1, Date.valueOf(txfDate.getValue()));
                statement.setInt(2, sectionPrimaryKey);
                statement.setInt(3, classPrimaryKey);
                resultSet = statement.executeQuery();
                
                while (resultSet.next()) {
                    ++total;
                    for (StudentAttendenceViewModal attendence : table.getItems()) {
                       
                        if (attendence.getRegNo() == resultSet.getInt("student_id")) {
                            if (resultSet.getInt("status") == 1) {
                                ++present;
                                oldAttendence.add(new StudentAttendenceViewModal(attendence.getRegNo(), attendence.getStuName(), attendence.getFName(), attendence.getGender(), true));
                            } else {
                                ++absent;
                                oldAttendence.add(new StudentAttendenceViewModal(attendence.getRegNo(), attendence.getStuName(), attendence.getFName(), attendence.getGender(), false));
                            }

                        }

                    }

                }
                table.getItems().clear();
                table.setItems(oldAttendence);
                savebtn.setVisible(false);
                updatebtn.setVisible(true);
                labTotalStu.setText("" + total);
                labPresentStu.setText("" + present);
                labAbsenseStu.setText("" + absent);
                total=0;
                present=0;
                absent=0;
                
            }else{
                
                 savebtn.setVisible(true);
                 updatebtn.setVisible(false);
                //loadingStudentIntoTable();
                 
                 try {
                    int sectionPrimarykey = 0;
                    connection = DatabaseConnection.getConnection();
                    sql = "select id from sections where name =? ";
                    statement = connection.prepareStatement(sql);
                    statement.setString(1, comboSection.getValue());

                    resultSet = statement.executeQuery();
                    if (resultSet.next()) {

                        sectionPrimarykey = resultSet.getInt("id");

                    }

                    sql = "select * from student where section_id=? and leave_status =? and ALUMI_STATUS=?";
                    statement = connection.prepareStatement(sql);
                    statement.setInt(1, sectionPrimarykey);
                    statement.setInt(2, 0);
                    statement.setInt(3, 0);
                    resultSet = statement.executeQuery();
                    tableDate.clear();
                    int i = 1;
                    while (resultSet.next()) {
                        labTotalStu.setText("" + i++);
                        tableDate.add(new StudentAttendenceViewModal(resultSet.getInt("reg_id"), resultSet.getString("name"), resultSet.getString("father_name"), resultSet.getString("gender"), false));

                    }
                } catch (ClassNotFoundException | SQLException ex) {
                    Logger.getLogger(studentAttendenceViewController.class.getName()).log(Level.SEVERE, null, ex);
                }

           
           // table.getItems().clear();
            table.setItems(tableDate);
            
            } 
            
        } catch (SQLException | ClassNotFoundException ex) {
            Logger.getLogger(studentAttendenceViewController.class.getName()).log(Level.SEVERE, null, ex);
        }
      
    
    });
   
    
    }

    
}
