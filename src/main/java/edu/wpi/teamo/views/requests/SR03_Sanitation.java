package edu.wpi.teamo.views.requests;

import com.jfoenix.controls.*;
import edu.wpi.teamo.App;
import edu.wpi.teamo.Pages;
import edu.wpi.teamo.Session;
import edu.wpi.teamo.database.account.Account;
import edu.wpi.teamo.database.map.NodeInfo;
import edu.wpi.teamo.database.request.BaseRequest;
import edu.wpi.teamo.database.request.SanitationRequest;
import edu.wpi.teamo.views.LocaleType;
import edu.wpi.teamo.views.SubPageContainer;
import edu.wpi.teamo.utils.itemsifters.LocationSearcher;
import edu.wpi.teamo.views.emailSender;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

import javax.mail.MessagingException;
import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class SR03_Sanitation implements Initializable {

    @FXML
    private StackPane stackPane;

    @FXML
    private JFXTextField service;

    @FXML
    private VBox topVbox;

    @FXML
    private VBox midVbox;

    @FXML
    private HBox bottomHbox;

    @FXML
    private JFXComboBox<String> assigneeBox;

    @FXML
    private JFXButton backButton;

    @FXML
    private JFXTimePicker timePicker = new JFXTimePicker();

    @FXML
    private JFXDatePicker datePicker = new JFXDatePicker();

    @FXML
    private JFXTextField notes;

    @FXML
    private Text typeErrorText;

    @FXML
    private Text roomErrorText;

    @FXML
    private Text dateErrorText;

    @FXML
    private Text timeErrorText;

    @FXML
    private Text assignedErrorText;

    @FXML
    private JFXTextField locationLine;

    @FXML
    private JFXCheckBox recurCheck;

    @FXML
    private JFXListView<JFXCheckBox> roomList;

    @FXML
    private HBox assignedBox;

    LocationSearcher locationSearcher;

    private boolean validRequest;

    private boolean recurring = false;

    private String recurMsg = App.resourceBundle.getString("key.is_not_recurring");

    @FXML
    public void initialize(URL location, ResourceBundle resources) {

        backButton.setOnAction(actionEvent -> SubPageContainer.switchPage(Pages.SERVICEREQUEST));

        initAsigneeBox();

        topVbox.getStyleClass().add("vbox");
        bottomHbox.getStyleClass().add("vbox");
        midVbox.getStyleClass().add("text-area");
        recurCheck.getStyleClass().add("check-box");

        if (Session.getAccount() == null || !Session.getAccount().hasEmployeeAccess()) {
            assignedBox.setVisible(false);
            assignedBox.setManaged(false);
        }

        recurCheck.selectedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                if (newValue){
                    recurring = true;
                    recurMsg = App.resourceBundle.getString("key.is_recurring");
                }
                else{
                    recurring = false;
                    recurMsg = App.resourceBundle.getString("key.is_not_recurring");
                }
            }
        });




        locationSearcher = new LocationSearcher(locationLine, roomList);
        updateLocations();
        updateLocale();


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
    private void handleSubmission(ActionEvent e) throws SQLException, MessagingException {
        String serviceName = service.getText();
        String assigned = assigneeBox.getValue();
        String details = notes.getText();

        List<NodeInfo>          locations = locationSearcher.getSelectedLocations();
        List<String> locationIDs = locationSearcher.getSelectedLocationIDs();



        validRequest = true;

        if (serviceName.equals("")) {
            typeErrorText.setText(App.resourceBundle.getString("key.no_sanitation_type_specified"));
            validRequest = false;
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

        if (assigned == null) {
            assigned = "Unassigned";
        }

        if (validRequest) {

            //Clear earlier error texts
            typeErrorText.setText("");
            timeErrorText.setText("");
            dateErrorText.setText("");
            roomErrorText.setText("");
            assignedErrorText.setText("");


            LocalTime curTime = timePicker.getValue();
            LocalDateTime curDate = datePicker.getValue().atTime(curTime);
            String randomUU = UUID.randomUUID().toString();

            BaseRequest baseRequest = new BaseRequest(randomUU, serviceName + ", " + details, locationIDs.stream(),
                    assigned, false, curDate, Session.getAccount().getUsername());

            new SanitationRequest(recurring, baseRequest).update();
            System.out.println("Sanitation request submitted");

            new Thread(() -> {
                try {
                    emailSender.sendSRReceiptMail(Session.getAccount().getEmail(),randomUU,"submitted");
                } catch (MessagingException ignored) { }
            }).start();

            service.setText("");
            notes.setText("");

            JFXDialogLayout content = new JFXDialogLayout();
            content.setHeading(new Label(App.resourceBundle.getString("key.sanitation_request_submitted")));

            if(!Session.getAccount().hasEmployeeAccess()){
                content.setBody(new Label(App.resourceBundle.getString("key.request_submitted_with") +
                        App.resourceBundle.getString("key.type_of_sanitation") + serviceName + "\n" +
                        App.resourceBundle.getString("key.room_semicolon") + String.join(", ", locationIDs) + "\n" +
                        App.resourceBundle.getString("key.time") + ": " + curDate.toString() + "\n" +
                        recurMsg + "\n" +
                        App.resourceBundle.getString("key.additional_notes") + details));
            }
            else{
                content.setBody(new Label(App.resourceBundle.getString("key.request_submitted_with") +
                        App.resourceBundle.getString("key.type_of_sanitation") + ": " +  serviceName + "\n" +
                        App.resourceBundle.getString("key.room_semicolon") + String.join(", ", locationIDs) + "\n" +
                        App.resourceBundle.getString("key.persons_assigned_semicolon") + assigned + "\n" +
                        App.resourceBundle.getString("key.time") + ": " + curDate.toString() + "\n" +
                        recurMsg + "\n" +
                        App.resourceBundle.getString("key.additional_notes") + details));
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
        content.setHeading(new Label(App.resourceBundle.getString("key.help_sanitation")));
        content.setBody(new Label(App.resourceBundle.getString("key.sanitation_type_help") +
                App.resourceBundle.getString("key.room_help") +
                App.resourceBundle.getString("key.assignee_help")));
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
