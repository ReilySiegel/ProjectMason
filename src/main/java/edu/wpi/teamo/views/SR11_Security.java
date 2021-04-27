package edu.wpi.teamo.views;

import edu.wpi.teamo.database.map.Node;
import edu.wpi.teamo.database.map.NodeInfo;
import javafx.geometry.Insets;
import javafx.scene.control.*;

import java.util.Locale;
import java.util.stream.Collectors;

import javafx.fxml.Initializable;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.layout.HBox;
import javafx.event.ActionEvent;
import java.util.ResourceBundle;
import java.util.stream.Stream;

import javafx.scene.text.Text;
import com.jfoenix.controls.*;
import java.sql.SQLException;
import java.util.LinkedList;
import edu.wpi.teamo.Pages;
import edu.wpi.teamo.App;
import javafx.fxml.FXML;
import java.util.List;
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
        App.getPrimaryStage().getScene().getStylesheets().add("edu/wpi/teamo/fxml/CSS/SecurityPage.css");
        App.getPrimaryStage().getScene().getStylesheets().add(LocationSearcher.getStylePath());
        locationErrorText.setStyle("-fx-text-fill: red;");
        bottomButtonBar.getStyleClass().add("vbox");
        midVBox.getStyleClass().add("text-area");
        titleBox.getStyleClass().add("vbox");
        submitButton.setOnAction(event -> handleSubmission());

        /* assign event handlers */
        submitButton.setOnAction(event -> handleSubmission());
        helpButton.setOnAction(event -> handleHelp());
        backButton.setOnAction(event -> handleBack());


        /* init search engine */
        locationSearcher = new LocationSearcher(locationSearchBox, searchResults);
        updateLocations();

    }

    private void handleBack() {
        App.switchPage(Pages.SERVICEREQUEST);
    }

    private void handleSubmission() {

        String details = notesBox.getText();
        List<NodeInfo> locations = locationSearcher.getSelectedLocations();
        List<String> locationIDs = locationSearcher.getSelectedLocationIDs();
        boolean emergency = emergencyCheckbox.isSelected();

        boolean validFields = validateFields(locationIDs);

        if (validFields) {
            //App.requestService.requestSanitation(locations.stream(), assigned, serviceName + ", " + details);
            System.out.println("Security request submitted");

            System.out.println(String.join(",", locationIDs));

            resetInputs();

            receiptDialog(emergency, locations, details);
        }
        else {
            locationErrorText.setText("Please select a location.");
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
        content.setHeading(new Text("Help - Security Service Request"));
        content.setBody(new Text("Emergency: Check this box if this is an emergency.\n" +
                "Notes: Any necessary details about the situation.\n" +
                "Locations: The locations where the security is needed.\n" +
                "Type into the search field and click the checkboxes to select the desired locations."));
        JFXDialog errorWindow = new JFXDialog(parentStackPane, content, JFXDialog.DialogTransition.TOP);

        JFXButton closeButton = new JFXButton("Close");
        closeButton.setStyle("-fx-background-color: #F40F19; -fx-text-fill: #fff");
        closeButton.setOnAction(event -> errorWindow.close());

        content.setActions(closeButton);
        errorWindow.show();
    }

    private void receiptDialog(boolean emergency, List<NodeInfo> locations, String details) {
        JFXDialogLayout content = new JFXDialogLayout();
        content.setHeading(new Text("Security Request Submitted"));
        content.setBody(new Text("Request submitted with: \n" +
                "Emergency: " + (emergency?"Yes":"No") + "\n" +
                "Locations: " + locations.stream().map(NodeInfo::getLongName).collect(Collectors.joining(", ")) + "\n" +
                "Additional notes: " + details));

        JFXDialog popup = new JFXDialog(parentStackPane, content, JFXDialog.DialogTransition.TOP);

        JFXButton closeButton = new JFXButton("Close");
        JFXButton backButton = new JFXButton("Back to Menu");

        closeButton.setStyle("-fx-background-color: #F40F19; -fx-text-fill: #fff");
        backButton.setStyle("-fx-background-color: #333333; -fx-text-fill: #fff");

        closeButton.setOnAction(event -> popup.close());

        backButton.setOnAction(event -> App.switchPage(Pages.SERVICEREQUEST));

        content.setActions(closeButton, backButton);
        popup.show();
    }

}



