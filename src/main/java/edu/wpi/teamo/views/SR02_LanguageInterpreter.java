package edu.wpi.teamo.views;

import com.jfoenix.controls.*;
import edu.wpi.teamo.App;
import edu.wpi.teamo.Pages;
import edu.wpi.teamo.database.map.NodeInfo;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.CheckMenuItem;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

import java.net.URL;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class SR02_LanguageInterpreter extends ServiceRequestPage implements Initializable {

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
    private JFXComboBox<String> languageBox;

    @FXML
    private JFXComboBox<String> jobBox;

    @FXML
    private VBox topVbox;

    @FXML
    private HBox bottomHbox;

    @FXML
    private VBox midVbox;

    @FXML
    private JFXTimePicker timepicker;

    @FXML
    private JFXDatePicker datepicker;

    @FXML
    private Text langErrorText;

    @FXML
    private Text jobErrorText;


    @FXML
    public void initialize(URL location, ResourceBundle resources) {
        topVbox.getStyleClass().add("vbox");
        bottomHbox.getStyleClass().add("vbox");
        midVbox.getStyleClass().add("text-area");

        fillLanguageBox();
        fillJobBox();
        validRequest = true;
        try {
            resetLocationBox();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void fillLanguageBox() {
        languageBox.getItems().add("Spanish");
        languageBox.getItems().add("Chinese");
        languageBox.getItems().add("Haitian");
        languageBox.getItems().add("Portuguese");
        languageBox.getItems().add("Vietnamese");
        languageBox.getItems().add("French");
        languageBox.getItems().add("Arabic");
        languageBox.getItems().add("Russian");
        languageBox.getItems().add("Italian");
        languageBox.getItems().add("Hindi");
    }

    public void fillJobBox() {
        jobBox.getItems().add("Face-to-Face");
        jobBox.getItems().add("Phone Call");
        jobBox.getItems().add("Video Call");
        jobBox.getItems().add("Documentation");
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
        String assignName = assignee.getText();
        String selectedLanguage = languageBox.getSelectionModel().getSelectedItem();
        String selectedJob = jobBox.getSelectionModel().getSelectedItem();
        String selectedTime = timepicker.getValue().toString();
        String selectedDate = datepicker.getValue().toString();

        List<MenuItem>        mItems    = locationBox.getItems();
        Stream<CheckMenuItem> cMItems   = mItems.stream().map((MenuItem mI) -> (CheckMenuItem) mI);
        Stream<CheckMenuItem> checked   = cMItems.filter(CheckMenuItem::isSelected);
        List<String>          locations = checked.map(CheckMenuItem::getText).collect(Collectors.toList());

        validRequest = true;
        if (selectedLanguage.equals("")) {
            langErrorText.setText(App.resourceBundle.getString("key.no_language_specified"));
            validRequest = false;
        }

        if (selectedJob.equals("")) {
            jobErrorText.setText(App.resourceBundle.getString("key.no_job_type_specified"));
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
            //App.InterpreterRequest("hi","hi","hi","hi","hi");
            langErrorText.setText("");
            jobErrorText.setText("");
            roomErrorText.setText("");
            assigneeErrorText.setText("");
            System.out.println("request successful");
            assignee.setText("");

            JFXDialogLayout content = new JFXDialogLayout();
            content.setHeading(new Text(App.resourceBundle.getString("key.language_request_submitted")));
            content.setBody(new Text(App.resourceBundle.getString("key.request_submitted_with") +
                    App.resourceBundle.getString("key.language_semicolon") +  selectedLanguage+ "\n" +
                    App.resourceBundle.getString("key.job_type_semicolon") + selectedJob + "\n" +
                    App.resourceBundle.getString("key.room_semicolon") + String.join(", ", locations) + "\n" +
                    App.resourceBundle.getString("key.persons_assigned_semicolon") + assignName));
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
        content.setHeading(new Text(App.resourceBundle.getString("key.help_interpreter_request")));
        content.setBody(new Text(App.resourceBundle.getString("key.help_language") +
                App.resourceBundle.getString("key.help_lang_job_type") +
                App.resourceBundle.getString("key.room_help") +
                App.resourceBundle.getString("key.assignee_help")+
                App.resourceBundle.getString("key.date/time_help")));
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