package edu.wpi.teamo.views.requests;

import com.jfoenix.controls.*;
import edu.wpi.teamo.App;
import edu.wpi.teamo.Pages;
import edu.wpi.teamo.Session;
import edu.wpi.teamo.database.account.Account;
import edu.wpi.teamo.database.map.NodeInfo;
import edu.wpi.teamo.database.request.BaseRequest;
import edu.wpi.teamo.database.request.ExtendedBaseRequest;
import edu.wpi.teamo.database.request.InterpreterRequest;
import edu.wpi.teamo.utils.itemsifters.LocationSearcher;
import edu.wpi.teamo.views.LocaleType;
import edu.wpi.teamo.views.SubPageContainer;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class SR02_LanguageInterpreter implements Initializable {

    @FXML
    private StackPane stackPane;

    @FXML
    private JFXComboBox<String> assigneeBox;

    @FXML
    private Text assigneeErrorText;

    @FXML
    private Text DateTimeErrorText;

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
    private JFXTimePicker timepicker = new JFXTimePicker();

    @FXML
    private JFXDatePicker datepicker = new JFXDatePicker();

    @FXML
    private Text langErrorText;

    @FXML
    private Text jobErrorText;

    @FXML
    private Text roomErrorText;

    @FXML
    private JFXTextField notes;

    @FXML
    private JFXTextField locationLine;

    @FXML
    private JFXListView<JFXCheckBox> roomList;

    @FXML
    private JFXButton backButton;

    @FXML
    private HBox assignedBox;

    LocationSearcher locationSearcher;


    @FXML
    public void initialize(URL location, ResourceBundle resources) {

        if (Session.getAccount() == null || !Session.getAccount().hasEmployeeAccess()) {
            assignedBox.setVisible(false);
            assignedBox.setManaged(false);
        }

        initAsigneeBox();

        backButton.setOnAction(actionEvent -> SubPageContainer.switchPage(Pages.SERVICEREQUEST));

        topVbox.getStyleClass().add("vbox");
        bottomHbox.getStyleClass().add("vbox");
        midVbox.getStyleClass().add("text-area");

        fillLanguageBox();
        fillJobBox();

        locationSearcher = new LocationSearcher(locationLine, roomList);
        updateLocations();
        updateLocale();
    }

    private void updateLocations() {
        try {
            locationSearcher.setLocations(App.mapService.getAllNodes().collect(Collectors.toList()));
        } catch (SQLException throwables) {
            locationSearcher.setLocations(new LinkedList<>());
            throwables.printStackTrace();
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

        String assigned = assigneeBox.getValue();
        String selectedLanguage = languageBox.getSelectionModel().getSelectedItem();
        String selectedJob = jobBox.getSelectionModel().getSelectedItem();
        String details = notes.getText();

        List<NodeInfo> locations = locationSearcher.getSelectedLocations();
        List<String> locationIDs = locationSearcher.getSelectedLocationIDs();


        validRequest = true;
        if (selectedLanguage == null) {
            langErrorText.setText(App.resourceBundle.getString("key.no_language_specified"));
            validRequest = false;
        }

        if (selectedJob == null) {
            jobErrorText.setText(App.resourceBundle.getString("key.no_job_type_specified"));
            validRequest = false;
        }

        if (locations.size() == 0) {
            roomErrorText.setText(App.resourceBundle.getString("key.no_room_specified"));
            validRequest = false;
        }

        if(assigned == null){
            assigned = "Unassigned";
        }

        if(timepicker.getValue() == null){
            validRequest = false;
            DateTimeErrorText.setText(App.resourceBundle.getString("key.missing_date/time"));
        }

        if(datepicker.getValue() == null){
            validRequest = false;
            DateTimeErrorText.setText(App.resourceBundle.getString("key.missing_date/time"));
        }

       if (validRequest) {

           LocalTime curTime = timepicker.getValue();
           LocalDateTime curDate = datepicker.getValue().atTime(curTime);

           BaseRequest baseRequest = new BaseRequest(UUID.randomUUID().toString(), details, locationIDs.stream(),
                  assigned, false, curDate, Session.getAccount().getUsername());

            new InterpreterRequest(selectedLanguage, selectedJob, baseRequest).update();
            langErrorText.setText("");
            jobErrorText.setText("");
            roomErrorText.setText("");
            ///assigneeErrorText.setText("");
            notes.setText("");
            System.out.println("request successful");

            JFXDialogLayout content = new JFXDialogLayout();
            content.setHeading(new Label(App.resourceBundle.getString("key.language_request_submitted")));
           if(!Session.getAccount().hasEmployeeAccess()) {
               content.setBody(new Label(App.resourceBundle.getString("key.request_submitted_with") +
                       App.resourceBundle.getString("key.language_semicolon") +  selectedLanguage+ "\n" +
                       App.resourceBundle.getString("key.job_type_semicolon") + selectedJob + "\n" +
                       App.resourceBundle.getString("key.room_semicolon") + String.join(", ", locationIDs) + "\n" +
                       App.resourceBundle.getString("key.selected_time_semicolon") + curDate));
           }
           else{
               content.setBody(new Label(App.resourceBundle.getString("key.request_submitted_with") +
                       App.resourceBundle.getString("key.language_semicolon") +  selectedLanguage+ "\n" +
                       App.resourceBundle.getString("key.job_type_semicolon") + selectedJob + "\n" +
                       App.resourceBundle.getString("key.room_semicolon") + String.join(", ", locationIDs) + "\n" +
                       App.resourceBundle.getString("key.persons_assigned_semicolon") + assigned + "\n" +
                       App.resourceBundle.getString("key.selected_time_semicolon") + curDate));
           }

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
        content.setHeading(new Label(App.resourceBundle.getString("key.help_interpreter_request")));
        content.setBody(new Label(App.resourceBundle.getString("key.help_language") +
                App.resourceBundle.getString("key.help_lang_job_type") +
                App.resourceBundle.getString("key.room_help") +
                App.resourceBundle.getString("key.assignee_help")+
                App.resourceBundle.getString("key.date/time_help")));
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

    public void updateLocale() {
        if (App.selectedLocale == LocaleType.en_US) {
            Locale.setDefault(new Locale("en", "US"));
        }
        else if (App.selectedLocale == LocaleType.es_ES) {
            Locale.setDefault(new Locale("es", "US"));
        }
    }
}