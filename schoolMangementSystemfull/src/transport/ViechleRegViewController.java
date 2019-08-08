/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package transport;

import com.jfoenix.controls.JFXButton;
import databaseconnection.DatabaseConnection;
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.function.Predicate;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
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
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.effect.BoxBlur;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Callback;
import javafx.util.Duration;
import org.controlsfx.control.Notifications;
import student.Registration.StudentController;

/**
 * FXML Controller class
 *
 * @author khan
 */
public class ViechleRegViewController implements Initializable {
    
    @FXML
    private TableView<ViechleRegViewModal> table;
    @FXML
    private TableColumn< ViechleRegViewModal, Integer> tbID;
    @FXML
    private TableColumn< ViechleRegViewModal, String> tbName;
    @FXML
    private TableColumn< ViechleRegViewModal, String> tbNo;
    @FXML
    private TableColumn< ViechleRegViewModal, Integer> tbCapacity;
    @FXML
    private TableColumn<ViechleRegViewModal, String> tbRouteName;
    @FXML
    private TableColumn< ViechleRegViewModal, Void> tbUpdate;
    @FXML
    private TableColumn< ViechleRegViewModal, Void> tbDelete;
  
    @FXML
    private TextField txfName;
    @FXML
    private TextField txfVichleNo;
    @FXML
    private TextField txfCapacity;
    @FXML
    private ComboBox<String> comboRouteName;
     //************** for database connection***********
    Connection connection = null;
    PreparedStatement statement = null;
    ResultSet resultSet = null;
    String sql = null;
    @FXML
    private TextField seachBox;
   

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        tbID.setCellValueFactory(new PropertyValueFactory<>("ID"));
        tbName.setCellValueFactory(new PropertyValueFactory<>("Name"));
        tbNo.setCellValueFactory(new PropertyValueFactory<>("No"));
        tbCapacity.setCellValueFactory(new PropertyValueFactory<>("Capacity"));
        tbRouteName.setCellValueFactory(new PropertyValueFactory<>("RouteName"));
        addUpdateToTable(tbUpdate,"Update");
        addDeleteToTable(tbDelete, "Delete");
        
        try {
            loadDataIntoTable();
            searchingHandler();
            loadRoutes();
            
        } catch (SQLException | ClassNotFoundException ex) {
            Logger.getLogger(ViechleRegViewController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }  
    private void searchingHandler() {
        // 1. Wrap the ObservableList in a FilteredList (initially display all data).
        FilteredList<ViechleRegViewModal> filteredData = new FilteredList<>(table.getItems(), p -> true);

        // 2. Set the filter Predicate whenever the filter changes.
        seachBox.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredData.setPredicate((Predicate<? super ViechleRegViewModal>) student -> {
                // If filter text is empty, display all persons.
                if (newValue == null || newValue.isEmpty()) {
                    return true;
                }

                // Compare first name and last name of every person with filter text.
                String lowerCaseFilter = newValue.toLowerCase();

                if (student.getName().toLowerCase().contains(lowerCaseFilter)) {
                    return true; // Filter matches ID.

                } else if (student.getNo().toLowerCase().contains(lowerCaseFilter)) {
                    return true; // Filter matches name.
                }
                return false; // Does not match.
            });
        });

        // 3. Wrap the FilteredList in a SortedList. 
        SortedList<ViechleRegViewModal> sortedData = new SortedList<>(filteredData);

        // 4. Bind the SortedList comparator to the TableView comparator.
        sortedData.comparatorProperty().bind(table.comparatorProperty());

        // 5. Add sorted (and filtered) data to the table.
        table.setItems(sortedData);

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

    private void clearFields() {
        txfName.setText("");
        txfVichleNo.setText("");
        txfCapacity.setText("");
        comboRouteName.setValue(null);

    }
    @FXML
    private void saveViechle(ActionEvent event) throws ClassNotFoundException, SQLException {
        int routeID=0;
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmation Dialog");
        alert.setHeaderText(null);
        alert.setContentText("Are you soure to save viechle record?");
        alert.initModality(Modality.APPLICATION_MODAL);
        Optional<ButtonType> action = alert.showAndWait();
        if (action.get() == ButtonType.OK) {
            connection = DatabaseConnection.getConnection();
            sql="select id from routs where name=?";
            statement=connection.prepareStatement(sql);
            statement.setString(1,comboRouteName.getValue());
            resultSet=statement.executeQuery();
            if(resultSet.next()){
            
              routeID=resultSet.getInt("id");
            }
            
            
            sql = "insert into vehicle values(?,?,?,?,?)";
            statement = connection.prepareStatement(sql);
            statement.setString(1, null);
            statement.setString(2, txfName.getText());
            statement.setString(3, txfVichleNo.getText());
            statement.setString(4, txfCapacity.getText());
            statement.setInt(5, routeID);
            statement.execute();
            showNotifications("Viechle Addition","Viechle add sucessfully");
            clearFields();
            loadDataIntoTable();
            searchingHandler();
    }

   
 }

    private ObservableList list = FXCollections.observableArrayList();

    private void loadDataIntoTable() throws SQLException, ClassNotFoundException {
        connection = DatabaseConnection.getConnection();
        sql = "SELECT * FROM routs,vehicle where routs.id= vehicle.route_id";
        statement = connection.prepareStatement(sql);
        resultSet = statement.executeQuery();
        list.clear();
        while (resultSet.next()) {

            list.addAll(new ViechleRegViewModal(resultSet.getInt("vehicle.id"),resultSet.getString("vehicle.name"),resultSet.getString("vehicle.viechle_no"),resultSet.getInt("vehicle.total_seats"),resultSet.getString("routs.name")));

        }
        table.setItems(list);
    }
    static ViechleRegViewModal date;

    private void addUpdateToTable(TableColumn column, String name) {

        Callback<TableColumn<ViechleRegViewModal, Void>, TableCell<ViechleRegViewModal, Void>> cellFactory = new Callback<TableColumn<ViechleRegViewModal, Void>, TableCell<ViechleRegViewModal, Void>>() {
            @Override
            public TableCell<ViechleRegViewModal, Void> call(final TableColumn<ViechleRegViewModal, Void> param) {
                final TableCell<ViechleRegViewModal, Void> cell = new TableCell<ViechleRegViewModal, Void>() {

                    JFXButton btn = new JFXButton(name);

                    {
                        btn.setStyle("-fx-background-color: #1d62d1;-fx-text-fill:white; ");
                        btn.setOnAction((ActionEvent event) -> {
                            final Node source = (Node) event.getSource();
                            final Stage primaryStage = (Stage) source.getScene().getWindow();
                            date = getTableView().getItems().get(getIndex());
                            Parent root;
                            try {
                                //  Stage stage=new Stage();
                                root = FXMLLoader.load(getClass().getResource("viechleRegUpdateView.fxml"));
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
                            } catch (IOException ex) {
                                Logger.getLogger(StudentController.class.getName()).log(Level.SEVERE, null, ex);
                            }

                        });
                    }

                    private void showNotifications(String title, String text) {

                        Notifications.create()
                                .title(title)
                                .text(text)
                                .hideAfter(Duration.seconds(1))
                                .darkStyle()
                                .hideCloseButton()
                                .position(Pos.TOP_LEFT)
                                .showConfirm();

                    }

                    @Override
                    public void updateItem(Void item, boolean empty) {
                        super.updateItem(item, empty);
                        //setGraphic(btn);
                        if (empty) {
                            setGraphic(null);
                        } else {
                            setGraphic(btn);
                        }
                    }
                };
                return cell;
            }
        };

        column.setCellFactory(cellFactory);

    }

    private void addDeleteToTable(TableColumn column, String name) {

        Callback<TableColumn<ViechleRegViewModal, Void>, TableCell<ViechleRegViewModal, Void>> cellFactory = new Callback<TableColumn<ViechleRegViewModal, Void>, TableCell<ViechleRegViewModal, Void>>() {
            @Override
            public TableCell<ViechleRegViewModal, Void> call(final TableColumn<ViechleRegViewModal, Void> param) {
                final TableCell<ViechleRegViewModal, Void> cell = new TableCell<ViechleRegViewModal, Void>() {

                    JFXButton btn = new JFXButton(name);

                    {
                        btn.setStyle("-fx-background-color: #e02418;-fx-text-fill:white; ");
                        btn.setOnAction((ActionEvent event) -> {
                            date = getTableView().getItems().get(getIndex());
                            final Node source = (Node) event.getSource();
                            final Stage primaryStage = (Stage) source.getScene().getWindow();
                            date = getTableView().getItems().get(getIndex());
                            Parent root;
                            try {
                                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                                alert.setTitle("Confirmation Dialog");
                                alert.setHeaderText(null);
                                alert.setContentText("Are you soure to delete viechle record?");
                                alert.initModality(Modality.APPLICATION_MODAL);
                                Optional<ButtonType> action = alert.showAndWait();
                                if (action.get() == ButtonType.OK) {
                                    connection = DatabaseConnection.getConnection();
                                    sql = "delete  from vehicle where id=? ";
                                    statement = connection.prepareStatement(sql);
                                    statement.setInt(1, date.getID());
                                    statement.execute();
                                    showNotifications("Viechle Record Deletion", "Viechle record delete sucessfull");
                                    loadDataIntoTable();
                                    searchingHandler();

                                }
                            } catch (Exception ex) {
                                Logger.getLogger(StudentController.class.getName()).log(Level.SEVERE, null, ex);
                            }

                        });
                    }

                    private void showNotifications(String title, String text) {

                        Notifications.create()
                                .title(title)
                                .text(text)
                                .hideAfter(Duration.seconds(1))
                                .darkStyle()
                                .hideCloseButton()
                                .position(Pos.TOP_LEFT)
                                .showConfirm();

                    }

                    @Override
                    public void updateItem(Void item, boolean empty) {
                        super.updateItem(item, empty);
                        //setGraphic(btn);
                        if (empty) {
                            setGraphic(null);
                        } else {
                            setGraphic(btn);
                        }
                    }
                };
                return cell;
            }
        };

        column.setCellFactory(cellFactory);

    }

private void loadRoutes() throws SQLException, ClassNotFoundException{

    connection = DatabaseConnection.getConnection();
    sql=" SELECT * FROM routs ";
    statement = connection.prepareStatement(sql);
    resultSet = statement.executeQuery();
    comboRouteName.getItems().clear();
    while (resultSet.next()) {
        comboRouteName.getItems().add(resultSet.getString("name"));
        
    }
}

    @FXML
    private void refresh(ActionEvent event) throws SQLException, ClassNotFoundException {
        loadDataIntoTable();
        searchingHandler();
        loadRoutes();
    }
}
