package edu.wpi.teamo.views.requests;


import com.jfoenix.controls.*;
import edu.wpi.teamo.App;
import edu.wpi.teamo.Pages;
import edu.wpi.teamo.Session;
import edu.wpi.teamo.database.account.Account;
import edu.wpi.teamo.database.map.NodeInfo;
import edu.wpi.teamo.database.request.*;
import edu.wpi.teamo.views.SubPageContainer;
import edu.wpi.teamo.utils.itemsifters.LocationSearcher;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Text;


import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.LinkedList;

import java.util.List;

import java.util.ResourceBundle;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class SR09_PatientTransportation implements Initializable{
    @FXML
    private StackPane stackPane;

    @FXML
    private JFXComboBox<String> assigneeBox;

    @FXML
    private JFXTextField notes;


    @FXML
    private JFXCheckBox emergencyCheckbox;

    @FXML
    private JFXListView<JFXCheckBox> roomList;

    @FXML
    private JFXTextField room;

    @FXML
    private JFXTextField patientDestination;


    @FXML
    private JFXComboBox<String> transportationMode;

    @FXML
    private Text typeErrorText;

    private boolean isEmergency;

    private boolean validRequest;

    LocationSearcher locationSearcher;

    @FXML
    private Text roomErrorText;

    @FXML
    private Text assignedErrorText;

    @FXML
    private JFXButton backButton;

    @FXML
    public void initialize(URL location, ResourceBundle resources) {

        initAsigneeBox();

        backButton.setOnAction(actionEvent -> SubPageContainer.switchPage(Pages.SERVICEREQUEST));
        emergencyCheckbox.getStyleClass().add("check-box");
        locationSearcher = new LocationSearcher(room, roomList);
        transportationMode.getItems().add("Helicopter");
        transportationMode.getItems().add("Train");
        transportationMode.getItems().add("Car");
        transportationMode.getItems().add("Plane");

        updateLocations();

    }

    private void updateLocations() {
        try {
            locationSearcher.setLocations(App.mapService.getAllNodes().collect(Collectors.toList()));
        } catch (SQLException throwables) {
            locationSearcher.setLocations(new LinkedList<>());
            throwables.printStackTrace();
        }
    }

    private void initAsigneeBox() {
        Stream<Account> employees = Stream.empty();
        try {
            employees = Account.getAll().filter(Account::hasEmployeeAccess);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        employees.forEach(employee -> {
            assigneeBox.getItems().add(employee.getUsername());
        });
    }


    @FXML
    private void handleSubmission(ActionEvent e) throws SQLException {
        String destination = patientDestination.getText();
        String assigned = assigneeBox.getValue();
        String details = notes.getText();

        List<NodeInfo> locations = locationSearcher.getSelectedLocations();
        List<String> locationIDs = locationSearcher.getSelectedLocationIDs();

        if(assigned == null){
            assigned = "Unassigned";
        }

        if (validateRequest()) {

            BaseRequest baseRequest = new BaseRequest(UUID.randomUUID().toString(),
                    details,
                    locationIDs.stream(),
                    assigned,
                    false, LocalDateTime.now(), Session.getAccount().getUsername());


            new TransportationRequest(
                    destination,
                    baseRequest).update();

            System.out.println("Patient Transportation request submitted");

            resetFields();

            receiptPopup(destination, locationIDs, assigned, details);
        }
    }


    private void receiptPopup(String destination, List<String> locationIDs, String assigned, String details) {
        JFXDialogLayout content = new JFXDialogLayout();
        content.setHeading(new Text(App.resourceBundle.getString("key.transportation_request_submitted")));
        content.setBody(new Text(App.resourceBundle.getString("key.request_submitted_with") +
                App.resourceBundle.getString("key.persons_assigned_semicolon") + assigned + "\n" +
                App.resourceBundle.getString("key.additional_notes")+ details + "\n" +
                App.resourceBundle.getString("key.room_semicolon") + String.join(", ", locationIDs) + "\n" +
                App.resourceBundle.getString("key.destination_semicolon") + destination));
        JFXDialog popup = new JFXDialog(stackPane, content, JFXDialog.DialogTransition.TOP);

        JFXButton closeButton = new JFXButton(App.resourceBundle.getString("key.close"));
        JFXButton backButton = new JFXButton(App.resourceBundle.getString("key.back_to_menu"));

        closeButton.setOnAction(event -> popup.close());

        backButton.setOnAction(event -> SubPageContainer.switchPage(Pages.SERVICEREQUEST));

        content.setActions(closeButton, backButton);
        popup.show();
    }

    private boolean validateRequest() {
        validRequest = true;

        if (patientDestination.equals("")) {
            //typeErrorText.setText(App.resourceBundle.getString("key.no_destination"));
            validRequest = false;
        }

        if (locationSearcher.getSelectedLocationIDs().size() == 0) {
            //roomErrorText.setText(App.resourceBundle.getString("key.no_room_specified"));
            validRequest = false;
        }

        return validRequest;
    }

    public void resetFields() {
        locationSearcher.clearSelectedLocations();
        patientDestination.setText("");
        notes.setText("");
    }

    @FXML
    private void handleHelp(ActionEvent e) {

        JFXDialogLayout content = new JFXDialogLayout();
        content.setHeading(new Text(App.resourceBundle.getString("key.help_transportation")));
        content.setBody(new Text(
                App.resourceBundle.getString("key.assignee_help")+
                App.resourceBundle.getString("key.room_help")));
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
