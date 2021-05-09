package edu.wpi.teamo.views.accountmanager;

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
import java.util.ArrayList;
import java.util.List;
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

    @FXML
    JFXTextField usernameBox;

    @FXML
    JFXTextField passwordBox;

    @FXML
    JFXTextField firstNameBox;

    @FXML
    JFXTextField lastNameBox;

    @FXML
    JFXComboBox<String> roleBox;

    private boolean correctPassword;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        acctTree.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);

        correctPassword = false;


        List<String> roles = new ArrayList<>();
        roles.add("employee");
        roles.add("patient");
        roleBox.setItems(FXCollections.observableArrayList(roles));


        makeAccountButton.setOnAction(event -> createAccount());
        deleteAccountButton.setOnAction(event -> deleteAccount());
        promoteButton.setOnAction(event -> updateAccess());
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

        JFXTreeTableColumn<Account, String> acctCleared = new JFXTreeTableColumn<>("Entry Status");
        acctCleared.setPrefWidth(100);
        acctCleared.setCellValueFactory(new Callback<TreeTableColumn.CellDataFeatures<Account, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TreeTableColumn.CellDataFeatures<Account, String> param) {
                StringProperty strProp;
                if (param.getValue().getValue().clearedPastEntry()) strProp = new SimpleStringProperty("Cleared Past Entry");
                else strProp = new SimpleStringProperty("Pending");
                return strProp;
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
        acctTree.getColumns().setAll(acctUsers, acctFirst, acctLast, acctRole, acctAdmin, acctCleared);
        acctTree.setRoot(root);
        acctTree.setShowRoot(false);

        acctTree.setEditable(true);


    }

    private void createAccount(){
        String user  = usernameBox.getText();
        String pass  = passwordBox.getText();
        String first = firstNameBox.getText();
        String last  = lastNameBox.getText();
        String role = roleBox.getValue();


        if(user.equals("") || pass.equals("") ||
        first.equals("") || last.equals("") || role == null){
            handleError();
        }
        else{
            try{
                new Account(user, pass, false,
                        first, last, role).update();
                this.updateAccountTable();
            }
            catch(SQLException e){
                e.printStackTrace();
            }
        }

    }


    private void deleteAccount(){
        if (Session.getAccount().isAdmin() && acctTree.getSelectionModel().getSelectedItem() != null) {
            try{
                acctTree.getSelectionModel().getSelectedItem().getValue().delete();
                this.updateAccountTable();
            }
            catch (SQLException e){
                e.printStackTrace();
            }
        }
    }

    private void updateAccess() {
        if (Session.getAccount().isAdmin() && acctTree.getSelectionModel().getSelectedItem() != null) {
            try {
                acctTree.getSelectionModel().getSelectedItem().getValue().setAdmin(!acctTree.getSelectionModel().getSelectedItem().getValue().isAdmin());
                updateAccountTable();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        else{
            //System.out.println("You aren't an admin, how did you get here?");
        }
    }

    /*

    Verifies the password of the admin before allowing promotion of others to admin status
    removed because it is redundant on an admin-only page
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
            closeButton.setStyle("-fx-background-color: #ba1b26; -fx-text-fill: #000000");
            closeButton.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent event) {
                    passwordWindow.close();
                }
            });

            JFXButton submitButton = new JFXButton(App.resourceBundle.getString("key.submit"));
            submitButton.setStyle("-fx-background-color: rgb(44,147,71); -fx-text-fill: #000000");
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
    } */

    private void handleError() {

        JFXDialogLayout content = new JFXDialogLayout();
        content.setHeading(new Text(App.resourceBundle.getString("key.addErrorMessage")));
        content.setBody(new Text(
                App.resourceBundle.getString("key.please_fill_out_all_fields_with_valid_arguments")));
        JFXDialog errorWindow = new JFXDialog(stackPane, content, JFXDialog.DialogTransition.TOP);

        JFXButton closeButton = new JFXButton(App.resourceBundle.getString("key.close"));
        closeButton.setStyle("-fx-background-color: #F40F19; -fx-text-fill: #fff");
        closeButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                errorWindow.close();
            }
        });

        content.setActions(closeButton);
        errorWindow.show();
    }
}
