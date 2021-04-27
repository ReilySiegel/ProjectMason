package edu.wpi.teamo.views;

import com.jfoenix.controls.*;
import edu.wpi.teamo.App;

import edu.wpi.teamo.Pages;
import edu.wpi.teamo.database.map.NodeInfo;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Text;

import java.net.URL;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class SR07_Medicine extends ServiceRequestPage implements Initializable {

    @FXML
    private StackPane stackPane;

    @FXML
    private JFXTextField medName;

    @FXML
    private JFXTextField medAmount;

    @FXML
    private JFXTextField assignee;

    @FXML
    private Text medErrorText;

    @FXML
    private Text amountErrorText;

    @FXML
    private Text roomErrorText;

    @FXML
    private Text assigneeErrorText;

    @FXML
    private MenuButton locationBox;

    private boolean validRequest;


    @FXML
    public void initialize(URL location, ResourceBundle resources) {

        validRequest = true;
        try {
            resetLocationBox();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void resetLocationBox() throws SQLException {
        LinkedList<NodeInfo> nodes = App.mapService.getAllNodes().collect(Collectors.toCollection(LinkedList::new));
        locationBox.getItems().removeAll(locationBox.getItems());

        for (NodeInfo node : nodes) {
            CheckMenuItem menuItem = new CheckMenuItem();
            menuItem.setText(node.getNodeID());

            menuItem.setOnAction(event -> {
                locationBox.setText(locationBox.getItems().stream()
                        .map((MenuItem mI) -> (CheckMenuItem) mI)
                        .filter(CheckMenuItem::isSelected)
                        .map(CheckMenuItem::getText)
                        .collect(Collectors.joining(", ")));
            });

            locationBox.getItems().add(menuItem);
        }

    }

    @FXML
    private void clearMedError(KeyEvent e) {
        medErrorText.setText("");
    }

    @FXML
    private void clearAmountError(KeyEvent e) {
        amountErrorText.setText("");
    }

//    @FXML
//    private void clearRoomError(ActionEvent e) {
//        roomErrorText.setText("");
//    }

    @FXML
    private void clearAssigneeError(KeyEvent e) {
        assigneeErrorText.setText("");
    }

    @FXML
    private void handleSubmission(ActionEvent e) throws SQLException {
        String medicine = medName.getText();
        String amount = medAmount.getText();
        String assignName = assignee.getText();

        List<MenuItem>        mItems    = locationBox.getItems();
        Stream<CheckMenuItem> cMItems   = mItems.stream().map((MenuItem mI) -> (CheckMenuItem) mI);
        Stream<CheckMenuItem> checked   = cMItems.filter(CheckMenuItem::isSelected);
        List<String>          locations = checked.map(CheckMenuItem::getText).collect(Collectors.toList());

        validRequest = true;

        if (medicine.equals("")) {
            medErrorText.setText(App.resourceBundle.getString("key.no_medicine_specified"));
            validRequest = false;
        }

        if (amount.equals("")) {
            amountErrorText.setText(App.resourceBundle.getString("key.no_amount_specified"));
            validRequest = false;
        }

        if (locations.size() == 0) {
            roomErrorText.setText(App.resourceBundle.getString("key.no_room_specified"));
            validRequest = false;
        }
        if (assignName.equals("")) {
            assigneeErrorText.setText(App.resourceBundle.getString("key.no_assignee_specified"));
            validRequest = false;
        }

        if (validRequest) {
            App.requestService.requestMedicine(medicine, amount, locations.stream(), assignName);
            medErrorText.setText("");
            amountErrorText.setText("");
            roomErrorText.setText("");
            assigneeErrorText.setText("");
            System.out.println("request successful");
            medName.setText("");
            medAmount.setText("");
            assignee.setText("");

            JFXDialogLayout content = new JFXDialogLayout();
            content.setHeading(new Text(App.resourceBundle.getString("key.medicine_request_submitted")));
            content.setBody(new Text(App.resourceBundle.getString("key.request_submitted_with") +
                    App.resourceBundle.getString("key.type_semicolon")  + medicine + "\n" +
                    App.resourceBundle.getString("key.amount_semicolon")  + amount + "\n" +
                    App.resourceBundle.getString("key.room_semicolon")  + String.join(", ", locations) + "\n" +
                    App.resourceBundle.getString("key.persons_assigned_semicolon")  + assignName));
            JFXDialog popup = new JFXDialog(stackPane, content, JFXDialog.DialogTransition.TOP);

            JFXButton closeButton = new JFXButton(App.resourceBundle.getString("key.close"));
            JFXButton backButton = new JFXButton(App.resourceBundle.getString("key.back_to_main"));

            closeButton.setStyle("-fx-background-color: #F40F19; -fx-text-fill: #fff");
            backButton.setStyle("-fx-background-color: #333333; -fx-text-fill: #fff");

            closeButton.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent event) {
                    popup.close();
                }
            });

            backButton.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent event) {
                    App.switchPage(Pages.SERVICEREQUEST);
                }
            });

            content.setActions(closeButton, backButton);
            popup.show();
        }
    }

    @FXML
    private void handleHelp(ActionEvent e) {

        JFXDialogLayout content = new JFXDialogLayout();
        content.setHeading(new Text(App.resourceBundle.getString("key.help_medicine_request")));
        content.setBody(new Text(App.resourceBundle.getString("key.medicine_name_help") +
                App.resourceBundle.getString("key.amount_help") +
                App.resourceBundle.getString("key.room_help") +
                App.resourceBundle.getString("key.assignee_help")));
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