package edu.wpi.teamo.views.requests;

import com.jfoenix.controls.*;
import edu.wpi.teamo.App;

import edu.wpi.teamo.Pages;
import edu.wpi.teamo.Session;
import edu.wpi.teamo.database.account.Account;
import edu.wpi.teamo.database.map.NodeInfo;

import edu.wpi.teamo.database.request.BaseRequest;
import edu.wpi.teamo.database.request.MedicineRequest;
import edu.wpi.teamo.utils.itemsifters.LocationSearcher;
import edu.wpi.teamo.views.LocaleType;
import edu.wpi.teamo.views.SubPageContainer;
import edu.wpi.teamo.views.emailSender;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Text;

import javax.mail.MessagingException;
import java.time.LocalDateTime;
import java.net.URL;
import java.sql.SQLException;
import java.time.LocalTime;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class SR07_Medicine implements Initializable {

    @FXML
    private StackPane stackPane;

    @FXML
    private JFXTextField medName;

    @FXML
    private JFXTextField medAmount;

    @FXML
    private JFXComboBox<String> assigneeBox;

    @FXML
    private JFXTextField notes;

    @FXML
    private JFXTimePicker timePicker = new JFXTimePicker();

    @FXML
    private JFXDatePicker datePicker = new JFXDatePicker();

    @FXML
    private Text medErrorText;

    @FXML
    private Text amountErrorText;

    @FXML
    private Text locationErrorText;

    @FXML
    private Text dateErrorText;

    @FXML
    private Text timeErrorText;

    @FXML
    private Text assigneeErrorText;

    @FXML
    private Text assignedLabel;

    @FXML
    private HBox assignedBox;

    @FXML
    private JFXListView<JFXCheckBox> roomList;

    @FXML
    private JFXTextField locationSearchBox;

    @FXML
    private JFXButton backButton;

    private boolean validRequest;

    LocationSearcher locationSearcher;


    @FXML
    public void initialize(URL location, ResourceBundle resources) {

        backButton.setOnAction(actionEvent -> SubPageContainer.switchPage(Pages.SERVICEREQUEST));

        locationSearcher = new LocationSearcher(locationSearchBox, roomList);
        updateLocations();
        updateLocale();

        validRequest = true;

        initAsigneeBox();

        if (Session.getAccount() == null || !Session.getAccount().hasEmployeeAccess()) {
            assignedBox.setVisible(false);
            assignedBox.setManaged(false);
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
        String medicine = medName.getText();
        String amount = medAmount.getText();
        String assignName = assigneeBox.getValue();
        String medNotes = notes.getText();


        List<NodeInfo>          locations = locationSearcher.getSelectedLocations();
        List<String> locationIDs = locationSearcher.getSelectedLocationIDs();

        validRequest = true;

        if (medicine.equals("")) {
            medErrorText.setText(App.resourceBundle.getString("key.no_medicine_specified"));
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

        if (amount.equals("")) {
            amountErrorText.setText(App.resourceBundle.getString("key.no_amount_specified"));
            validRequest = false;
        }

        if (locations.size() == 0) {
            locationErrorText.setText(App.resourceBundle.getString("key.no_room_specified"));
            validRequest = false;
        }
        if (assignName == null) {
            assignName = "Unassigned";
        }

        if (validRequest) {

            LocalTime curTime = timePicker.getValue();
            LocalDateTime curDate = datePicker.getValue().atTime(curTime);
            String randomUU = UUID.randomUUID().toString();

            BaseRequest br = new BaseRequest(randomUU, medNotes, locationIDs.stream(), assignName, false, curDate, Session.getAccount().getUsername());
            new MedicineRequest(medicine, amount, br).update();

            new Thread(() -> {
                try {
                    emailSender.sendSRReceiptMail(Session.getAccount().getEmail(),randomUU,"submitted");
                } catch (MessagingException ignored) { }
            }).start();

            timeErrorText.setText("");
            dateErrorText.setText("");
            medErrorText.setText("");
            amountErrorText.setText("");
            assigneeErrorText.setText("");
            locationErrorText.setText("");

            System.out.println("request successful");
            medName.setText("");
            medAmount.setText("");
            notes.setText("");

            JFXDialogLayout content = new JFXDialogLayout();
            content.setHeading(new Label(App.resourceBundle.getString("key.medicine_request_submitted")));
            content.setBody(new Label(App.resourceBundle.getString("key.request_submitted_with") +
                    App.resourceBundle.getString("key.type_semicolon") + medicine + "\n" +
                    App.resourceBundle.getString("key.amount_semicolon")  + amount + "\n" +
                    App.resourceBundle.getString("key.room_semicolon") + String.join(", ", locationIDs) + "\n" +
                    App.resourceBundle.getString("key.persons_assigned_semicolon")  + assignName + "\n" +
                    App.resourceBundle.getString("key.time") + ": " +  curDate.toString() + "\n" +
                    App.resourceBundle.getString("key.notes") + ": " + medNotes));
            JFXDialog popup = new JFXDialog(stackPane, content, JFXDialog.DialogTransition.TOP);

            JFXButton closeButton = new JFXButton(App.resourceBundle.getString("key.close"));
            JFXButton backButton = new JFXButton(App.resourceBundle.getString("key.back_to_main"));

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
        content.setHeading(new Label(App.resourceBundle.getString("key.help_medicine_request")));
        content.setBody(new Label(App.resourceBundle.getString("key.medicine_name_help") +
                App.resourceBundle.getString("key.amount_help") +
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