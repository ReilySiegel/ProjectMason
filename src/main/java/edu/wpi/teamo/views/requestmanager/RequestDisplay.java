package edu.wpi.teamo.views.requestmanager;

import edu.wpi.teamo.database.account.Account;
import java.time.format.DateTimeFormatter;
import edu.wpi.teamo.database.request.*;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.MenuItem;
import javafx.scene.input.MouseEvent;
import javafx.scene.control.Label;
import javafx.event.EventHandler;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.event.ActionEvent;
import java.time.LocalDateTime;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Stream;
import javafx.scene.control.*;
import javafx.scene.text.Text;
import javafx.scene.text.Font;
import com.jfoenix.controls.*;
import java.sql.SQLException;
import java.util.HashMap;
import edu.wpi.teamo.App;

public class RequestDisplay {

    HashMap<String, JFXCheckBox> typeCheckBoxes;
    JFXListView<VBox> reqList;
    JFXCheckBox showComplete;
    JFXTimePicker timePicker;
    JFXDatePicker datePicker;
    JFXCheckBox filterTime;
    String user;

    public static final String emergencyEntranceKey = App.resourceBundle.getString("key.emergency_entrance");
    public static final String normalEntranceKey = App.resourceBundle.getString("key.normal_entrance");
    private static double sRInfoBoxLeftMargin = 50;

    private List<String> openRequestViews;

    public RequestDisplay(JFXListView<VBox> reqDisplayListView,
                          HashMap<String, JFXCheckBox> typeCheckboxes,
                          JFXCheckBox showCompleted,
                          JFXTimePicker timePicker,
                          JFXDatePicker datePicker,
                          JFXCheckBox filterTime,
                          String user) {
        this.openRequestViews = new LinkedList<>();
        this.typeCheckBoxes = typeCheckboxes;
        this.showComplete = showCompleted;
        this.timePicker = timePicker;
        this.datePicker = datePicker;
        this.reqList = reqDisplayListView;
        this.filterTime = filterTime;
        this.user = user;
    }

    public void update() {
        reqList.getItems().clear();

        try {

            Stream<MedicineRequest> medRequests = MedicineRequest.getAll();
            Stream<SanitationRequest> sanRequests = SanitationRequest.getAll();
            Stream<SecurityRequest> secRequests = SecurityRequest.getAll();
            Stream<FoodRequest> foodRequests = FoodRequest.getAll();
            Stream<GiftRequest> giftRequests = GiftRequest.getAll();
            Stream<InterpreterRequest> interpRequests = InterpreterRequest.getAll();
            Stream<LaundryRequest> laundryRequests = LaundryRequest.getAll();
            Stream<MaintenanceRequest> maintRequests = MaintenanceRequest.getAll();
            Stream<ReligiousRequest> religRequests = ReligiousRequest.getAll();
            Stream<TransportationRequest> transRequests = TransportationRequest.getAll();
            Stream<COVIDSurveyRequest> covidRequests = COVIDSurveyRequest.getAll();

            if (typeCheckBoxes.get("Medicine").isSelected())         medRequests.forEach(r -> displaySRIfPassFilter(r, App.resourceBundle.getString("key.med_sr")));
            if (typeCheckBoxes.get("Sanitation").isSelected())       sanRequests.forEach(r -> displaySRIfPassFilter(r, App.resourceBundle.getString("key.san_sr")));
            if (typeCheckBoxes.get("Security").isSelected())         secRequests.forEach(r -> displaySRIfPassFilter(r, App.resourceBundle.getString("key.sec_sr")));
            if (typeCheckBoxes.get("Food").isSelected())            foodRequests.forEach(r -> displaySRIfPassFilter(r, App.resourceBundle.getString("key.food_sr")));
            if (typeCheckBoxes.get("Gift").isSelected())            giftRequests.forEach(r -> displaySRIfPassFilter(r, App.resourceBundle.getString("key.gift_sr")));
            if (typeCheckBoxes.get("Interpreter").isSelected())   interpRequests.forEach(r -> displaySRIfPassFilter(r, App.resourceBundle.getString("key.int_sr")));
            if (typeCheckBoxes.get("Laundry").isSelected())      laundryRequests.forEach(r -> displaySRIfPassFilter(r, App.resourceBundle.getString("key.laun_sr")));
            if (typeCheckBoxes.get("Maintenance").isSelected())    maintRequests.forEach(r -> displaySRIfPassFilter(r, App.resourceBundle.getString("key.main_sr")));
            if (typeCheckBoxes.get("Transportation").isSelected()) transRequests.forEach(r -> displaySRIfPassFilter(r, App.resourceBundle.getString("key.transportation_request")));
            if (typeCheckBoxes.get("Religious").isSelected())      religRequests.forEach(r -> displaySRIfPassFilter(r, App.resourceBundle.getString("key.rel_sr")));
            if (typeCheckBoxes.get("COVID Survey").isSelected())   covidRequests.forEach(r -> addCovidSurveyIfPassFilter(r, user));

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }


    }

    private void displaySRIfPassFilter(ExtendedBaseRequest m, String type) {
        boolean passFilter = true;

        if (!showComplete.isSelected() && m.isComplete()) {
            passFilter = false;
        }

        if (filterTime.isSelected()) {
            if (timePicker.getValue() != null && datePicker.getValue() != null) {
                LocalDateTime latestDate = datePicker.getValue().atTime(timePicker.getValue());
                if (latestDate.isBefore(m.getDue())) passFilter = false;
            }
        }

        if (user != null && !user.isEmpty()) {
            if (!m.getAssigned().equals(user)) {
                passFilter = false;
            }
        }

        if (passFilter) {
            VBox srBox = makeSRBox(m, type);
            reqList.getItems().add(srBox);
        }

    }

    public VBox makeSRBox(ExtendedBaseRequest m, String type) {
        VBox sRBox = new VBox();
        sRBox.setSpacing(10);
        sRBox.setAlignment(Pos.CENTER_LEFT);
        sRBox.setStyle("-fx-padding: 10px; -fx-background-color: #e5e5e5; -fx-effect: dropshadow(gaussian, rgba(170, 170, 170, 0.3), 10, 0.5, 0.0, 0.0)");

        //make expand button
        JFXButton expand = new JFXButton("+");

        HBox headerBox = makeBaseRequestHeaderBox(m, type, expand);
        sRBox.getChildren().add(headerBox);

        HBox editBox = makeBaseRequestEditBox(m);
        sRBox.getChildren().add(editBox);

        VBox extendedInfoBox = makeExtendedInfoBox(m, type);
        sRBox.getChildren().add(extendedInfoBox);

        expand.setOnAction(event -> {
            if (extendedInfoBox.isVisible()) {
                openRequestViews.remove(m.getID());
                retractSRBox(expand, editBox, extendedInfoBox);
            } else {
                openRequestViews.add(m.getID());
                expandSRBox(expand, editBox, extendedInfoBox);
            }
        });
        if (!openRequestViews.contains(m.getID())) {
            retractSRBox(expand, editBox, extendedInfoBox);
        }
        else {
            expandSRBox(expand, editBox, extendedInfoBox);
        }

        sRBox.getStyleClass().add("requestBox");
        return sRBox;
    }

    private HBox makeBaseRequestHeaderBox(ExtendedBaseRequest m, String type, JFXButton expand) {
        HBox hb = new HBox();
        hb.setAlignment(Pos.CENTER_LEFT);

        // style expand button
        expand.setMinWidth(30);
        expand.setPrefWidth(30);
//        expand.setMinHeight(30);
//        expand.setPrefHeight(30);
        expand.setStyle("-fx-border-radius: 5; -fx-background-radius: 5");
        hb.getChildren().add(expand);

        //text displaying type of request
        Text typeText = new Text(type);
        typeText.getStyleClass().add("annoyingText");
        typeText.setFont(new Font(20));
        HBox typeBox = new HBox(typeText);
        typeBox.setMinWidth(200);
        hb.getChildren().add(typeBox);

        HBox spacer = new HBox();
        hb.getChildren().add(spacer);

        spacer.setMinWidth(100);
        hb.setHgrow(spacer, Priority.ALWAYS);

        //time
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd/yyyy, " + "H:m:s");
        Text dueText = new Text(App.resourceBundle.getString("key.due_semicolon") + m.getDue().format(formatter));
        dueText.getStyleClass().add("annoyingText");
        hb.getChildren().add(dueText);

        //status (is complete)
        Text statusText = m.isComplete() ? new Text(App.resourceBundle.getString("key.complete"))
                                         : new Text(App.resourceBundle.getString("key.in_progress"));
        statusText.getStyleClass().add("annoyingText");
        hb.getChildren().add(statusText);

        hb.setMargin(statusText, new Insets(0, 10, 0, 0));

        hb.setSpacing(30);
        return hb;
    }

    private HBox makeBaseRequestEditBox(ExtendedBaseRequest m) {
        HBox editBox = new HBox();
        editBox.setSpacing(10);
        editBox.setAlignment(Pos.CENTER_LEFT);
        VBox.setMargin(editBox, new Insets(0, 0, 0, sRInfoBoxLeftMargin));

        //make assignee combo box of employees
        Label asigneeLabel = new Label(App.resourceBundle.getString("key.assigned_semicolon"));
        JFXComboBox<String> assignee = new JFXComboBox<>();
        initAsigneeBox(m, assignee);
        assignee.setOnAction(event -> handleChangeAsignee(assignee, m));
        HBox assigneeBox = new HBox(asigneeLabel, assignee);
        assigneeBox.setAlignment(Pos.CENTER_LEFT);
        editBox.getChildren().add(assigneeBox);

        JFXCheckBox completedBox = new JFXCheckBox();
        completedBox.setText(App.resourceBundle.getString("key.complete"));
        completedBox.setSelected(m.isComplete());
        completedBox.setOnAction(event -> handleChangeComplete(completedBox, m));
        editBox.getChildren().add(completedBox);

        return editBox;
    }

    private void handleChangeComplete(JFXCheckBox checkBox, ExtendedBaseRequest request) {
        try {
            request.setComplete(checkBox.isSelected());
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        update();
    }

    private void handleChangeAsignee(JFXComboBox<String> assignee, ExtendedBaseRequest m) {
        String newAssignee = assignee.getValue();
        if (newAssignee != null) {
            try {
                m.setAssigned(newAssignee);
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        }
        update();
    }

    private void initAsigneeBox(ExtendedBaseRequest m, JFXComboBox<String> assigneeBox) {
        Stream<Account> employees = Stream.empty();
        try {
            employees = Account.getAll().filter(Account::hasEmployeeAccess);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        employees.forEach(employee -> {
            assigneeBox.getItems().add(employee.getUsername());
        });

        if (assigneeBox.getItems().contains(m.getAssigned())) {
            assigneeBox.setValue(m.getAssigned());
        } //only put the assigned in the box if it is an employee
        else {
            assigneeBox.setValue("");
        }
    }

    public VBox makeExtendedInfoBox(ExtendedBaseRequest m, String type) {
        VBox eIBox = new VBox();
        eIBox.setSpacing(5);
        VBox.setMargin(eIBox, new Insets(0, 0, 0, sRInfoBoxLeftMargin));

        switch (type) {
            case "Medicine Request":
            case "Medicamento":
                try {
                    eIBox.getChildren().add(new Text("Type: " + MedicineRequest.getByID(m.getID()).getType()));
                    eIBox.getChildren().add(new Text("Amount: " + MedicineRequest.getByID(m.getID()).getAmount()));
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }
                break;
            case "Sanitation Request":
            case "Saneamiento":
                try {
                    eIBox.getChildren().add(new Text(App.resourceBundle.getString("key.recurring_semicolon") + SanitationRequest.getByID(m.getID()).isRecurring()));
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }
                break;
            case "Security Request":
            case "Seguridad":
                try {
                    eIBox.getChildren().add(new Text(App.resourceBundle.getString("key.emergency_semicolon") + SecurityRequest.getByID(m.getID()).isEmergency()));
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }
                break;
            case "Food Request":
            case "Alimento":
                try {
                    eIBox.getChildren().add(new Text(App.resourceBundle.getString("key.dietary_restrictions_semicolon") + FoodRequest.getByID(m.getID()).getdR()));
                    eIBox.getChildren().add(new Text(App.resourceBundle.getString("key.appetizer_semicolon") + FoodRequest.getByID(m.getID()).getAppetizer()));
                    eIBox.getChildren().add(new Text(App.resourceBundle.getString("key.entree_semicolon") + FoodRequest.getByID(m.getID()).getEntre()));
                    eIBox.getChildren().add(new Text(App.resourceBundle.getString("key.dessert_semicolon") + FoodRequest.getByID(m.getID()).getDessert()));
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }
                break;
            case "Gift Request":
            case "Regalo":
                if (type.equals("Gift Request")) {
                    try {
                        eIBox.getChildren().add(new Text(App.resourceBundle.getString("key.recipient_semicolon") + GiftRequest.getByID(m.getID()).getRecipient()));
                        eIBox.getChildren().add(new Text(App.resourceBundle.getString("key.tracking_id_semicolon") + GiftRequest.getByID(m.getID()).getTrackingID()));
                    } catch (SQLException throwables) {
                        throwables.printStackTrace();
                    }
                }
                break;
            case "Interpreter Request":
            case "Intérprete":
                try {
                    eIBox.getChildren().add(new Text(App.resourceBundle.getString("key.language_semicolon") + InterpreterRequest.getByID(m.getID()).getLanguage()));
                    eIBox.getChildren().add(new Text(App.resourceBundle.getString("key.type_semicolon")  + InterpreterRequest.getByID(m.getID()).getType()));
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }
                break;
            case "Laundry Request":
            case "Lavado de Ropa":
                try {
                    eIBox.getChildren().add(new Text(App.resourceBundle.getString("key.gown_semicolon") + LaundryRequest.getByID(m.getID()).getGown()));
                    eIBox.getChildren().add(new Text(App.resourceBundle.getString("key.bedding_semicolon") + LaundryRequest.getByID(m.getID()).getBedding()));
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }
                break;
            case "Maintenance Request":
            case "Mantenimiento":
                try {
                    eIBox.getChildren().add(new Text(App.resourceBundle.getString("key.type_semicolon") + MaintenanceRequest.getByID(m.getID()).getType()));
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }
                break;
            case "Religious Request":
            case "Religión":
                try {
                    eIBox.getChildren().add(new Text(App.resourceBundle.getString("key.service_semicolon") + ReligiousRequest.getByID(m.getID()).getService()));
                    eIBox.getChildren().add(new Text(App.resourceBundle.getString("key.figure_semicolon") + ReligiousRequest.getByID(m.getID()).getFigure()));
                    eIBox.getChildren().add(new Text(App.resourceBundle.getString("key.last_rights_semicolon") + ReligiousRequest.getByID(m.getID()).isLastRites()));
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }
                break;
            case "Transportation Request":
            case "Solicitude de transporte":
                try {
                    eIBox.getChildren().add(new Text(App.resourceBundle.getString("key.destination_semicolon") + TransportationRequest.getByID(m.getID()).getDestination()));
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }
        }
        eIBox.getChildren().add(new Text(App.resourceBundle.getString("key.details_semicolon") + m.getDetails()));
        eIBox.getChildren().add(new Text(App.resourceBundle.getString("key.ID_semicolon") + m.getID()));

        for (Node n : eIBox.getChildren()) {
            n.getStyleClass().add("annoyingText");
        }
        return eIBox;
    }

    private void expandSRBox(JFXButton expand, HBox editBox, VBox extendedInfoBox) {
        editBox.setVisible(true);
        editBox.setManaged(true);
        extendedInfoBox.setVisible(true);
        extendedInfoBox.setManaged(true);
        expand.setText("-");
    }

    private void retractSRBox(JFXButton expand, HBox editBox, VBox extendedInfoBox) {
        editBox.setVisible(false);
        editBox.setManaged(false);
        extendedInfoBox.setVisible(false);
        extendedInfoBox.setManaged(false);
        expand.setText("+");
    }


    public void addCovidSurveyIfPassFilter(COVIDSurveyRequest c, String user) {
        boolean passFilter = true;

        if (!showComplete.isSelected() && c.getIsComplete()) passFilter = false;

        if (user != null && !user.isEmpty()) {
            if (!user.equals(c.getUsername())) passFilter = false;
        }

        if (passFilter) {
            VBox mContainer = makeCovidSurveyBox(c, user);
            reqList.getItems().add(mContainer);
            System.out.println(c.getTimestamp());
        }
    }

    public VBox makeCovidSurveyBox(COVIDSurveyRequest c, String user) {
        VBox sB = new VBox();
        sB.setStyle("-fx-padding: 10px; -fx-background-color: #e5e5e5; -fx-effect: dropshadow(gaussian, rgba(170, 170, 170, 0.3), 10, 0.5, 0.0, 0.0)");
        sB.setSpacing(7);

        JFXButton expand = new JFXButton("+");
        expand.setMinWidth(30);
        expand.setPrefWidth(30);
        expand.setMinHeight(30);
        expand.setPrefHeight(30);
        expand.setStyle("-fx-border-radius: 5; -fx-background-radius: 5");

        HBox headerBox = makeCovidSurveyHeaderBox(c, expand);
        sB.getChildren().add(headerBox);

        HBox actionBox = makeCovidSurveyActionBox(c, user);
        sB.getChildren().add(actionBox);

        expand.setOnAction(event -> {
            if (actionBox.isVisible()) {
                openRequestViews.remove(c.getId());
                retractSurveyBox(expand, actionBox);
            } else {
                openRequestViews.add(c.getId());
                expandSurveyBox(expand, actionBox);
            }
        });
        if (!openRequestViews.contains(c.getId())) {
            retractSurveyBox(expand, actionBox);
        }
        else {
            expandSurveyBox(expand, actionBox);
        }

        return sB;
    }

    private void expandSurveyBox(JFXButton expand, HBox actionBox) {
        actionBox.setVisible(true);
        actionBox.setManaged(true);
        expand.setText("-");
    }

    private void retractSurveyBox(JFXButton expand, HBox actionBox) {
        actionBox.setVisible(false);
        actionBox.setManaged(false);
        expand.setText("+");
    }

    private HBox makeCovidSurveyHeaderBox(COVIDSurveyRequest c, JFXButton expandButton) {
        HBox headerBox = new HBox();
        headerBox.getChildren().add(expandButton);
        headerBox.setSpacing(10);

        Text typeText = new Text(App.resourceBundle.getString("key.COVID_survey_requests"));
        typeText.setFont(new Font(20));
        HBox typeBox = new HBox(typeText);
        typeBox.setMinWidth(200);
        headerBox.getChildren().add(typeBox);

        //assigned person display
        Text userText = new Text(App.resourceBundle.getString("key.user_semicolon") + c.getUsername());
        HBox userBox = new HBox(userText);
        userBox.setMinWidth(100);
        userBox.setPrefWidth(100);
        userBox.setMaxWidth(100);
        headerBox.getChildren().add(userBox);

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd/yyyy, " + "H:m:s");
        Text timeText = new Text(App.resourceBundle.getString("key.submitted_at_semicolon") + c.getTimestamp().format(formatter));
        timeText.setWrappingWidth(130);
        HBox dateBox = new HBox(timeText);
        dateBox.setMinWidth(100);
        dateBox.setPrefWidth(100);
        dateBox.setMaxWidth(100);
        headerBox.getChildren().add(dateBox);

        Text statusText = new Text();
        if (c.getIsComplete()) statusText.setText("Complete");
        else statusText.setText("In progress");
        headerBox.getChildren().add(statusText);

        return headerBox;
    }

    private HBox makeCovidSurveyActionBox(COVIDSurveyRequest c, String user) {
        HBox actionBox = new HBox();
        actionBox.setSpacing(10);

        Text emergencyEntrance = new Text(App.resourceBundle.getString(c.getUseEmergencyEntrance()
                                                                       ? "key.recommended_emergency"
                                                                       : "key.recommended_normal"));
        emergencyEntrance.setWrappingWidth(120);
        HBox entranceBox = new HBox(emergencyEntrance);
        entranceBox.setMinWidth(130);
        entranceBox.setPrefWidth(130);
        entranceBox.setMaxWidth(130);
        actionBox.getChildren().add(emergencyEntrance);

        JFXButton applyButton = new JFXButton(App.resourceBundle.getString("key.apply"));
        applyButton.setStyle("-fx-border-radius: 5; -fx-background-radius: 5");
        applyButton.setVisible(false);

        JFXComboBox<String> entranceComboBox = new JFXComboBox<>();
        entranceComboBox.getItems().add(normalEntranceKey);
        entranceComboBox.getItems().add(emergencyEntranceKey);
        entranceComboBox.setValue(getUserEmergencyEntranceStatus(c) ? emergencyEntranceKey : normalEntranceKey);
        entranceComboBox.setOnAction(event -> applyButton.setVisible(true));
        actionBox.getChildren().add(entranceComboBox);

        applyButton.setOnAction(event -> handleChangeEntryStatus(entranceComboBox, c));
        actionBox.getChildren().add(applyButton);

        JFXCheckBox completedBox = new JFXCheckBox();
        completedBox.setText(App.resourceBundle.getString("key.complete"));
        completedBox.setSelected(c.getIsComplete());
        completedBox.setOnAction(event -> handleChangeCovidSurveyComplete(completedBox, c));
        actionBox.getChildren().add(completedBox);

        return actionBox;
    }

    private boolean getUserEmergencyEntranceStatus(COVIDSurveyRequest c) {
        Account account = null;
        try {
            account = Account.getByUsername(c.getUsername());
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        if (account != null) {
            return account.getUseEmergencyEntrance();
        }
        else {
            System.out.println("Cannot retrieve covid survey submitter's account");
            return true;
        }

    }

    private void handleChangeEntryStatus(JFXComboBox<String> entranceComboBox, COVIDSurveyRequest c) {
        String newEntryStatusString = entranceComboBox.getValue();
        if (!newEntryStatusString.equals(normalEntranceKey) && !newEntryStatusString.equals(emergencyEntranceKey)) {
            System.out.println("random value in covid approval combobox??");
            return;
        }

        boolean useEmergencyEntrance = newEntryStatusString.equals(emergencyEntranceKey);
        try {
            Account.getByUsername(c.getUsername()).setUseEmergencyEntrance(useEmergencyEntrance);
            Account.getByUsername(c.getUsername()).setTakenSurvey(true);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        update();
    }

    private void handleChangeCovidSurveyComplete(JFXCheckBox checkBox, COVIDSurveyRequest request) {
        try {
            request.setComplete(checkBox.isSelected());
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        update();
    }

}
