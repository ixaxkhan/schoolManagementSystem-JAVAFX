
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
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.util.Callback;
import javafx.util.Duration;
import org.controlsfx.control.Notifications;

/**
 * FXML Controller class
 *
 * @author khan
 */
public class EmployeeAttendenceViewController implements Initializable {
    @FXML
    private DatePicker txfDate;
    @FXML
    private JFXButton savebtn;
    @FXML
    private Label labTotal;
    @FXML
    private Label labPresent;
    @FXML
    private Label labAbsent;
    @FXML
    private JFXButton updatebtn;
    @FXML
    private TableView<EmployeeAttendenceVewModal> table;
    @FXML
    private TableColumn<EmployeeAttendenceVewModal, Integer> tbEmpID;
    @FXML
    private TableColumn<EmployeeAttendenceVewModal, String> tbEmpName;
    @FXML
    private TableColumn<EmployeeAttendenceVewModal , String> tbGender;
    @FXML
    private TableColumn<EmployeeAttendenceVewModal, String> tbAddress;
    @FXML
    private TableColumn<EmployeeAttendenceVewModal, String> tbPhone;
    @FXML
    private TableColumn<EmployeeAttendenceVewModal, Boolean> tbAction;
    //************** for database connection***********
    Connection connection = null;
    PreparedStatement statement = null;
    ResultSet resultSet = null;
    String sql = null;
   
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        updatebtn.setVisible(false);
        try {
            loadDataIntoTable();
            loadingOldAttendence();
        } catch (ClassNotFoundException | SQLException ex) {
            Logger.getLogger(EmployeeAttendenceViewController.class.getName()).log(Level.SEVERE, null, ex);
        }
        tbEmpID.setCellValueFactory(new PropertyValueFactory<>("EmpID"));
        tbEmpName.setCellValueFactory(new PropertyValueFactory<>("EmpName"));
        tbAddress.setCellValueFactory(new PropertyValueFactory<>("Address"));
        tbGender.setCellValueFactory(new PropertyValueFactory<>("Gender"));
        tbPhone.setCellValueFactory(new PropertyValueFactory<>("Phone"));
        tbAction.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<EmployeeAttendenceVewModal, Boolean>, ObservableValue<Boolean>>() {

            @Override
            public ObservableValue<Boolean> call(TableColumn.CellDataFeatures<EmployeeAttendenceVewModal, Boolean> p) {
                return new SimpleBooleanProperty(p.getValue().isPresent());
            }
        });
        tbAction.setCellFactory(new Callback<TableColumn<EmployeeAttendenceVewModal, Boolean>, TableCell<EmployeeAttendenceVewModal, Boolean>>() {

            @Override
            public TableCell<EmployeeAttendenceVewModal, Boolean> call(TableColumn<EmployeeAttendenceVewModal, Boolean> p) {
                return new EmployeeAttendenceViewController.CheckBoxCell(table);
            }

        });

        
       
    }  
    ObservableList<EmployeeAttendenceVewModal> oldAttendences = FXCollections.observableArrayList();
    
        private void loadingOldAttendence() {

        txfDate.valueProperty().addListener((observable, oldValue, newValue) -> {
            try {
                connection = DatabaseConnection.getConnection();
                
                sql="SELECT * FROM emp_attendence where date =?";
                statement = connection.prepareStatement(sql);
                statement.setDate(1, Date.valueOf(txfDate.getValue()));
                resultSet= statement.executeQuery();
                if(resultSet.next()){
                
                    sql = "SELECT * FROM emp_attendence where date =?";
                    statement = connection.prepareStatement(sql);
                    statement.setDate(1, Date.valueOf(txfDate.getValue()));
                    resultSet = statement.executeQuery();
                    oldAttendences.clear();
                    while (resultSet.next()) {

                        for (EmployeeAttendenceVewModal oldAttendence : table.getItems()) {
                            if (oldAttendence.getEmpID() == resultSet.getInt("employee_id")) {

                                if (resultSet.getInt("status") == 1) {

                                   oldAttendences.add(new EmployeeAttendenceVewModal(oldAttendence.getEmpID(),oldAttendence.getEmpName(),oldAttendence.getGender(),oldAttendence.getAddress(),oldAttendence.getPhone(),true));
                                } else {

                                    oldAttendences.add(new EmployeeAttendenceVewModal(oldAttendence.getEmpID(),oldAttendence.getEmpName(),oldAttendence.getGender(),oldAttendence.getAddress(),oldAttendence.getPhone(),false));
                               
                                }

                            }

                        }

                    }
                    
                table.setItems(oldAttendences);
                updatebtn.setVisible(true);
                savebtn.setVisible(false);
                }else{
                
                   savebtn.setVisible(true);
                   updatebtn.setVisible(false);
                    loadDataIntoTable() ;
                
                
                }
               
               
            } catch (ClassNotFoundException | SQLException ex) {
                Logger.getLogger(EmployeeAttendenceViewController.class.getName()).log(Level.SEVERE, null, ex);
            }

        });
        
    }
      ObservableList<EmployeeAttendenceVewModal> employees = FXCollections.observableArrayList();
   private void loadDataIntoTable() throws ClassNotFoundException, SQLException{
   
       connection = DatabaseConnection.getConnection();

       sql = "SELECT * FROM employee where leave_status =?";
       statement = connection.prepareStatement(sql);
       statement.setInt(1, 0);
       resultSet = statement.executeQuery();
       employees.clear();
       int i=0;
       while (resultSet.next()) {
           i++;
       labTotal.setText(""+i);
       employees.add(new EmployeeAttendenceVewModal(resultSet.getInt("reg_no"),resultSet.getString("name"),resultSet.getString("gender"),resultSet.getString("address"),resultSet.getString("phone"),false ) );
       }
    table.setItems(employees);
   }     
        
        
    

    @FXML
    private void saveAttendence(ActionEvent event) throws ClassNotFoundException, SQLException {
        connection = DatabaseConnection.getConnection();
        sql = "INSERT INTO `emp_attendence`(date, status, employee_id) VALUES (?,?,?)";
        statement = connection.prepareStatement(sql); 
        for (EmployeeAttendenceVewModal attendence : table.getItems()){
        
        
            statement.setDate(1, Date.valueOf(txfDate.getValue()));
            if(attendence.isPresent()){
            
            statement.setInt(2, 1);
            }else{
            
            statement.setInt(2, 0);
            
            }
            statement.setInt(3, attendence.getEmpID());
            statement.addBatch();
        }
       statement.executeBatch();
       showNotifications("Attendence Sheet","Attendence  save sucessfully");
        
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
    private void updateAttendence(ActionEvent event) throws ClassNotFoundException, SQLException {
        connection = DatabaseConnection.getConnection();
        
        sql="delete from `emp_attendence` where date=?";
        statement = connection.prepareStatement(sql);
        statement.setDate(1, Date.valueOf(txfDate.getValue()));
        statement.execute();
        sql = "INSERT INTO `emp_attendence`(date, status, employee_id) VALUES (?,?,?)";
        statement = connection.prepareStatement(sql);
        for (EmployeeAttendenceVewModal attendence : table.getItems()) {

            statement.setDate(1, Date.valueOf(txfDate.getValue()));
            if (attendence.isPresent()) {

                statement.setInt(2, 1);
            } else {

                statement.setInt(2, 0);

            }
            statement.setInt(3, attendence.getEmpID());
            statement.addBatch();
        }
        statement.executeBatch();
        showNotifications("Attendence Sheet", "Attendence sheet update sucessfully");
    }
    
    
      //Define the button cell
    private class CheckBoxCell extends TableCell<EmployeeAttendenceVewModal, Boolean> {

        final JFXCheckBox cellCheckBox = new JFXCheckBox();

        CheckBoxCell(final TableView tblView) {

            cellCheckBox.setOnAction(new EventHandler<ActionEvent>() {

                @Override
                public void handle(ActionEvent t) {
                    int present = 0;
                    int selectdIndex = getTableRow().getIndex();
                    if (table.getItems().get(selectdIndex).isPresent()) {

                        table.getItems().get(selectdIndex).setPresent(false);
                    } else {
                        table.getItems().get(selectdIndex).setPresent(true);

                    }
                    for (EmployeeAttendenceVewModal attendence : table.getItems()) {

                        if (attendence.isPresent()) {
                            ++present;

                        }

                    }
                    labPresent.setText("" + present);
                    present = 0;
                    int result = Integer.parseInt(labTotal.getText()) - Integer.parseInt(labPresent.getText());
                    labAbsent.setText("" + result);
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


    
}
