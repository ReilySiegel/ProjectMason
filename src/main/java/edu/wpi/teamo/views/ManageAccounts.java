package edu.wpi.teamo.views;

import com.jfoenix.controls.*;
import com.jfoenix.controls.datamodels.treetable.RecursiveTreeObject;
import edu.wpi.teamo.App;
import edu.wpi.teamo.Session;
import edu.wpi.teamo.database.account.Account;
import edu.wpi.teamo.database.request.FoodRequest;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.event.EventType;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.control.cell.CheckBoxTreeTableCell;
import javafx.scene.control.cell.ComboBoxTreeCell;
import javafx.scene.control.cell.ComboBoxTreeTableCell;
import javafx.scene.control.cell.TextFieldTreeTableCell;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.util.Callback;

import javax.swing.*;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class ManageAccounts implements Initializable {

    @FXML
    JFXTreeTableView<Account> acctTree;

    @FXML
    JFXButton makeAccountButton;

    @FXML
    JFXButton deleteAccountButton;

    @FXML
    JFXButton promoteButton;

    @FXML
    StackPane stackPane;

    private boolean correctPassword;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        acctTree.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);

        correctPassword = false;

        makeAccountButton.setOnAction(event -> createAccount());
        deleteAccountButton.setOnAction(event -> deleteAccount());
        promoteButton.setOnAction(event -> handleVerify());
        updateAccountTable();
    }


    @FXML
    private void updateAccountTable() {

        JFXTreeTableColumn<Account, String> acctUsers = new JFXTreeTableColumn<>("Username");
        acctUsers.setPrefWidth(200);
        acctUsers.setCellValueFactory(new Callback<TreeTableColumn.CellDataFeatures<Account, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TreeTableColumn.CellDataFeatures<Account, String> param) {
                StringProperty strProp = new SimpleStringProperty(param.getValue().getValue().getUsername());
                return strProp;
            }
        });

        JFXTreeTableColumn<Account, String> acctFirst = new JFXTreeTableColumn<>("First Name");
        acctFirst.setPrefWidth(200);
        acctFirst.setCellValueFactory(new Callback<TreeTableColumn.CellDataFeatures<Account, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TreeTableColumn.CellDataFeatures<Account, String> param) {
                StringProperty strProp = new SimpleStringProperty(param.getValue().getValue().getFirstName());
                return strProp;
            }
        });
        JFXTreeTableColumn<Account, String> acctLast = new JFXTreeTableColumn<>("Last Name");
        acctLast.setPrefWidth(150);
        acctLast.setCellValueFactory(new Callback<TreeTableColumn.CellDataFeatures<Account, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TreeTableColumn.CellDataFeatures<Account, String> param) {
                StringProperty strProp = new SimpleStringProperty(param.getValue().getValue().getLastName());
                return strProp;
            }
        });

        JFXTreeTableColumn<Account, String> acctRole = new JFXTreeTableColumn<>("Role");
        acctRole.setPrefWidth(200);
        acctRole.setCellValueFactory(new Callback<TreeTableColumn.CellDataFeatures<Account, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TreeTableColumn.CellDataFeatures<Account, String> param) {
                StringProperty strProp = new SimpleStringProperty(param.getValue().getValue().getRole());
                return strProp;
            }
        });


        JFXTreeTableColumn<Account, Boolean> acctAdmin = new JFXTreeTableColumn<>("Admin");
        acctAdmin.setPrefWidth(150);
        //acctAdmin.setCellFactory(CheckBoxTreeTableCell.forTreeTableColumn(acctAdmin));
            acctAdmin.setCellValueFactory(param -> {
                if(param.getValue().getValue().isAdmin()) {
                    return new SimpleBooleanProperty(true);
                } else {
                    return new SimpleBooleanProperty(false);
                }
            });




        if(Session.getAccount().isAdmin()) {
            acctUsers.setEditable(true);
            acctFirst.setEditable(true);
            acctLast.setEditable(true);
            acctRole.setEditable(true);
            acctAdmin.setEditable(true);

            //Enable editing username via doubleclick and enter
            acctUsers.setCellFactory(TextFieldTreeTableCell.forTreeTableColumn());
            acctUsers.setOnEditCommit(new EventHandler<TreeTableColumn.CellEditEvent<Account, String>>() {
                @Override
                public void handle(TreeTableColumn.CellEditEvent<Account, String> event) {
                    TreeItem<Account> currentAccount = acctTree.getTreeItem(event.getTreeTablePosition().getRow());
                    try {
                        String userName = event.getNewValue();
                        currentAccount.getValue().setUsername(userName);
                    } catch (SQLException throwables) {
                        throwables.printStackTrace();
                    }
                }
            });

            //Enable editing First Name via doubleclick and enter
            acctFirst.setCellFactory(TextFieldTreeTableCell.forTreeTableColumn());
            acctFirst.setOnEditCommit(new EventHandler<TreeTableColumn.CellEditEvent<Account, String>>() {
                @Override
                public void handle(TreeTableColumn.CellEditEvent<Account, String> event) {
                    TreeItem<Account> currentAccount = acctTree.getTreeItem(event.getTreeTablePosition().getRow());
                    try {
                        String firstName = event.getNewValue();
                        currentAccount.getValue().setFirstName(firstName);
                    } catch (SQLException throwables) {
                        throwables.printStackTrace();
                    }
                }
            });

            //Enable editing Last Name via doubleclick and enter
            acctLast.setCellFactory(TextFieldTreeTableCell.forTreeTableColumn());
            acctLast.setOnEditCommit(new EventHandler<TreeTableColumn.CellEditEvent<Account, String>>() {
                @Override
                public void handle(TreeTableColumn.CellEditEvent<Account, String> event) {
                    TreeItem<Account> currentAccount = acctTree.getTreeItem(event.getTreeTablePosition().getRow());
                    try {
                        String LastName = event.getNewValue();
                        currentAccount.getValue().setLastName(LastName);
                    } catch (SQLException throwables) {
                        throwables.printStackTrace();
                    }
                }
            });

            //Enable editing role via doubleclick and enter
            acctRole.setCellFactory(TextFieldTreeTableCell.forTreeTableColumn());
            acctRole.setOnEditCommit(new EventHandler<TreeTableColumn.CellEditEvent<Account, String>>() {
                @Override
                public void handle(TreeTableColumn.CellEditEvent<Account, String> event) {
                    TreeItem<Account> currentAccount = acctTree.getTreeItem(event.getTreeTablePosition().getRow());
                    try {
                        String role = event.getNewValue();
                        currentAccount.getValue().setRole(role);
                    } catch (SQLException throwables) {
                        throwables.printStackTrace();
                    }
                }
            });

        }

        ObservableList<Account> accountList = FXCollections.observableArrayList();
        try {
            Stream<Account> acctStream = Account.getAll();
            acctStream.forEach(m -> accountList.add(m));
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        final TreeItem<Account> root = new RecursiveTreeItem<Account>(accountList, RecursiveTreeObject::getChildren);
        acctTree.getColumns().setAll(acctUsers, acctFirst, acctLast, acctRole, acctAdmin);
        acctTree.setRoot(root);
        acctTree.setShowRoot(false);

        acctTree.setEditable(true);


    }

    private void createAccount(){
        try{
            new Account("default", "default", false,
                    "default", "default", "default").update();
        }
        catch(SQLException e){
            e.printStackTrace();
        }


        this.updateAccountTable();
    }


    private void deleteAccount(){
        try{
            acctTree.getSelectionModel().getSelectedItem().getValue().delete();
            this.updateAccountTable();
        }
        catch (SQLException e){
            e.printStackTrace();
        }

    }

    private void updateAccess() {
        if (Session.getAccount().isAdmin() && correctPassword) {
            try {
                acctTree.getSelectionModel().getSelectedItem().getValue().setAdmin(!acctTree.getSelectionModel().getSelectedItem().getValue().isAdmin());
                updateAccountTable();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        else{
            System.out.println("You aren't an admin, how did you get here?");
        }
    }

    private void handleVerify() {

        if(!correctPassword) {
            JFXDialogLayout content = new JFXDialogLayout();
            content.setHeading(new Text(App.resourceBundle.getString("key.enter_password")));
            JFXPasswordField passwordField = new JFXPasswordField();

            VBox vb = new VBox();
            vb.setPadding(new Insets(10, 0, 0, 10));
            vb.setSpacing(10);
            final Label message = new Label("");
            vb.getChildren().setAll(passwordField, message);

            content.setBody(vb);
            JFXDialog passwordWindow = new JFXDialog(stackPane, content, JFXDialog.DialogTransition.TOP);


            JFXButton closeButton = new JFXButton(App.resourceBundle.getString("key.close"));
            closeButton.setStyle("-fx-background-color: #F40F19; -fx-text-fill: #fff");
            closeButton.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent event) {
                    passwordWindow.close();
                }
            });

            JFXButton submitButton = new JFXButton(App.resourceBundle.getString("key.submit"));
            submitButton.setStyle("-fx-background-color: rgb(62, 234, 105); -fx-text-fill: #fff");
            submitButton.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent event) {
                    if (passwordField.getText().equals(Session.getAccount().getPasswordHash())) {
                        message.setText("Thank you");
                        message.setTextFill(Color.rgb(21, 117, 84));
                        correctPassword = true;
                        updateAccess();
                        passwordWindow.close();
                    } else {
                        message.setText("Password Incorrect");
                        message.setTextFill(Color.rgb(210, 39, 30));
                    }
                }
            });

            content.setActions(closeButton, submitButton);
            passwordWindow.show();
        }
        else{
            updateAccess();
        }
    }
}
