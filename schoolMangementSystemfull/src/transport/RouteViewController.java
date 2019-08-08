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
import student.Registration.StudentModel;


/**
 * FXML Controller class
 *
 * @author khan
 */
public class RouteViewController implements Initializable {
    @FXML
    private TableView<RouteViewModal> table;
    @FXML
    private TableColumn<RouteViewModal, Integer> tbRouteID;
    @FXML
    private TableColumn<RouteViewModal, String> tbName;
    @FXML
    private TableColumn<RouteViewModal, String> tbFrom;
    @FXML
    private TableColumn<RouteViewModal, String> tbTo;
    @FXML
    private TableColumn<RouteViewModal, Void> tbUpdateRoute;
    @FXML
    private TableColumn<RouteViewModal, Void> tbDeleteRoute;
    @FXML
    private TextField txfRouteName;
    @FXML
    private TextField txfFrom;
    @FXML
    private TextField txfTo;
   
    //************** for database connection***********
    Connection connection = null;
    PreparedStatement statement = null;
    ResultSet resultSet = null;
    String sql = null;
    @FXML
    private TextField searchBox;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        tbRouteID.setCellValueFactory(new PropertyValueFactory<>("RouteID"));
        tbName.setCellValueFactory(new PropertyValueFactory<>("Name"));
        tbFrom.setCellValueFactory(new PropertyValueFactory<>("From"));
        tbTo.setCellValueFactory(new PropertyValueFactory<>("To"));
        try {
            loadDataIntoTable();
            searchingHandler();
            addUpdateToTable(tbUpdateRoute,"Update");
            addDeleteToTable(tbDeleteRoute,"Delete");
            
        } catch (SQLException | ClassNotFoundException ex) {
            Logger.getLogger(RouteViewController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }    

    @FXML
    private void saveRoute(ActionEvent event) throws SQLException, ClassNotFoundException {
        
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmation Dialog");
        alert.setHeaderText(null);
        alert.setContentText("Are you soure to save route?");
        alert.initModality(Modality.APPLICATION_MODAL);
        Optional<ButtonType> action = alert.showAndWait();
        if (action.get() == ButtonType.OK) {
            connection = DatabaseConnection.getConnection();
            sql = "INSERT INTO routs VALUES(?,?,?,?)";
            statement = connection.prepareStatement(sql);
            statement.setString(1, null);
            statement.setString(2, txfRouteName.getText());
            statement.setString(3, txfFrom.getText());
            statement.setString(4, txfTo.getText());
            statement.execute();
            loadDataIntoTable();
            showNotifications("Route Addition","Route add sucessfully");
            clearFields();
            searchingHandler();
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
    private void clearFields(){
        txfRouteName.setText("");
        txfFrom.setText("");
        txfTo.setText("");
    
    }
    private ObservableList list = FXCollections.observableArrayList();

    private void loadDataIntoTable() throws SQLException, ClassNotFoundException {
        connection = DatabaseConnection.getConnection();
        sql = "SELECT * FROM routs";
        statement = connection.prepareStatement(sql);
        resultSet = statement.executeQuery();
        list.clear();
        while (resultSet.next()) {

            list.addAll(new RouteViewModal(resultSet.getInt("id"), resultSet.getString("name"), resultSet.getString("fromt"),resultSet.getString("tot")));

        }
        table.setItems(list);
    }
    static RouteViewModal date;

    private void addUpdateToTable(TableColumn column, String name) {

        Callback<TableColumn<RouteViewModal, Void>, TableCell<RouteViewModal, Void>> cellFactory = new Callback<TableColumn<RouteViewModal, Void>, TableCell<RouteViewModal, Void>>() {
            @Override
            public TableCell<RouteViewModal, Void> call(final TableColumn<RouteViewModal, Void> param) {
                final TableCell<RouteViewModal, Void> cell = new TableCell<RouteViewModal, Void>() {

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
                                root = FXMLLoader.load(getClass().getResource("routeUpdateView.fxml"));
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

        Callback<TableColumn<RouteViewModal, Void>, TableCell<RouteViewModal, Void>> cellFactory = new Callback<TableColumn<RouteViewModal, Void>, TableCell<RouteViewModal, Void>>() {
            @Override
            public TableCell<RouteViewModal, Void> call(final TableColumn<RouteViewModal, Void> param) {
                final TableCell<RouteViewModal, Void> cell = new TableCell<RouteViewModal, Void>() {

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
                                alert.setContentText("Are you soure to delete Expense Category?");
                                alert.initModality(Modality.APPLICATION_MODAL);
                                Optional<ButtonType> action = alert.showAndWait();
                                if (action.get() == ButtonType.OK) {
                                    connection = DatabaseConnection.getConnection();
                                    sql = "delete  from routs where id=? ";
                                    statement = connection.prepareStatement(sql);
                                    statement.setInt(1, date.getRouteID());
                                    statement.execute();
                                    showNotifications("Route Delation ", "Rout delete sucessfull");
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

    @FXML
    private void refresh(ActionEvent event) throws SQLException, ClassNotFoundException {
        loadDataIntoTable();
        searchingHandler();
    }
    private void searchingHandler() {
        // 1. Wrap the ObservableList in a FilteredList (initially display all data).
        FilteredList<RouteViewModal> filteredData = new FilteredList<>(table.getItems(), p -> true);

        // 2. Set the filter Predicate whenever the filter changes.
        searchBox.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredData.setPredicate((Predicate<? super RouteViewModal>) student -> {
                // If filter text is empty, display all persons.
                if (newValue == null || newValue.isEmpty()) {
                    return true;
                }

                // Compare first name and last name of every person with filter text.
                String lowerCaseFilter = newValue.toLowerCase();

                if (student.getFrom().toLowerCase().contains(lowerCaseFilter)) {
                    return true; // Filter matches ID.

                } else if (student.getName().toLowerCase().contains(lowerCaseFilter)) {
                    return true; // Filter matches name.
                }
                return false; // Does not match.
            });
        });

        // 3. Wrap the FilteredList in a SortedList. 
        SortedList<RouteViewModal> sortedData = new SortedList<>(filteredData);

        // 4. Bind the SortedList comparator to the TableView comparator.
        sortedData.comparatorProperty().bind(table.comparatorProperty());

        // 5. Add sorted (and filtered) data to the table.
        table.setItems(sortedData);

    }
}
