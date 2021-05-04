package edu.wpi.teamo.views;

import com.jfoenix.controls.*;
import edu.wpi.teamo.App;
import edu.wpi.teamo.Pages;
import edu.wpi.teamo.database.map.NodeInfo;
import edu.wpi.teamo.database.request.BaseRequest;
import edu.wpi.teamo.database.request.ReligiousRequest;
import edu.wpi.teamo.database.request.SanitationRequest;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.Initializable;
import javafx.scene.control.Labeled;
import javafx.scene.control.MenuButton;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.fxml.FXML;
import javafx.scene.layout.VBox;
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


public class SR08_Religious extends ServiceRequestPage implements Initializable {
    @FXML
    private StackPane stackPane;

    @FXML
    private JFXTextField service;

    @FXML
    private JFXTextField assigneeName;

    @FXML
    private JFXTextField notes;

    @FXML
    private JFXTextField religiousFigure;

    @FXML
    private JFXCheckBox lastRites;

    @FXML
    private JFXListView<JFXCheckBox> roomList;

    @FXML
    private JFXTextField room;

    @FXML
    private VBox topVbox;

    @FXML
    private VBox midVbox;

    @FXML
    private HBox bottomHbox;

    @FXML
    private Text typeErrorText;

    private boolean needLastRites;

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

        backButton.setOnAction(actionEvent -> SubPageContainer.switchPage(Pages.SERVICEREQUEST));

        topVbox.getStyleClass().add("vbox");
        bottomHbox.getStyleClass().add("vbox");
        midVbox.getStyleClass().add("text-area");
        lastRites.getStyleClass().add("check-box");

        locationSearcher = new LocationSearcher(room, roomList);
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

    @FXML
    private void handleSubmission(ActionEvent e) throws SQLException {
        String serviceName = service.getText();
        String assigned = assigneeName.getText();
        String details = notes.getText();

        List<NodeInfo> locations = locationSearcher.getSelectedLocations();
        List<String> locationIDs = locationSearcher.getSelectedLocationIDs();

        if (validateRequest()) {

            BaseRequest baseRequest = new BaseRequest(UUID.randomUUID().toString(),
                                                      serviceName + ", " + details,
                                                      locationIDs.stream(),
                                                      assigned,
                                                      false
                                                      );

            new ReligiousRequest(service.getText(),
                                 religiousFigure.getText(),
                                 lastRites.isSelected(),
                                 baseRequest).update();

            System.out.println("Religious request submitted");

            resetFields();

            receiptPopup(serviceName, locationIDs, assigned, details);
        }
    }

    private void receiptPopup(String serviceName, List<String> locationIDs, String assigned, String details) {
        JFXDialogLayout content = new JFXDialogLayout();
        content.setHeading(new Text(App.resourceBundle.getString("key.religious_request_submitted")));
        content.setBody(new Text(App.resourceBundle.getString("key.request_submitted_with") +
                App.resourceBundle.getString("key.type_of_service") + serviceName + "\n" +
                App.resourceBundle.getString("key.persons_assigned_semicolon") + assigned + "\n" +
                App.resourceBundle.getString("key.additional_notes")+ details + "\n" +
                App.resourceBundle.getString("key.room_semicolon") + String.join(", ", locationIDs) + "\n" +
                App.resourceBundle.getString("key.religious_figure_semicolon") + religiousFigure.getText() + "\n" +
                App.resourceBundle.getString("key.last_rights_semicolon") + lastRites.isSelected()));
        JFXDialog popup = new JFXDialog(stackPane, content, JFXDialog.DialogTransition.TOP);

        JFXButton closeButton = new JFXButton(App.resourceBundle.getString("key.close"));
        JFXButton backButton = new JFXButton(App.resourceBundle.getString("key.back_to_main"));

        closeButton.setOnAction(event -> popup.close());

        backButton.setOnAction(event -> SubPageContainer.switchPage(Pages.SERVICEREQUEST));

        content.setActions(closeButton, backButton);
        popup.show();
    }

    private boolean validateRequest() {
        validRequest = true;

        if (service.getText().equals("")) {
            typeErrorText.setText(App.resourceBundle.getString("key.no_religion_type_specified"));
            validRequest = false;
        }

        if (locationSearcher.getSelectedLocationIDs().size() == 0) {
            roomErrorText.setText(App.resourceBundle.getString("key.no_room_specified"));
            validRequest = false;
        }

        /*
        if(assigneeName.getText().equals("")){
            assignedErrorText.setText(App.resourceBundle.getString("key.no_assignee_specified"));
            validRequest = false;
        } */

        return validRequest;
    }


    public void handleHelpPress(ActionEvent e){
        JFXDialogLayout content = new JFXDialogLayout();
        content.setHeading(new Text(App.resourceBundle.getString("key.help")));
        content.setBody(new Text(App.resourceBundle.getString("key.religion_help")));
        JFXDialog helpWindow = new JFXDialog(stackPane, content, JFXDialog.DialogTransition.TOP);

        JFXButton closeButton = new JFXButton(App.resourceBundle.getString("key.close"));
        closeButton.setStyle("-fx-background-color: #f40f19");
        closeButton.setOnAction(event -> helpWindow.close());

        content.setActions(closeButton);
        helpWindow.show();
    }

    public void resetFields() {
        locationSearcher.clearSelectedLocations();
        service.setText("");
        assigneeName.setText("");
        religiousFigure.setText("");
        notes.setText("");
        typeErrorText.setText("");
        roomErrorText.setText("");
    }

}
