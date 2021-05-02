package edu.wpi.teamo.views;

import edu.wpi.teamo.database.map.Node;
import edu.wpi.teamo.database.map.NodeInfo;
import edu.wpi.teamo.database.request.BaseRequest;
import edu.wpi.teamo.database.request.MedicineRequest;
import edu.wpi.teamo.database.request.SecurityRequest;
import javafx.geometry.Insets;
import javafx.scene.control.*;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

import javafx.fxml.Initializable;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.layout.HBox;
import javafx.event.ActionEvent;

import java.util.stream.Stream;

import javafx.scene.text.Text;
import com.jfoenix.controls.*;
import java.sql.SQLException;

import edu.wpi.teamo.Pages;
import edu.wpi.teamo.App;
import javafx.fxml.FXML;

import java.net.URL;

public class SR11_Security extends ServiceRequestPage implements Initializable {

    @FXML
    private StackPane parentStackPane;

    @FXML
    private JFXButton submitButton;

    @FXML
    private JFXButton backButton;

    @FXML
    private JFXButton helpButton;

    @FXML
    private VBox titleBox;

    @FXML
    private VBox midVBox;

    @FXML
    private HBox bottomButtonBar;

    @FXML
    private JFXTextField locationSearchBox;

    @FXML
    private Text locationErrorText;

    @FXML
    private JFXTextField notesBox;

    @FXML
    private JFXCheckBox emergencyCheckbox;

    @FXML
    private JFXListView<JFXCheckBox> searchResults;

    LocationSearcher locationSearcher;

    @FXML
    public void initialize(URL location, ResourceBundle resources) {



        /* add styles */
        locationErrorText.setStyle("-fx-text-fill: red;");
        bottomButtonBar.getStyleClass().add("vbox");
        midVBox.getStyleClass().add("text-area");
        titleBox.getStyleClass().add("vbox");
        submitButton.setOnAction(event -> handleSubmission());

        /* assign event handlers */
        submitButton.setOnAction(event -> handleSubmission());
        helpButton.setOnAction(event -> handleHelp());
        backButton.setOnAction(actionEvent -> SubPageContainer.switchPage(Pages.SERVICEREQUEST));


        /* init search engine */
        locationSearcher = new LocationSearcher(locationSearchBox, searchResults);
        updateLocations();

    }



    private void handleSubmission() {

        String details = notesBox.getText();
        List<NodeInfo> locations = locationSearcher.getSelectedLocations();
        List<String> locationIDs = locationSearcher.getSelectedLocationIDs();
        boolean emergency = emergencyCheckbox.isSelected();

        boolean validFields = validateFields(locationIDs);

        if (validFields) {
            //App.requestService.requestSanitation(locations.stream(), assigned, serviceName + ", " + details);

            LocalDateTime dateTime = LocalDateTime.now();

            SecurityRequest sr = new SecurityRequest(
                                                    emergency,
                                                    new BaseRequest(
                                                                    UUID.randomUUID().toString(),
                                                                    details,
                                                                    locationIDs.stream(),
                                                                    "",
                                                                    false,
                                                                    dateTime)
                                                    );
            try {
                sr.update();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }

            System.out.println("Security request submitted");

            System.out.println(String.join(",", locationIDs));

            resetInputs();

            receiptDialog(emergency, locations, details);
        }
        else {
            locationErrorText.setText(App.resourceBundle.getString("key.please_select_a_location"));
        }
    }

    private void updateLocations() {
        try {
            locationSearcher.setLocations(App.mapService.getAllNodes().collect(Collectors.toList()));
        } catch (SQLException throwables) {
            locationSearcher.setLocations(new LinkedList<>());
            throwables.printStackTrace();
        }
    }

    private static List<String> getSelectedLocationsFromMenuButton(MenuButton locationBox) {
        List<MenuItem> mItems = locationBox.getItems();
        Stream<CheckMenuItem> cMItems = mItems.stream().map((MenuItem mI) -> (CheckMenuItem) mI);
        Stream<CheckMenuItem> checked = cMItems.filter(CheckMenuItem::isSelected);
        return checked.map(CheckMenuItem::getText).collect(Collectors.toList());
    }

    private boolean validateFields(List<String> locations) {
        boolean validRequest = true;

        if (locations.size() == 0) {
            validRequest = false;
        }

        return validRequest;
    }

    private void resetInputs() {
        emergencyCheckbox.setSelected(false);
        locationSearchBox.setText("");
        notesBox.setText("");
    }

    private void handleHelp() {

        JFXDialogLayout content = new JFXDialogLayout();
        content.setHeading(new Text(App.resourceBundle.getString("key.help_security_request")));
        content.setBody(new Text(App.resourceBundle.getString("key.emergency_help") +
                App.resourceBundle.getString("key.notes_help") +
                App.resourceBundle.getString("key.locations_help1") +
                App.resourceBundle.getString("key.locations_help2")));
        JFXDialog errorWindow = new JFXDialog(parentStackPane, content, JFXDialog.DialogTransition.TOP);

        JFXButton closeButton = new JFXButton(App.resourceBundle.getString("key.close"));
        closeButton.setStyle("-fx-background-color: #F40F19; -fx-text-fill: #fff");
        closeButton.setOnAction(event -> errorWindow.close());

        content.setActions(closeButton);
        errorWindow.show();
    }

    private void receiptDialog(boolean emergency, List<NodeInfo> locations, String details) {
        JFXDialogLayout content = new JFXDialogLayout();
        content.setHeading(new Text(App.resourceBundle.getString("key.security_request_submitted")));
        content.setBody(new Text(App.resourceBundle.getString("key.request_submitted_with") +
                App.resourceBundle.getString("key.emergency_semicolon") +
                (emergency?App.resourceBundle.getString("key.yes"):App.resourceBundle.getString("key.no")) + "\n" +
                App.resourceBundle.getString("key.locations_semicolon") +
                locations.stream().map(NodeInfo::getLongName).collect(Collectors.joining(", ")) + "\n" +
                App.resourceBundle.getString("key.additional_notes")  + details));

        JFXDialog popup = new JFXDialog(parentStackPane, content, JFXDialog.DialogTransition.TOP);

        JFXButton closeButton = new JFXButton(App.resourceBundle.getString("key.close"));
        JFXButton backButton = new JFXButton(App.resourceBundle.getString("key.back_to_main"));

        closeButton.setStyle("-fx-background-color: #F40F19; -fx-text-fill: #fff");
        backButton.setStyle("-fx-background-color: #333333; -fx-text-fill: #fff");

        closeButton.setOnAction(event -> popup.close());

        backButton.setOnAction(event -> App.switchPage(Pages.SERVICEREQUEST));

        content.setActions(closeButton, backButton);
        popup.show();
    }

}



