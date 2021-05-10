package edu.wpi.teamo.views.requests;

import com.jfoenix.controls.*;
import edu.wpi.teamo.App;
import edu.wpi.teamo.Pages;
import edu.wpi.teamo.Session;
import edu.wpi.teamo.database.map.NodeInfo;
import edu.wpi.teamo.database.request.BaseRequest;
import edu.wpi.teamo.database.request.LaundryRequest;
import edu.wpi.teamo.utils.itemsifters.LocationSearcher;
import edu.wpi.teamo.views.SubPageContainer;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.MenuButton;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Text;

import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.LinkedList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.UUID;
import java.util.stream.Collectors;

public class SR04_Laundry implements Initializable {

    @FXML
    private StackPane stackPane;

    @FXML
    private JFXTextField service;

    @FXML
    private Text checkError;

    @FXML
    private JFXCheckBox gown;

    @FXML
    private JFXCheckBox sheets;

    @FXML
    private JFXTextField assignee;

    @FXML
    private JFXTextField notes;

    @FXML
    private Text assigneeErrorText;

    @FXML
    private Text roomErrorText;

    @FXML
    private Text dateErrorText;

    @FXML
    private Text timeErrorText;

    @FXML
    private JFXButton backButton;

    @FXML
    private JFXDatePicker datePicker;

    @FXML
    private  JFXTimePicker timePicker;

    @FXML
    private JFXTextField locationSearchBox;

    @FXML
    private JFXListView<JFXCheckBox> roomList;

    @FXML
    private HBox assignedBox;


    private boolean validRequest;

    LocationSearcher locationSearcher;


    @FXML
    public void initialize(URL location, ResourceBundle resources) {

        if (Session.getAccount() == null || !Session.getAccount().hasEmployeeAccess()) {
            assignedBox.setVisible(false);
            assignedBox.setManaged(false);
        }

        backButton.setOnAction(actionEvent -> SubPageContainer.switchPage(Pages.SERVICEREQUEST));

        locationSearcher = new LocationSearcher(locationSearchBox, roomList);
        updateLocations();

        validRequest = true;
    }

    private void updateLocations() {
        try {
            locationSearcher.setLocations(App.mapService.getAllNodes().collect(Collectors.toList()));
        } catch (SQLException throwables) {
            locationSearcher.setLocations(new LinkedList<>());
            throwables.printStackTrace();
        }
    }


    @FXML
    private void clearAssigneeError(KeyEvent e) {
        assigneeErrorText.setText("");
    }

    @FXML
    private void handleSubmission(ActionEvent e) throws SQLException {
        boolean gownCheck = gown.isSelected();
        boolean sheetCheck = sheets.isSelected();
        String assignName = assignee.getText();
        String note = notes.getText();
        String laundryChecked = "";

        List<NodeInfo> locations = locationSearcher.getSelectedLocations();
        List<String> locationIDs = locationSearcher.getSelectedLocationIDs();

        validRequest = true;
        if (!gownCheck && !sheetCheck) {
            validRequest = false;
            checkError.setText(App.resourceBundle.getString("key.laundry_check_error"));
        } else {
            if (gownCheck) {
                laundryChecked = " Gowns ";
                if (sheetCheck) {
                    laundryChecked += "and Sheets";
                }
            } else {
                laundryChecked = "Sheets";
            }
        }


        if(timePicker.getValue() == null){
            timeErrorText.setText(App.resourceBundle.getString("key.no_time_specified"));
            validRequest = false;
        }

        if(datePicker.getValue() == null){
            dateErrorText.setText(App.resourceBundle.getString("key.no_date_specified"));
            validRequest = false;
        }
        if (locations.size() == 0) {
            roomErrorText.setText(App.resourceBundle.getString("key.no_room_specified"));
            validRequest = false;
        }
        if (assignName.equals("")) {
            assignName = "Unassigned";
        }

        if (validRequest) {

            LocalTime curTime = timePicker.getValue();
            LocalDateTime curDate = datePicker.getValue().atTime(curTime);
            BaseRequest br =
                    new BaseRequest(UUID.randomUUID().toString(), note, locationIDs.stream(),
                            assignName, false, curDate, Session.getAccount().getUsername());
            new LaundryRequest(gownCheck, sheetCheck, br).update();


            assigneeErrorText.setText("");
            roomErrorText.setText("");
            checkError.setText("");
            timeErrorText.setText("");
            dateErrorText.setText("");
            System.out.println("request successful");

            assignee.setText("");
            this.notes.setText("");

            JFXDialogLayout content = new JFXDialogLayout();
            content.setHeading(new Text(App.resourceBundle.getString("key.laundry_request_submitted")));
            content.setBody(new Text(App.resourceBundle.getString("key.request_submitted_with") +
                    App.resourceBundle.getString("key.laundry_checked") + laundryChecked + "\n" +
                    App.resourceBundle.getString("key.room_semicolon") + String.join(", ", locationIDs) + "\n" +
                    App.resourceBundle.getString("key.persons_assigned_semicolon") + assignName + "\n" +
                    App.resourceBundle.getString("key.time") + ": " +  curDate.toString() + "\n" +
                    App.resourceBundle.getString("key.notes") + ": " + note));
            JFXDialog popup = new JFXDialog(stackPane, content, JFXDialog.DialogTransition.TOP);

            JFXButton closeButton = new JFXButton(App.resourceBundle.getString("key.close"));
            JFXButton backButton = new JFXButton(App.resourceBundle.getString("key.back_to_menu"));

            closeButton.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent event) {
                    popup.close();
                }
            });

            backButton.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent event) {
                    SubPageContainer.switchPage(Pages.SERVICEREQUEST);
                }
            });

            content.setActions(closeButton, backButton);
            popup.show();
        }
    }

    @FXML
    private void handleHelp(ActionEvent e) {

        JFXDialogLayout content = new JFXDialogLayout();
        content.setHeading(new Text(App.resourceBundle.getString("key.help_laundry_request")));
        content.setBody(new Text(App.resourceBundle.getString("key.room_help") +
                App.resourceBundle.getString("key.assignee_help") +
                App.resourceBundle.getString("key.room_help")  +
                App.resourceBundle.getString("key.laundry_check_help")));

        JFXDialog errorWindow = new JFXDialog(stackPane, content, JFXDialog.DialogTransition.TOP);

        JFXButton closeButton = new JFXButton(App.resourceBundle.getString("key.close"));
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
